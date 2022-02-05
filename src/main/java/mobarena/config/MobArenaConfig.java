package mobarena.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MobArenaConfig {

    public static final Logger LOGGER = LogManager.getLogger("MobArena");
    ObjectMapper mapper = new ObjectMapper();
    public String json;

    List<ArenaDataTemplate> arenaList = new ArrayList<ArenaDataTemplate>();

//    public void addDefaultGlobalSettingsList(){
//        String prefix  = "&a[MobArena] ";
//
//        globalSettingsList.add("enabled", new JsonPrimitive(true));
//        globalSettingsList.add("prefix", new JsonPrimitive(prefix));
//    }


    public void createJson() throws IOException {
        File file = new File(FabricLoader.getInstance().getConfigDir().toString()+"/mobarena.json");
        mapper.writeValue(file, arenaList);

        json = mapper.writeValueAsString(arenaList);
    }

    public void addArenaToList(String name){
        ArenaDataTemplate arena = new ArenaDataTemplate();
        arena.name = name;
        arenaList.add(arena);
    }




}