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
import net.minecraft.text.Text;

public class DeleteArena implements Command {

    private int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();


            MobArena.database.deleteArena(name);
            ArenaManager.arenas.remove(name);
            player.sendMessage(Text.translatable("mobarena.deletedarena", name), false);
            return 0;
    }

    public LiteralCommandNode<ServerCommandSource> getNode() {
        return CommandManager
                .literal("delete")
                .requires(source -> source.hasPermissionLevel(2))
                .then(
                        CommandManager.argument("name", StringArgumentType.greedyString()).executes(this::run).suggests(new NameSuggestionProvider())
                )
                .build();
    }
}