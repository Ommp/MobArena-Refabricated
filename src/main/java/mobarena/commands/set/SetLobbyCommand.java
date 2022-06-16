package mobarena.commands.set;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mobarena.MobArena;
import mobarena.Warp;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;

public class SetLobbyCommand implements Command<ServerCommandSource> {

    public SetLobbyCommand() {}

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        String arenaName = StringArgumentType.getString(context, "arena_name");

        BlockPos coordinates = context.getSource().getEntity().getBlockPos();
        float yaw = context.getSource().getEntity().yaw;
        float pitch = context.getSource().getEntity().pitch;

        Warp warp = new Warp(coordinates, yaw, pitch);

        MobArena.config.arenas.get(arenaName).lobby = warp;

        return 0;
    }
}
