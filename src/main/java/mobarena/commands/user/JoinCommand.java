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

public class JoinCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("joinarena")
                .then(CommandManager.argument("arenaname", StringArgumentType.word())
                        .executes(context -> {
                            final ServerCommandSource source = context.getSource();
                            ServerPlayerEntity Player = source.getPlayer();

                            RegistryKey<World> world = Player.world.getRegistryKey();
                            String arenaName = StringArgumentType.getString(context, "arenaname");

                            if (Warp.getMainWarp(arenaName) != null) {
                                Warp mainWarp = Warp.getMainWarp(arenaName);
                                if (Player.isSleeping()) Player.wakeUp(true, true);
                                Player.networkHandler.requestTeleport(mainWarp.x, mainWarp.y, mainWarp.z, mainWarp.yaw, mainWarp.pitch);
                                return 1;
                            } else {
                                context.getSource().sendFeedback(new LiteralText("Arena or warp does not exist!"), false);
                                return 0;
                            }
                        })));
    }
}
