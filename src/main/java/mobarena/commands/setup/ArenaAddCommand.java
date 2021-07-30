package mobarena.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

public class ArenaAddCommand implements Command<ServerCommandSource> {

    public ArenaAddCommand() {}
    
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity senderPlayer = source.getPlayer();

        //store Arena name
        String arenaName = StringArgumentType.getString(context, "arena_name");
        System.out.println("this is just a test to see if the command works!");
        // try {
            
        // } catch {

        // }
            return 1;
    }
}

