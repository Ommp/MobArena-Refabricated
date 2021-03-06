package mobarena.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mobarena.MobArena;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;

public class LeaveArena implements Command{
    private int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();


        String name = MobArena.arenaManager.getArenaFromPlayer(player);
        MobArena.arenaManager.arenas.get(name).transportPlayer(player, "exit");
        MobArena.arenaManager.removeActivePlayer(player);

        player.sendMessage(new TranslatableText("mobarena.leftarena"), false);
        return 1;
    }

    public LiteralCommandNode<ServerCommandSource> getNode() {
        return CommandManager
                .literal("leave")
                .executes(this::run)
                .build();
    }
}