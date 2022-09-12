package mobarena.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mobarena.ArenaClass;
import mobarena.ArenaItem;
import mobarena.MobArena;
import mobarena.config.ArenaClassConfig;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
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
                    HashMap<String, Integer> enchantments = new HashMap<>();

                //for every enchantment an item has, add the enchantment to the arraylist
                for (int j = 0; j < inventory.getStack(i).getEnchantments().size(); j++) {

                    String enchantmentName = inventory.getStack(i).getEnchantments().getCompound(j).getString("id");
                    int level = inventory.getStack(i).getEnchantments().getCompound(j).getInt("lvl");
                    enchantments.put(enchantmentName, level);
                }

                if (enchantments.isEmpty()) {
                    arenaClass.addItems(new ArenaItem(Registry.ITEM.getId(inventory.getStack(i).getItem()).toString(), inventory.getStack(i).getCount(), i));
                } else {
                    arenaClass.addItems(new ArenaItem(Registry.ITEM.getId(inventory.getStack(i).getItem()).toString(), inventory.getStack(i).getCount(), i, enchantments));
                }
            }
        }

        ArenaClassConfig config = new ArenaClassConfig();
        config.load();
        config.addClass(arenaClass);
        config.save();
        MobArena.arenaManager.initClasses();

        player.sendMessage(new TranslatableText("mobarena.setclass", name), false);

        return 1;
    }

    public LiteralCommandNode<ServerCommandSource> getNode() {
        return CommandManager
                .literal("setclass")
                .then(
                        CommandManager.argument("name", StringArgumentType.greedyString()).executes(this::run)
                )
                .build();
    }
}
