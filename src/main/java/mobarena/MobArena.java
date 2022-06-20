package mobarena;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mobarena.commands.*;
import mobarena.database.Database;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MobArena implements ModInitializer {
    public static final String MOD_ID = "mobarena";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static Database database = new Database();

	public static ArenaManager arenaManager = new ArenaManager();

	public static void log(Level level, String message) {
		final String logPrefix = "[MobArena]: ";
		LOGGER.log(level, logPrefix.concat(message));
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Initialised MobArena Mod for Minecraft v1.18.2");

		database.connectToDB();

		CommandRegistrationCallback.EVENT.register(MobArena::registerCommands);

	}

	private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
		LiteralCommandNode<ServerCommandSource> mobarena = CommandManager
				.literal("mobarena")
				.build();

		LiteralCommandNode<ServerCommandSource> alias = CommandManager
				.literal("ma")
				.build();

		dispatcher.getRoot().addChild(mobarena);
		dispatcher.getRoot().addChild(alias);

		Command[] commands = new Command[] {
				new CreateArena(),
				new JoinArena(),
				new SetLobby(),
				new SetArena(),
				new LeaveArena(),
				new SetExit(),
				new SetSpec(),
				new Ready(),
				new SetP1(),
				new SetP2(),
				new SetWorld(),
				new AddMobSpawn()
		};

		for (Command command : commands) {
			mobarena.addChild(command.getNode());
			alias.addChild(command.getNode());
		}
	}
}
