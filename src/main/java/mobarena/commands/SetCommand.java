package mobarena.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
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

public class SetCommand implements Command{

    private int setMinPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int number = IntegerArgumentType.getInteger(context, "number");
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        MobArena.database.setMinPlayers(number, name);
        player.sendMessage(new TranslatableText("mobarena.updatedmin"), false);
        ArenaManager.reloadArena(name);
        return 1;
    }
    private int setMaxPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int number = IntegerArgumentType.getInteger(context, "number");
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        MobArena.database.setMaxPlayers(number, name);
        player.sendMessage(new TranslatableText("mobarena.updatedmax"), false);
        ArenaManager.reloadArena(name);
        return 1;
    }

    @Override
    public LiteralCommandNode<ServerCommandSource> getNode() {
        return CommandManager
                .literal("set")
                .then(CommandManager.literal("minPlayers").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("number", IntegerArgumentType.integer())
                .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(new NameSuggestionProvider())
                .executes(this::setMinPlayers))))

                .then(CommandManager.literal("maxPlayers").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("number", IntegerArgumentType.integer())
                .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(new NameSuggestionProvider())
                .executes(this::setMaxPlayers))))

                .build();
    }
}
