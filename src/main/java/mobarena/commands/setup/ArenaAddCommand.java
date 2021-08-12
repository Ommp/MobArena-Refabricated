package mobarena.commands.setup;

import com.google.gson.Gson;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import mobarena.Arena;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class ArenaAddCommand {

public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
    dispatcher.register(CommandManager.literal("createarena")
            .then(CommandManager.argument("arenaname", StringArgumentType.word())
            .then(CommandManager.argument("x_1", DoubleArgumentType.doubleArg())
            .then(CommandManager.argument("y_1", DoubleArgumentType.doubleArg())
            .then(CommandManager.argument("z_1", DoubleArgumentType.doubleArg())
            .then(CommandManager.argument("x_2", DoubleArgumentType.doubleArg())
            .then(CommandManager.argument("y_2", DoubleArgumentType.doubleArg())
            .then(CommandManager.argument("z_2", DoubleArgumentType.doubleArg())

                        .executes(context -> {
                            final ServerCommandSource source = context.getSource();
                            ServerPlayerEntity senderPlayer = source.getPlayer();

                            RegistryKey<World> world = senderPlayer.world.getRegistryKey();
                            String arenaName = StringArgumentType.getString(context, "arenaname");
                            Vec3d pos1 = new Vec3d(DoubleArgumentType.getDouble(context, "x_1"), DoubleArgumentType.getDouble(context, "y_1"), DoubleArgumentType.getDouble(context, "z_1"));
                            Vec3d pos2 = new Vec3d(DoubleArgumentType.getDouble(context, "x_2"), DoubleArgumentType.getDouble(context, "y_2"), DoubleArgumentType.getDouble(context, "z_2"));

                            context.getSource().sendFeedback(new LiteralText("Created arena: " + arenaName), true);
                            context.getSource().sendFeedback(new LiteralText("From: " + pos1 + " to: " + pos2), true);
                            context.getSource().sendFeedback(new LiteralText("In world: " + world.getValue()), true);

                            Gson gson = new Gson();

                            Arena arena = new Arena(arenaName, pos1, pos2, world);


                            String json = gson.toJson(arena);

                            context.getSource().sendFeedback(new LiteralText(json), true);

                            return 1;
                    })))))))));
        }
    }
