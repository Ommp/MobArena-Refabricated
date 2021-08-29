package mobarena.commands.setup;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import mobarena.database.Arena;
import mobarena.database.Warp;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class SetWarpCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("masetwarp")
                .then(CommandManager.argument("name", StringArgumentType.word())
                .then(CommandManager.argument("warptype", StringArgumentType.word())

                        .executes(context -> {
                            final ServerCommandSource source = context.getSource();
                            ServerPlayerEntity Player = source.getPlayer();

                            RegistryKey<World> world = Player.world.getRegistryKey();
                            String name = StringArgumentType.getString(context, "name");
                            String warpType = StringArgumentType.getString(context, "warptype");


                            if (Arena.get(name) == null) {
                                context.getSource().sendFeedback(new LiteralText("Error: That arena does not exist!"), true);
                                return 0;
                            }
                            switch (warpType){
                                case "arena":
                                    Warp.setArenaWarp(name, Player.getX(),Player.getY(),Player.getZ(), Player.yaw, Player.pitch, world.getValue().toString());
                                    break;
                                case "lobby":
                                    Warp.setLobbyWarp(name, Player.getX(),Player.getY(),Player.getZ(), Player.yaw, Player.pitch, world.getValue().toString());
                                    break;
                                case "exit":
                                    Warp.setExitWarp(name, Player.getX(),Player.getY(),Player.getZ(), Player.yaw, Player.pitch, world.getValue().toString());
                                    break;
                                default:
                                    context.getSource().sendFeedback(new LiteralText("Invalid warp type."), false);
                                    return 0;
                            }

                            context.getSource().sendFeedback(new LiteralText("Successfully set warp " + warpType), false);
                            return 1;
                        }))));
    }
}