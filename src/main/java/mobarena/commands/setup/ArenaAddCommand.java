package mobarena.commands.setup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import mobarena.Arena;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.io.*;
import java.nio.file.Path;

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

                            Arena arena = new Arena(arenaName, pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z, world.getValue().toString());

                            Gson gson = new GsonBuilder()
                                    .setPrettyPrinting()
                                    .create();

                            String json = gson.toJson(arena);
                            Path configFolder = FabricLoader.getInstance().getConfigDir();
                            File arenas = new File(configFolder + "/arenas.json");
                            try {
                                arenas.createNewFile();
                                FileOutputStream fOut = new FileOutputStream(arenas);
                                OutputStreamWriter myOutWriter =new OutputStreamWriter(fOut);
                                myOutWriter.append(json);
                                myOutWriter.close();
                                fOut.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            context.getSource().sendFeedback(new LiteralText(arenas.toString()), true);
                            return 1;
                    })))))))));
        }
    }
