package mobarena.commands.user;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import mobarena.database.Warp;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class ReadyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("maready")
                .then(CommandManager.argument("arenaname", StringArgumentType.word())
                .executes(context -> {
                    final ServerCommandSource source = context.getSource();
                    ServerPlayerEntity Player = source.getPlayer();

                    String arenaName = StringArgumentType.getString(context, "arenaname");



                    if (Warp.getArenaWarp(arenaName) != null) {
                        Warp arenaWarp = Warp.getArenaWarp(arenaName);
                        if (Player.isSleeping()) Player.wakeUp(true, true);
                        Player.networkHandler.requestTeleport(arenaWarp.x, arenaWarp.y, arenaWarp.z, arenaWarp.yaw, arenaWarp.pitch);
                        return 1;
                    } else {
                        //add check to make sure it's the same arena as the one that was joined.
                        context.getSource().sendFeedback(new LiteralText("Could not fetch arena warp, or wrong arena."), false);
                        return 0;
                    }

                    // if playerhasjoinedarena == false, return 0
                })));
    }
}