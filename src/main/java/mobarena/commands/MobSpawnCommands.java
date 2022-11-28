package mobarena.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mobarena.ArenaManager;
import mobarena.MobArena;
import mobarena.commands.suggestions.NameSuggestionProvider;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

public class MobSpawnCommands implements Command {

    private int addMobSpawn(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        MobArena.database.addMobSpawnPoint(name, player.getX(), player.getY(), player.getZ());
        player.sendMessage(new TranslatableText("mobarena.addedmobspawnpoint"), false);
        ArenaManager.loadInactiveArena(name);
        return 1;
    }

    private int showMobSpawn(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        var source = context.getSource();
        var player = source.getPlayer();
        var world = player.getWorld();

        var points = MobArena.database.getMobSpawnPoints(name);
        for (var point: points) {
            System.out.println(point);
            world.spawnParticles(ParticleTypes.LARGE_SMOKE, point.getX(), point.getY(), point.getZ(), 1 ,0, 0, 0, 0);
        }

        player.sendMessage(new TranslatableText("mobarena.showingpoints"), false);
        ArenaManager.loadInactiveArena(name);
        return 1;
    }

    private int removeMobSpawn(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        MobArena.database.removeMobSpawnPoint(name, player.getX(), player.getY(), player.getZ());
        player.sendMessage(new TranslatableText("mobarena.removedmobspawnpoint"), false);
        ArenaManager.loadInactiveArena(name);
        return 1;
    }

    @Override
    public LiteralCommandNode<ServerCommandSource> getNode() {
        return CommandManager
                .literal("mobspawn")
                .then(CommandManager.literal("add").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(new NameSuggestionProvider())
                .executes(this::addMobSpawn)))

                .then(CommandManager.literal("show").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(new NameSuggestionProvider())
                .executes(this::showMobSpawn)))

                .then(CommandManager.literal("remove").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(new NameSuggestionProvider())
                .executes(this::removeMobSpawn)))

                .build();
    }
}
