package mobarena.config;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class MobArenaConfig {

    public static final Logger LOGGER = LogManager.getLogger("MobArena");
    public Gson gson = new GsonBuilder().setPrettyPrinting().create();

    //config sections
    @SerializedName("Global-settings")
    public JsonObject globalSettingsList = new JsonObject();

    @SerializedName("Arenas")
    public JsonObject arenaList = new JsonObject();

    @SerializedName("Classes")
    public JsonObject classes = new JsonObject();


    public void addDefaultGlobalSettingsList(){
        String prefix  = "&a[MobArena] ";

        globalSettingsList.add("enabled", new JsonPrimitive(true));
        globalSettingsList.add("prefix", new JsonPrimitive(prefix));
    }

    public void createArenaTemplate(String name, String worldName) {

        JsonObject arena = new JsonObject();
        arena.add("worldName", new JsonPrimitive(worldName));
        arena.add("enabled", new JsonPrimitive(true));

        JsonArray arenaWarp = new JsonArray();
        JsonArray exitWarp = new JsonArray();
        JsonArray specWarp = new JsonArray();
        JsonArray lobbyWarp = new JsonArray();

        JsonArray arenaWarpYawPitch = new JsonArray();
        JsonArray lobbyWarpYawPitch = new JsonArray();
        JsonArray exitWarpYawPitch = new JsonArray();
        JsonArray specWarpYawPitch = new JsonArray();

        JsonArray p1 = new JsonArray();
        JsonArray p2 = new JsonArray();
        JsonArray l1 = new JsonArray();
        JsonArray l2 = new JsonArray();



        arena.add("arenaWarp", arenaWarp);
        arena.add("lobbyWarp", lobbyWarp);
        arena.add("exitWarp", exitWarp);
        arena.add("specWarp", specWarp);

        arena.add("arenaYawPitch", arenaWarpYawPitch);
        arena.add("lobbyYawPitch", lobbyWarpYawPitch);
        arena.add("exitYawPitch", exitWarpYawPitch);
        arena.add("specYawPitch", specWarpYawPitch);

        arena.add("p1", p1);
        arena.add("p2", p2);
        arena.add("l1", l1);
        arena.add("l2", l2);

        arenaList.add(name, arena);
        gson.toJsonTree(arenaList);
    }

    public JsonObject ArenaDataTemplate(String name){
        return arenaList.getAsJsonObject(name);
    }

}