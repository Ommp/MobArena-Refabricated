package mobarena;

import mobarena.config.MobArenaConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class MobArena implements ModInitializer {

	public static MobArenaConfig config = new MobArenaConfig();
	public static ArenaLoader arenaLoader = new ArenaLoader();

    public static final String MOD_ID = "mobarena";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static void log(Level level, String message) {
		final String logPrefix = "[MobArena]: ";
		LOGGER.log(level, logPrefix.concat(message));
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Initialised MobArena Mod for Minecraft v1.16");

		config.loadFile();
		try {
			config.readGlobalConfigJson();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			config.readArenasJson();
		} catch (IOException e) {
			e.printStackTrace();
		}
		config.addArenaToList("default");
		config.addArenaToList("default2");
		config.addArenaToList("default3");
		try {
			config.saveArenaJson();
		} catch (IOException e) {
			e.printStackTrace();
		}
		arenaLoader.loadAllArenas();

		CommandRegistrationCallback.EVENT.register(MobArenaCommandRegistry::register);

	}
}
