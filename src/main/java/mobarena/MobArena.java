package mobarena;

import mobarena.config.MobArenaConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MobArena implements ModInitializer {

	public static final MobArenaConfig config = new MobArenaConfig();
	private ArenaManager arenaManager = ArenaManager.getInstance();


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
		String name = "IceArena";
		arenaManager.addArenaToMap(name);


		CommandRegistrationCallback.EVENT.register(MobArenaCommandRegistry::register);
	}
}
