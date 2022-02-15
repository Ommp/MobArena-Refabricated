package mobarena.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;



public class ArenaInfoCommand implements Command<ServerCommandSource> {

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        String arenaName = StringArgumentType.getString(context, "arena_name");

        //TODO implement actual info
        context.getSource().sendFeedback(Text.of(arenaName), false);

        return 0;
    }
}
