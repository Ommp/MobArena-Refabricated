package mobarena.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;

public interface Command {
    public LiteralCommandNode<ServerCommandSource> getNode();
}
