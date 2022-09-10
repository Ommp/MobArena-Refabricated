package mobarena.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mobarena.MobArena;
import mobarena.commands.suggestions.NameSuggestionProvider;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

public class JoinArena implements Command{
    private int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();
        if (MobArena.arenaManager.checkArenaExists(name)) {

            MobArena.arenaManager.loadArena(name);
            if (MobArena.arenaManager.arenas.get(name).isPlayerInArena(player)) {
                player.sendMessage(new TranslatableText("mobarena.alreadyjoined", name), false);
                return 0;
            }
            if(MobArena.arenaManager.isPlayerActive(player)) {
                player.sendMessage(new TranslatableText("mobarena.alreadyinanotherarena"), false);
                return 0;
            }
            if (MobArena.arenaManager.arenas.get(name).isRunning()) {
                player.sendMessage(new TranslatableText("mobarena.arenaalreadyrunning"), false);
                return 0;
            }
                MobArena.arenaManager.arenas.get(name).joinLobby(player);
                return 1;
            }

        else {
            player.sendMessage(new TranslatableText("mobarena.arenanotfound", name), false);
            return 0;
        }
    }

    public LiteralCommandNode<ServerCommandSource> getNode() {
        return CommandManager
                .literal("join")
                .then(
                        CommandManager.argument("name", StringArgumentType.greedyString()).executes(this::run).suggests(new NameSuggestionProvider())
                )
                .build();
    }
}