package mobarena.commands.setup;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import mobarena.database.Arena;
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
            .then(CommandManager.argument("name", StringArgumentType.word())

                        .executes(context -> {
                            final ServerCommandSource source = context.getSource();
                            ServerPlayerEntity senderPlayer = source.getPlayer();

                            RegistryKey<World> world = senderPlayer.world.getRegistryKey();
                            String name = StringArgumentType.getString(context, "name");

                            if (Arena.get(name) != null) {
                                context.getSource().sendFeedback(new LiteralText("Error: Arena already exists!"), true);
                                return 0;
                            }

                            Arena.add(name, world.getValue().toString());
                            context.getSource().sendFeedback(new LiteralText("Successfully created arena."), true);
                            return 1;
                    })));
        }
    }
