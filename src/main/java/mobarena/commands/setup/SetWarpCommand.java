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
                        //TODO add warp type argument
//                .then(CommandManager.argument("warptype", StringArgumentType.word())

                        .executes(context -> {
                            final ServerCommandSource source = context.getSource();
                            ServerPlayerEntity Player = source.getPlayer();

                            RegistryKey<World> world = Player.world.getRegistryKey();
                            String name = StringArgumentType.getString(context, "name");


                            if (Arena.get(name) == null) {
                                context.getSource().sendFeedback(new LiteralText("Error: That arena does not exist!"), true);
                                return 0;
                            }
                            context.getSource().sendFeedback(new LiteralText(world.getValue().toString()), false);
                            Warp.setArenaWarp(name, Player.getX(),Player.getY(),Player.getZ(), Player.yaw, Player.pitch, world.getValue().toString());

                            context.getSource().sendFeedback(new LiteralText("Successfully set warp."), false);
                            return 1;
                        })));
    }
}