package mobarena.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mobarena.ArenaClass;
import mobarena.ArenaItem;
import mobarena.config.ArenaClassConfig;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class SetClass implements Command {

    private int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();


        PlayerInventory inventory = player.getInventory();
        ArenaClassConfig arenaClassConfig = new ArenaClassConfig();
        ArenaClass arenaClass = new ArenaClass();
        arenaClass.setName(name);
        for (int i = 0; i < inventory.size(); i++) {
            arenaClass.addItems(new ArenaItem(inventory.getStack(i).getItem().toString(), inventory.getStack(i).getCount()));
        }
        arenaClassConfig.addClass(arenaClass);
        arenaClassConfig.save();

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
