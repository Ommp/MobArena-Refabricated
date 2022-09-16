package mobarena.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mobarena.ArenaManager;
import mobarena.MobArena;
import mobarena.commands.suggestions.NameSuggestionProvider;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

public class SetP2 implements Command{

    private int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        MobArena.database.updateP2(name, player.getBlockX(), player.getBlockY(), player.getBlockZ());
        player.sendMessage(new TranslatableText("mobarena.updatedpos"), false);
        ArenaManager.reloadArena(name);
        return 1;
    }

    @Override
    public LiteralCommandNode<ServerCommandSource> getNode() {
        return CommandManager
                .literal("setp2")
                .requires(source -> source.hasPermissionLevel(2))
                .then(
                        CommandManager.argument("name", StringArgumentType.greedyString()).executes(this::run).suggests(new NameSuggestionProvider())
                )
                .build();
    }
}