package mobarena.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import mobarena.Arena;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


//@JsonIgnoreProperties({"arenas","classes","globalConfig"})
public class MobArenaConfig {

    public static final Logger LOGGER = LogManager.getLogger("MobArena");
    ObjectMapper mapper = new ObjectMapper();

    public File arenasConfigFile;
    public File classConfigFile;
    public File globalConfigFile;


    public ArenaListData arenas = new ArenaListData();
//    public ClassData classes = new ClassData();
    public GlobalConfig globalConfig = new GlobalConfig();

    public void loadFile() {
        arenasConfigFile = new File(FabricLoader.getInstance().getConfigDir().toString()+"/mobarena/arenas.json");
        classConfigFile = new File(FabricLoader.getInstance().getConfigDir().toString()+"/mobarena/classes.json");
        globalConfigFile = new File(FabricLoader.getInstance().getConfigDir().toString()+"/mobarena/mobarena.json");
    }

    public void saveArenaJson() throws IOException {
        mapper.writeValue(arenasConfigFile, arenas);
    }

//    public void saveClassesJson() throws IOException {
//        mapper.writeValue(classConfigFile, classes);
//    }

    public void saveGlobalJson() throws IOException {
        mapper.writeValue(globalConfigFile, globalConfig);
    }

    public void readArenasJson() throws IOException {
        if (!arenasConfigFile.exists()) {
            saveArenaJson();
        }

        arenas = mapper.readValue(arenasConfigFile, ArenaListData.class);
    }

//    public void readClassesJson() throws IOException {
//        classes = mapper.readValue(classConfigFile, ClassData.class);
//    }

    public void readGlobalConfigJson() throws IOException {
        if (!globalConfigFile.exists()) {
            saveGlobalJson();
        }

        globalConfig = mapper.readValue(globalConfigFile, GlobalConfig.class);
    }

    public void addArenaToList(String name){
         Arena arena = new Arena(name);
        arenas.arenaList.put(name, arena);
    }
}