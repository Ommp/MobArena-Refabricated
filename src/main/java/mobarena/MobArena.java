package mobarena;

import mobarena.database.Database;
import net.fabricmc.api.ModInitializer;
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
//		database.addArena("FireArena", 1, 10);
//		database.updateP1("FireArena", 5, 5, 5);
		System.out.println(database.getArenaByName("FireArena").get(0));
	}
}
