package mobarena.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mobarena.ArenaManager;
import mobarena.MobArena;
import mobarena.commands.suggestions.NameSuggestionProvider;
import mobarena.config.ArenaModel;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

public class CreateArena implements Command {

    private int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (ArenaManager.checkArenaExists(name)) {
            player.sendMessage(new TranslatableText("mobarena.arenaexists", name), false);
            return 0;
        }
        else {
            MobArena.database.addArena(name);
            ArenaManager.loadInactiveArena(name);
            ArenaManager.addArenaNames();
            var arenaModel = new ArenaModel();
            arenaModel.createDefaultReinforcements();
            MobArena.arenaConfig.addArenaConfig(name, arenaModel);
            MobArena.arenaConfig.save();
            player.sendMessage(new TranslatableText("mobarena.createdarena", name), false);
        }

        return 1;
    }

    public LiteralCommandNode<ServerCommandSource> getNode() {
        return CommandManager
                .literal("create")
                .requires(source -> source.hasPermissionLevel(2))
                .then(
                        CommandManager.argument("name", StringArgumentType.greedyString()).executes(this::run).suggests(new NameSuggestionProvider())
                )
                .build();
    }
}