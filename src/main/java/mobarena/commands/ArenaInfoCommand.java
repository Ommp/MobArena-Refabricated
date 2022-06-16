package mobarena.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mobarena.MobArena;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;



public class ArenaInfoCommand implements Command<ServerCommandSource> {

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        String arenaName = StringArgumentType.getString(context, "arena_name");

        String worldName = MobArena.config.arenas.get(arenaName).worldName;

        //TODO fix error
        context.getSource().sendFeedback(new TranslatableText("mobarena.arenainfo", arenaName, worldName), false);

        return 1;
    }
}
