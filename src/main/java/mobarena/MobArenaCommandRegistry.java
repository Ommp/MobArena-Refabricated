package mobarena;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import mobarena.commands.ArenaCreateCommand;
import mobarena.commands.ArenaEditCommand;
import mobarena.commands.ArenaInfoCommand;
import mobarena.commands.ModInfoCommand;
import mobarena.commands.suggestion.NameSuggestionProvider;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.util.IConsumer;


import static net.minecraft.server.command.CommandManager.argument;

public class MobArenaCommandRegistry {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        RootCommandNode<ServerCommandSource> rootNode = dispatcher.getRoot();

        LiteralCommandNode<ServerCommandSource> mobArenaCommandsRootNode;
        {
            LiteralCommandNode<ServerCommandSource> maInfoNode = CommandManager.literal("info")
                    .executes(new ModInfoCommand())
                    .build();

            mobArenaCommandsRootNode = CommandManager.literal("ma")
                    .executes(maInfoNode.getCommand())
                    .build();

            mobArenaCommandsRootNode.addChild(maInfoNode);
        }

        IConsumer<LiteralCommandNode<ServerCommandSource>> registerNode = (node) -> {
            rootNode.addChild(node);
            mobArenaCommandsRootNode.addChild(node);
        };

        LiteralArgumentBuilder<ServerCommandSource> arenaBuilder = CommandManager.literal("arena");
        LiteralArgumentBuilder<ServerCommandSource> arenaInfoBuilder = CommandManager.literal("info");
        LiteralArgumentBuilder<ServerCommandSource> arenaEditBuilder = CommandManager.literal("edit");
        LiteralArgumentBuilder<ServerCommandSource> arenaCreateBuilder = CommandManager.literal("create");
        LiteralArgumentBuilder<ServerCommandSource> arenaDeleteBuilder = CommandManager.literal("delete");
        LiteralArgumentBuilder<ServerCommandSource> arenaListBuilder = CommandManager.literal("list");


        arenaInfoBuilder.then(argument("arena_name", StringArgumentType.word()).suggests(new NameSuggestionProvider.Arena())
                .executes(new ArenaInfoCommand()));
        arenaEditBuilder.then(argument("arena_name", StringArgumentType.word()).suggests(new NameSuggestionProvider.Arena())
                .executes(new ArenaEditCommand()));
        arenaCreateBuilder.then(argument("arena_name", StringArgumentType.word()))
                .executes(new ArenaCreateCommand());


        LiteralCommandNode<ServerCommandSource> arenaNode = arenaBuilder
                .build();
        arenaNode.addChild(arenaInfoBuilder.build());
        arenaNode.addChild(arenaEditBuilder.build());
        arenaNode.addChild(arenaCreateBuilder.build());

       registerNode.accept(arenaNode);


        rootNode.addChild(mobArenaCommandsRootNode);

    }


}
