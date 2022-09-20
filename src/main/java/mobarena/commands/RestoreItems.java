package mobarena.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mobarena.ArenaManager;
import mobarena.MobArena;
import mobarena.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class RestoreItems implements Command {
    private int restore(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (MobArena.database.playerInventoryRowExists(player.getUuidAsString()) && !ArenaManager.isPlayerActive(player)) {
        PlayerManager.clearInventory(player);
        PlayerManager.retrieveItems(player);
        }
        return 1;
    }

    public LiteralCommandNode<ServerCommandSource> getNode() {
        return CommandManager
                .literal("restoreitems")
                .executes(this::restore)
                .build();
    }
}
