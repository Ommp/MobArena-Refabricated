package mobarena.commands.user;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import mobarena.database.Warp;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class ReadyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("maready")
                .then(CommandManager.argument("arenaname", StringArgumentType.word())
                .executes(context -> {
                    final ServerCommandSource source = context.getSource();
                    ServerPlayerEntity Player = source.getPlayer();

                    String arenaName = StringArgumentType.getString(context, "arenaname");
                    Warp arenaWarp = Warp.getArenaWarp(arenaName);

                    Player.networkHandler.requestTeleport(arenaWarp.x, arenaWarp.y, arenaWarp.z, arenaWarp.yaw, arenaWarp.pitch);
                    return 1;

                    // if playerhasjoinedarena == false, return 0
                })));
    }
}