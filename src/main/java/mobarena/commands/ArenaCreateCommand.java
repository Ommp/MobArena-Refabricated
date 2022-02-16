package mobarena.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mobarena.Arena;
import mobarena.MobArena;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.io.IOException;

public class ArenaCreateCommand implements Command<ServerCommandSource> {

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {

        String arenaName = StringArgumentType.getString(context, "arena_name");

        Arena arena = new Arena(arenaName, context.getSource().getWorld());
        MobArena.config.arenas.arenaList.put(arena.name, arena);
        try {
            MobArena.config.saveArenaJson();
        } catch (IOException e) {
            e.printStackTrace();
        }

        context.getSource().sendFeedback(new TranslatableText("mobarena.modinfo"), false);

        return 0;
    }
}
