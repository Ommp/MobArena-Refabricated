package mobarena;

import mobarena.items.GuiItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MobArena implements ModInitializer {

	private ArenaMaster arenaMaster;

	private void setupArenaMaster() {
		ArenaMaster arenaMaster = new ArenaMaster();
	}

	private void setup() {
		setupArenaMaster();
	}

	public static void setupArenaObject(String name){
		Arena arena = new Arena(name);
		Location lobbyWarp = new Location(new BlockPos(10,50,10),0,0);
		arena.setLobbyWarp(lobbyWarp);

	}



	public static Item GUI_ITEM = new GuiItem(new Item.Settings().group(ItemGroup.MISC));

    public static final String MOD_ID = "mobarena";
    public static final Logger LOGGER = LogManager.getLogger("mobarena");


	public static void log(Level level, String message) {
		final String logPrefix = "[MobArena]: ";
		LOGGER.log(level, logPrefix.concat(message));
	}


	@Override
	public void onInitialize() {
		LOGGER.info("Initalised MobArena Mod for Minecraft v1.16");

		Registry.register(Registry.ITEM, new Identifier("mobarena", "gui_item"), GUI_ITEM);

	}

//	ServerWorldEvents.Load

	}
