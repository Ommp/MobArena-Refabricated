package mobarena.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mobarena.MobArena;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

public class AddMobSpawn implements Command{

    private int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");

        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        MobArena.database.addMobSpawnPoint(name, player.getX(), player.getY(), player.getZ());
        player.sendMessage(new TranslatableText("mobarena.addedmobspawnpoint"), false);
        MobArena.arenaManager.reloadArena(name);
        return 1;
    }

    @Override
    public LiteralCommandNode<ServerCommandSource> getNode() {
        return CommandManager
                .literal("addmobspawn")
                .then(
                        CommandManager.argument("name", StringArgumentType.greedyString()).executes(this::run)
                )
                .build();
    }
}