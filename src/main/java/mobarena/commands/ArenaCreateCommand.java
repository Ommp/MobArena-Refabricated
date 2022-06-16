package mobarena.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mobarena.Arena;
import mobarena.MobArena;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

public class ArenaCreateCommand implements Command<ServerCommandSource> {

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        String arenaName = StringArgumentType.getString(context, "arena_name");


        Arena arena = new Arena(arenaName, context.getSource().getWorld());
        MobArena.config.arenas.put(arena.name, arena);
        MobArena.config.saveArenaJson();

        context.getSource().sendFeedback(new TranslatableText("mobarena.createdarena", arenaName), false);

        return 0;
    }
}
