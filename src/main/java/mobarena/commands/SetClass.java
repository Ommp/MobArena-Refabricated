package mobarena.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mobarena.ArenaClass;
import mobarena.ArenaItem;
import mobarena.ArenaManager;
import mobarena.config.ArenaClassConfig;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

public class SetClass implements Command {

    private int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();


        PlayerInventory inventory = player.getInventory();
        ArenaClass arenaClass = new ArenaClass();
        arenaClass.setName(name);
        for (int i = 0; i < inventory.size(); i++) {
            if (!Objects.equals(Registry.ITEM.getId(inventory.getStack(i).getItem()).toString(), "minecraft:air")) {

                var stackEncoded = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, inventory.getStack(i)).get().orThrow().toString();
                arenaClass.addItems(new ArenaItem(stackEncoded, i));
            }
        }
        ArenaClassConfig config = new ArenaClassConfig();
        config.load();
        config.addClass(arenaClass);
        config.save();
        ArenaManager.initClasses();

        player.sendMessage(new TranslatableText("mobarena.setclass", name), false);

        return 1;
    }

    public LiteralCommandNode<ServerCommandSource> getNode() {
        return CommandManager
                .literal("setclass")
                .requires(source -> source.hasPermissionLevel(2))
                .then(
                        CommandManager.argument("name", StringArgumentType.greedyString()).executes(this::run)
                )
                .build();
    }
}
