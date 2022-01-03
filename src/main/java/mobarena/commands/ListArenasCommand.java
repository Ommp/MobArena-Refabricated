package mobarena.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import mobarena.database.Arena;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;

public class ListArenasCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("malistarenas")
                        .executes(context -> {
                            ArrayList<Arena> Arenas = Arena.all();
                            context.getSource().sendFeedback(new LiteralText("Arena list: " ), true);

                            for (int i = 0; i < Arenas.size(); i++) {
                                context.getSource().sendFeedback(new LiteralText(Arenas.get(i).name), true);
                            }

                            return 1;
                        }));
    }

}
