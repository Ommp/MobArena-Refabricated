package mobarena.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mobarena.ArenaManager;
import mobarena.PlayerManager;
import mobarena.commands.suggestions.NameSuggestionProvider;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.GameMode;

public class SpectateArena implements Command{
    private int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity p = source.getPlayer();

        ArenaManager.loadArena(name);
        if (ArenaManager.checkArenaExists(name)) {

            if (ArenaManager.arenas.get(name).isPlayerInArena(p)) {
                p.sendMessage(new TranslatableText("mobarena.alreadyjoined", name), false);
                return 0;
            }
            if(ArenaManager.isPlayerActive(p)) {
                p.sendMessage(new TranslatableText("mobarena.alreadyinanotherarena"), false);
                return 0;
            }

            PlayerManager.savePlayerInventory(p);
            PlayerManager.clearInventory(p);
            PlayerManager.setGameMode(p, GameMode.SPECTATOR);
            ArenaManager.addActivePlayer(p, name);
            ArenaManager.arenas.get(name).transportPlayer(p, "spec");
            ArenaManager.arenas.get(name).addSpectatorPlayer(p);
            p.sendMessage(new TranslatableText("mobarena.joinedspec", name), false);
            return 1;
        }

        else {
            p.sendMessage(new TranslatableText("mobarena.arenanotfound", name), false);
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