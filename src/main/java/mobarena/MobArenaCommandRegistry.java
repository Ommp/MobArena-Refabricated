import mobarena.commands.*;

import java.io.FileNotFoundException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.function.Predicate;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

import java.util.function.Predicate;

import static net.minecraft.server.command.CommandManager.argument;

public class MobArenaCommandRegistry {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(
            (CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) -> {
                LiteralCommandNode<ServerCommandSource> mobarenaCommandsRootNode =
                    CommandManager.literal("mobarenacommands").build();


                LiteralArgumentBuilder<ServerCommandSource> arenaAddBuilder = CommandManager.literal("add");
                // LiteralArgumentBuilder<ServerCommandSource> arenaJoinBuilder = CommandManager.literal("join");
                // LiteralArgumentBuilder<ServerCommandSource> arenaRemoveBuilder = CommandManager.literal("remove");



                arenaAddBuilder.then(argument("arena_name", StringArgumentType.word()).executes(new ArenaAddCommand()));
                // LiteralCommandNode<ServerCommandSource> addArenaNode = CommandManager.literal("add")
                // .then(argument("arena_name", StringArgumentType.word()).executes(new ArenaAddCommand()));
            }
        );
    }
}