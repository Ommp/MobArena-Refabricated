package mobarena.config;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;

public class MobArenaConfig {

    public static final Logger LOGGER = LogManager.getLogger("MobArena");
    public Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public String json;

//    //config sections
//    @SerializedName("Global-settings")
//    public JsonObject globalSettingsList = new JsonObject();
//
//    @SerializedName("Arenas")
//    public JsonObject arenaList = new JsonObject();
//
//    @SerializedName("Classes")
//    public JsonObject classes = new JsonObject();


//    public void addDefaultGlobalSettingsList(){
//        String prefix  = "&a[MobArena] ";
//
//        globalSettingsList.add("enabled", new JsonPrimitive(true));
//        globalSettingsList.add("prefix", new JsonPrimitive(prefix));
//    }





    public ArrayList<ArenaDataTemplate> arenaList = new ArrayList<>();

    public void addArenaToList(String name){
        ArenaDataTemplate arena = new ArenaDataTemplate();
        arena.name = name;
        arenaList.add(arena);
    }



}