package mobarena;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import mobarena.commands.ArenaCreateCommand;
import mobarena.commands.ArenaEditCommand;
import mobarena.commands.ArenaInfoCommand;
import mobarena.commands.ModInfoCommand;
import mobarena.commands.set.SetArenaCommand;
import mobarena.commands.set.SetExitCommand;
import mobarena.commands.set.SetLobbyCommand;
import mobarena.commands.set.SetSpectatorCommand;
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

        LiteralArgumentBuilder<ServerCommandSource> setLobbyWarpBuilder = CommandManager.literal("setlobbywarp");
        LiteralArgumentBuilder<ServerCommandSource> setArenaWarpBuilder = CommandManager.literal("setarenawarp");
        LiteralArgumentBuilder<ServerCommandSource> setExitWarpBuilder = CommandManager.literal("setexitwarp");
        LiteralArgumentBuilder<ServerCommandSource> setSpectatorWarpBuilder = CommandManager.literal("setspectatorwarp");
        LiteralArgumentBuilder<ServerCommandSource> setNameBuilder = CommandManager.literal("setname");
        LiteralArgumentBuilder<ServerCommandSource> setWorldNameBuilder = CommandManager.literal("setworldname");
        LiteralArgumentBuilder<ServerCommandSource> setEnabledBuilder = CommandManager.literal("setenabled");
        LiteralArgumentBuilder<ServerCommandSource> setArenaRegionBuilder = CommandManager.literal("setarenaRegion");
        LiteralArgumentBuilder<ServerCommandSource> setLobbyRegionBuilder = CommandManager.literal("setlobbyRegion");


        arenaInfoBuilder.then(argument("arena_name", StringArgumentType.word()).suggests(new NameSuggestionProvider.Arena())
                .executes(new ArenaInfoCommand()));
        arenaEditBuilder.then(argument("arena_name", StringArgumentType.word()).suggests(new NameSuggestionProvider.Arena())
                .executes(new ArenaEditCommand()));
        arenaCreateBuilder.then(argument("arena_name", StringArgumentType.word())
                .executes(new ArenaCreateCommand()));

        setLobbyWarpBuilder.then(argument("arena_name", StringArgumentType.word()).suggests(new NameSuggestionProvider.Arena())
                .executes(new SetLobbyCommand()));
        setArenaWarpBuilder.then(argument("arena_name", StringArgumentType.word()).suggests(new NameSuggestionProvider.Arena())
                .executes(new SetArenaCommand()));
        setExitWarpBuilder.then(argument("arena_name", StringArgumentType.word()).suggests(new NameSuggestionProvider.Arena())
                .executes(new SetExitCommand()));
        setSpectatorWarpBuilder.then(argument("arena_name", StringArgumentType.word()).suggests(new NameSuggestionProvider.Arena())
                .executes(new SetSpectatorCommand()));





        LiteralCommandNode<ServerCommandSource> arenaNode = arenaBuilder
                .build();
        arenaNode.addChild(arenaInfoBuilder.build());
        arenaNode.addChild(arenaEditBuilder.build());
        arenaNode.addChild(arenaCreateBuilder.build());

        arenaNode.addChild(setLobbyWarpBuilder.build());
        arenaNode.addChild(setArenaWarpBuilder.build());
        arenaNode.addChild(setExitWarpBuilder.build());
        arenaNode.addChild(setSpectatorWarpBuilder.build());



       registerNode.accept(arenaNode);


        rootNode.addChild(mobArenaCommandsRootNode);

    }


}
