package mobarena.commands.setup;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import mobarena.database.Arena;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class DeleteArenaCommand {


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("deletearena")
                                    .then(CommandManager.argument("arenaname", StringArgumentType.word())
                                    .executes(context -> {
                                        String arenaName = StringArgumentType.getString(context, "arenaname");

                                        Arena arena = Arena.get(arenaName);
                                        arena.deleteArena();

                                        context.getSource().sendFeedback(new LiteralText("Deleted arena: " + arenaName), true);
                                        return 1;
                                    })));
    }
}
