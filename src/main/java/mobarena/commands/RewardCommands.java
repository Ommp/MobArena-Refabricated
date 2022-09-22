package mobarena.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mobarena.MobArena;
import mobarena.commands.suggestions.NameSuggestionProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

public class RewardCommands implements Command {

    private int addReward(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int wave = IntegerArgumentType.getInteger(context, "wave");
        String name = StringArgumentType.getString(context, "arena");

        var player = context.getSource().getPlayer();
        var inventory = player.getInventory();

        var nbt = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, inventory.getMainHandStack()).get().orThrow().toString();

        MobArena.database.createReward(nbt, wave, name);
        player.sendMessage(new TranslatableText("mobarena.addedreward", wave), false);
        return 1;
    }

    private int deleteWaveRewards(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int wave = IntegerArgumentType.getInteger(context, "wave");
        String name = StringArgumentType.getString(context, "arena");

        var player = context.getSource().getPlayer();

        MobArena.database.deleteWaveRewards(wave, name);
        player.sendMessage(new TranslatableText("mobarena.deletedwaverewards", wave), false);
        return 1;
    }


    @Override
    public LiteralCommandNode<ServerCommandSource> getNode() {
        return CommandManager
                .literal("rewards")
                .then(CommandManager.literal("add").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("wave", IntegerArgumentType.integer())
                .then(CommandManager.argument("arena", StringArgumentType.greedyString()).suggests(new NameSuggestionProvider())
                .executes(this::addReward))))

                .then(CommandManager.literal("wave").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("delete")
                .then(CommandManager.argument("wave", IntegerArgumentType.integer())
                .then(CommandManager.argument("arena", StringArgumentType.greedyString()).suggests(new NameSuggestionProvider())
                .executes(this::deleteWaveRewards)))))

                .build();
    }
}
