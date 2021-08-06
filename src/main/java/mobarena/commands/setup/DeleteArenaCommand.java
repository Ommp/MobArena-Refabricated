package mobarena.commands.setup;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class DeleteArenaCommand {


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("deletearena")
                                    .then(CommandManager.argument("name", StringArgumentType.word()))
                                    .executes(context -> {
                                        System.out.println("Deleted arena!");

                                        return 1;
                                    }));
    }
}
