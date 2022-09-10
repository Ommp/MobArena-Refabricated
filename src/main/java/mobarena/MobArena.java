package mobarena;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import mobarena.commands.*;
import mobarena.config.ArenaConfig;
import mobarena.database.Database;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class MobArena implements ModInitializer {
    public static final String MOD_ID = "mobarena";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	public static Database database = new Database();

	public static ArenaManager arenaManager = new ArenaManager();

	public static ArenaConfig arenaConfig = new ArenaConfig();
	public static MinecraftServer serverinstance;

	public static void log(Level level, String message) {
		final String logPrefix = "[MobArena]: ";
		LOGGER.log(level, logPrefix.concat(message));
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Initialised MobArena Mod for Minecraft v1.18.2");

		new File(FabricLoader.getInstance().getConfigDir().toString()+ "/mobarena").mkdirs();
		database.connectToDB();

		CommandRegistrationCallback.EVENT.register(MobArena::registerCommands);

		ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
			serverinstance = server.getOverworld().getServer();
		});

		arenaManager.initClasses();
		arenaConfig.load();
		arenaManager.addArenaNames();

		ServerPlayerEvents.ALLOW_DEATH.register(((player, source, amount) -> {
			if (MobArena.arenaManager.isPlayerActive(player)) {
				String name = MobArena.arenaManager.getArenaFromPlayer(player);
				MobArena.arenaManager.arenas.get(name).addDeadPlayer(player);
				return false;
			}
			return true;
		}));
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
				new AddMobSpawn(),
				new SpectateArena(),
				new DeleteArena(),
				new SetClass()
		};

		for (Command command : commands) {
			mobarena.addChild(command.getNode());
			alias.addChild(command.getNode());
		}
	}
}