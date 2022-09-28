package mobarena.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

import static mobarena.ArenaManager.getArenaFromPlayer;

public class Ready implements Command{
    private int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (getArenaFromPlayer(player).forceClass()) {
            if (!getArenaFromPlayer(player).playerHasClass(player.getUuidAsString())) {
                player.sendMessage(new TranslatableText("mobarena.selectaclass"), false);
            }
            else {
                getArenaFromPlayer(player).addReadyLobbyPlayer(player);
            }
        }
        if (!getArenaFromPlayer(player).forceClass()){
            getArenaFromPlayer(player).addReadyLobbyPlayer(player);
        }
            return 1;
    }

    public LiteralCommandNode<ServerCommandSource> getNode() {
        return CommandManager
                .literal("ready")
                .executes(this::run)
                .build();
    }
}
