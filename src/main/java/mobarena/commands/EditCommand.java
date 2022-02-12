package mobarena.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mobarena.Arena;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

public class EditCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        LiteralCommandNode<ServerCommandSource> mobArenaNode  = CommandManager
                .literal("mobarena")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> {
                    sendText(context, new TranslatableText("mobarena.main"));
                    return 1;
                }).build();

        LiteralCommandNode<ServerCommandSource> editNode = CommandManager
                .literal("edit")
                .executes(context -> {
                    Arena arena;
                    sendText(context, new TranslatableText("mobarena.edit"));
                    return 1;
                }).build();

        LiteralCommandNode<ServerCommandSource> addNode = CommandManager
                .literal("add")
                .executes(context -> {
                    sendText(context, new TranslatableText("mobarena.add"));
                    return 1;
                }).build();

        LiteralCommandNode<ServerCommandSource> deleteNode = CommandManager
                .literal("delete")
                .executes(context -> {
                    sendText(context, new TranslatableText("mobarena.delete"));
                    return 1;
                }).build();


        dispatcher.getRoot().addChild(mobArenaNode);

        mobArenaNode.addChild(addNode);
        mobArenaNode.addChild(editNode);
        mobArenaNode.addChild(deleteNode);

    }

    private static void sendText(CommandContext<ServerCommandSource> context, TranslatableText text) {
        context.getSource().sendFeedback(text, true);
    }

}
