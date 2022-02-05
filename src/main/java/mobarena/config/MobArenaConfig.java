package mobarena.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

//@JsonIgnoreProperties({"json","arenasConfig"})
public class MobArenaConfig {

    public static final Logger LOGGER = LogManager.getLogger("MobArena");
    ObjectMapper mapper = new ObjectMapper();


    String json;
    File arenasConfigFile;
    File classConfigFile;
    File globalConfigFile;


    List<ArenaDataTemplate> arenas = new ArrayList<ArenaDataTemplate>();
    ClassData classes = new ClassData();
    GlobalConfig globalConfig = new GlobalConfig();

    public void loadFile() {
        arenasConfigFile = new File(FabricLoader.getInstance().getConfigDir().toString()+"/mobarena/arenas.json");
        classConfigFile = new File(FabricLoader.getInstance().getConfigDir().toString()+"/mobarena/classes.json");
        globalConfigFile = new File(FabricLoader.getInstance().getConfigDir().toString()+"/mobarena/mobarena.json");
    }

    public void createJson() throws IOException {
        mapper.writeValue(arenasConfigFile, arenas);
        mapper.writeValue(classConfigFile, classes);
        mapper.writeValue(globalConfigFile, globalConfig);

        json = mapper.writeValueAsString(arenas);
    }

    public void readJson() throws IOException {
        arenas = mapper.readValue(arenasConfigFile, List.class);
        classes = mapper.readValue(classConfigFile, ClassData.class);
        globalConfig = mapper.readValue(globalConfigFile, GlobalConfig.class);


        LOGGER.info(arenas);
        LOGGER.info(classes);
        LOGGER.info(globalConfig);
    }

    public void addArenaToList(String name){
        ArenaDataTemplate arena = new ArenaDataTemplate();
        arena.name = name;
        arenas.add(arena);
    }




}