package mobarena.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mobarena.ArenaClass;
import mobarena.ArenaItem;
import mobarena.ArenaManager;
import mobarena.MobArena;
import mobarena.commands.suggestions.NameSuggestionProvider;
import mobarena.commands.suggestions.forceClassSuggestionProvider;
import mobarena.config.ArenaClassConfig;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Objects;

public class SetCommand implements Command {

    private int setMinPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int number = IntegerArgumentType.getInteger(context, "number");
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        MobArena.database.setMinPlayers(number, name);
        player.sendMessage(Text.translatable("mobarena.updatedmin"), false);
        ArenaManager.loadInactiveArena(name);
        return 1;
    }
    private int setMaxPlayers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int number = IntegerArgumentType.getInteger(context, "number");
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        MobArena.database.setMaxPlayers(number, name);
        player.sendMessage(Text.translatable("mobarena.updatedmax"), false);
        ArenaManager.loadInactiveArena(name);
        return 1;
    }

    private int setLobby(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        MobArena.database.updateLobbyWarp(name, player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
        player.sendMessage(Text.translatable("mobarena.updatedwarp"), false);
        ArenaManager.loadInactiveArena(name);
        return 1;
    }
    private int setExit(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        MobArena.database.updateExitWarp(name, player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
        player.sendMessage(Text.translatable("mobarena.updatedwarp"), false);
        ArenaManager.loadInactiveArena(name);
        return 1;
    }

    private int setArena(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        MobArena.database.updateArenaWarp(name, player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
        player.sendMessage(Text.translatable("mobarena.updatedwarp"), false);
        ArenaManager.loadInactiveArena(name);
        return 1;
    }

    private int setSpec(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        MobArena.database.updateSpecWarp(name, player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
        player.sendMessage(Text.translatable("mobarena.updatedwarp"), false);
        ArenaManager.loadInactiveArena(name);
        return 1;
    }

    private int setP1(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        MobArena.database.updateP1(name, player.getBlockX(), player.getBlockY(), player.getBlockZ());
        player.sendMessage(Text.translatable("mobarena.updatedpos"), false);
        ArenaManager.loadInactiveArena(name);
        return 1;
    }

    private int setP2(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        MobArena.database.updateP2(name, player.getBlockX(), player.getBlockY(), player.getBlockZ());
        player.sendMessage(Text.translatable("mobarena.updatedpos"), false);
        ArenaManager.loadInactiveArena(name);
        return 1;
    }

    private int setClass(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();


        PlayerInventory inventory = player.getInventory();
        ArenaClass arenaClass = new ArenaClass();
        arenaClass.setName(name);
        for (int i = 0; i < inventory.size(); i++) {
            if (!Objects.equals(Registries.ITEM.getId(inventory.getStack(i).getItem()).toString(), "minecraft:air")) {

                var stackEncoded = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, inventory.getStack(i)).get().orThrow().toString();
                arenaClass.addItems(new ArenaItem(stackEncoded, i));
            }
        }
        ArenaClassConfig config = new ArenaClassConfig();
        config.load();
        config.addClass(arenaClass);
        config.save();
        ArenaManager.initClasses();

        player.sendMessage(Text.translatable("mobarena.setclass", name), false);

        return 1;
    }

    private int setWorld(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        MobArena.database.updateWorld(player.getWorld().getRegistryKey().getValue().toString(), name);
        player.sendMessage(Text.translatable("mobarena.updatedworld"), false);
        ArenaManager.loadInactiveArena(name);
        return 1;
    }

    private int setCountdown(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int num = IntegerArgumentType.getInteger(context, "number");
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        MobArena.database.setCountdown(num, name);
        player.sendMessage(Text.translatable("mobarena.updatedcountdown", num), false);
        ArenaManager.loadInactiveArena(name);
        return 1;
    }

    private int setWaveCountdown(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int num = IntegerArgumentType.getInteger(context, "number");
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        MobArena.database.setWaveCountdown(num, name);
        player.sendMessage(Text.translatable("mobarena.updatedwavecountdown", num), false);
        ArenaManager.loadInactiveArena(name);
        return 1;
    }

    private int setForceClass(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        var number = IntegerArgumentType.getInteger(context, "number");
        var name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (number < 1 ) {
            MobArena.database.setForceClass(false, name);
            player.sendMessage(Text.translatable("mobarena.updatedforceclass", "false", name), false);
            ArenaManager.loadInactiveArena(name);
        } else {
            MobArena.database.setForceClass(true, name);
            player.sendMessage(Text.translatable("mobarena.updatedforceclass", "true", name), false);
            ArenaManager.loadInactiveArena(name);
        }

        return 1;
    }

    private int setIsXPAllowed(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        var number = IntegerArgumentType.getInteger(context, "number");
        var name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (number < 1 ) {
            MobArena.database.setAllowXP(false, name);
            player.sendMessage(Text.translatable("mobarena.updatedallowxp", "false", name), false);
            ArenaManager.loadInactiveArena(name);
        } else {
            MobArena.database.setAllowXP(true, name);
            player.sendMessage(Text.translatable("mobarena.updatedallowxp", "true", name), false);
            ArenaManager.loadInactiveArena(name);
        }

        return 1;
    }

    private int setIsProtected(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        var number = IntegerArgumentType.getInteger(context, "number");
        var name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        if (number < 1 ) {
            MobArena.database.setIsProtected(false, name);
            player.sendMessage(Text.translatable("mobarena.updatedisprotected", "false", name), false);
            ArenaManager.loadInactiveArena(name);
        } else {
            MobArena.database.setIsProtected(true, name);
            player.sendMessage(Text.translatable("mobarena.updatedisprotected", "true", name), false);
            ArenaManager.loadInactiveArena(name);
        }

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

                .then(CommandManager.literal("lobby").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(new NameSuggestionProvider())
                .executes(this::setLobby)))

                .then(CommandManager.literal("arena").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(new NameSuggestionProvider())
                .executes(this::setArena)))

                .then(CommandManager.literal("exit").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(new NameSuggestionProvider())
                .executes(this::setExit)))

                .then(CommandManager.literal("spectator").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(new NameSuggestionProvider())
                .executes(this::setSpec)))

                .then(CommandManager.literal("p1").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(new NameSuggestionProvider())
                .executes(this::setP1)))

                .then(CommandManager.literal("p2").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(new NameSuggestionProvider())
                .executes(this::setP2)))

                .then(CommandManager.literal("class").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("name", StringArgumentType.greedyString())
                .executes(this::setClass)))

                .then(CommandManager.literal("world").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(new NameSuggestionProvider())
                .executes(this::setWorld)))

                .then(CommandManager.literal("countdown").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("number", IntegerArgumentType.integer())
                .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(new NameSuggestionProvider())
                .executes(this::setCountdown))))

                .then(CommandManager.literal("waveCountdown").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("number", IntegerArgumentType.integer())
                .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(new NameSuggestionProvider())
                .executes(this::setWaveCountdown))))

                .then(CommandManager.literal("forceclass").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("number", IntegerArgumentType.integer()).suggests(new forceClassSuggestionProvider())
                .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(new NameSuggestionProvider())
                .executes(this::setForceClass))))

                .then(CommandManager.literal("xpEnabled").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("number", IntegerArgumentType.integer()).suggests(new forceClassSuggestionProvider())
                .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(new NameSuggestionProvider())
                .executes(this::setIsXPAllowed))))

                .then(CommandManager.literal("isProtected").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("number", IntegerArgumentType.integer()).suggests(new forceClassSuggestionProvider())
                .then(CommandManager.argument("name", StringArgumentType.greedyString()).suggests(new NameSuggestionProvider())
                .executes(this::setIsProtected))))

                .build();
    }
}
