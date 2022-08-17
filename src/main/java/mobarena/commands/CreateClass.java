package mobarena.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mobarena.MobArena;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

public class CreateClass implements Command {

    private int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name").toLowerCase();

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        MobArena.database.createClass(name);
        player.sendMessage(new TranslatableText("mobarena.createdclass", name), false);
        return 1;
    }

    public LiteralCommandNode<ServerCommandSource> getNode() {
        return CommandManager
                .literal("addclass")
                .then(
                        CommandManager.argument("name", StringArgumentType.greedyString()).executes(this::run)
                )
                .build();
    }
}