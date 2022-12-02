package mobarena.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mobarena.ArenaManager;
import mobarena.PlayerManager;
import mobarena.WarpType;
import mobarena.commands.suggestions.NameSuggestionProvider;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

public class SpectateArena implements Command{
    private int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity p = source.getPlayer();

        ArenaManager.loadActiveArena(name);
        if (ArenaManager.checkArenaExists(name)) {

            if (ArenaManager.arenas.get(name).isPlayerInArena(p)) {
                p.sendMessage(Text.translatable("mobarena.alreadyjoined", name), false);
                return 0;
            }
            if(ArenaManager.isPlayerActive(p)) {
                p.sendMessage(Text.translatable("mobarena.alreadyinanotherarena"), false);
                return 0;
            }

            PlayerManager.savePlayerInventory(p);
            PlayerManager.clearInventory(p);
            PlayerManager.setGameMode(p, GameMode.SPECTATOR);
            ArenaManager.addActivePlayer(p, name);
            ArenaManager.arenas.get(name).transportPlayer(p, WarpType.SPECTATOR);
            ArenaManager.arenas.get(name).addSpectatorPlayer(p);
            p.sendMessage(Text.translatable("mobarena.joinedspec", name), false);
            return 1;
        }

        else {
            p.sendMessage(Text.translatable("mobarena.arenanotfound", name), false);
            return 0;
        }
    }

    public LiteralCommandNode<ServerCommandSource> getNode() {
        return CommandManager
                .literal("spectate")
                .then(
                        CommandManager.argument("name", StringArgumentType.greedyString()).executes(this::run).suggests(new NameSuggestionProvider())
                )
                .build();
    }
}