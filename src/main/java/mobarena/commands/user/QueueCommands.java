package mobarena.commands.user;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import mobarena.database.Warp;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class QueueCommands {
    public static ServerCommandSource source;
    public static ServerPlayerEntity Player;
    public static String arenaName;


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("majoin")
                .then(CommandManager.argument("arenaname", StringArgumentType.word())
                        .executes(context -> {
                            source = context.getSource();
                            Player = source.getPlayer();

                            RegistryKey<World> world = Player.world.getRegistryKey();
                            arenaName = StringArgumentType.getString(context, "arenaname");

                            if (Warp.getLobbyWarp(arenaName) != null) {
                                Warp lobbyWarp = Warp.getLobbyWarp(arenaName);
                                if (Player.isSleeping()) Player.wakeUp(true, true);
                                //TODO check for world
                                Player.networkHandler.requestTeleport(lobbyWarp.x, lobbyWarp.y, lobbyWarp.z, lobbyWarp.yaw, lobbyWarp.pitch);
                                return 1;
                            } else {
                                context.getSource().sendFeedback(new LiteralText("Arena or warp does not exist!"), false);
                                return 0;
                            }
                        })));

        dispatcher.register(CommandManager.literal("maready")
                .executes(context -> {
                    if (Warp.getArenaWarp(arenaName) != null) {
                        Warp arenaWarp = Warp.getArenaWarp(arenaName);
                        if (Player.isSleeping()) Player.wakeUp(true, true);
                        //TODO check for world
                        Player.networkHandler.requestTeleport(arenaWarp.x, arenaWarp.y, arenaWarp.z, arenaWarp.yaw, arenaWarp.pitch);
                        return 1;
                    } else {
                        context.getSource().sendFeedback(new LiteralText("Warp has not been set up or is unavailable"), false);
                        return 0;
                    }
                }));
    }
}
