package mobarena.config;

import com.google.gson.Gson;
import mobarena.Arena;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class MobArenaConfig {

    public Gson gson = new Gson();
    public String json;

    public Reader reader;

    File arenasConfigFile = new File(FabricLoader.getInstance().getConfigDir().toString()+"/mobarena/arenas.json");
    File globalConfigFile = new File(FabricLoader.getInstance().getConfigDir().toString()+"/mobarena/mobarena.json");

    public HashMap<String, Arena> arenas = new HashMap<>();
    public GlobalConfig globalConfig = new GlobalConfig();


    public void loadFile() {
        readArenasJson();
        readGlobalConfigJson();
    }

    public void readArenasJson() {
        if (arenasConfigFile.exists()) {
            try {
                reader = Files.newBufferedReader(Paths.get(FabricLoader.getInstance().getConfigDir().toString() + "/mobarena/arenas.json"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//figure out a way to NOT create an empty arena file and see if that stops the gson.fromjson to cause an error (presumably because of an empty arenas list in the file.
        // if arena file exists, then map arenas to gson.fromjson, if not, log to console that file does not exist (and no arenas).

//        arenas = gson.fromJson(reader, new TypeToken<HashMap<String, Arena>>(){}.getType());

        arenas = gson.fromJson(reader, HashMap.class);
    }

    public void saveArenaJson() {
        try {
            FileWriter fileWriter = new FileWriter(FabricLoader.getInstance().getConfigDir().toString()+"/mobarena/arenas.json");
            json = gson.toJson(arenas);
            fileWriter.write(json);
            fileWriter.close();

            gson.toJson(arenas, fileWriter);

//            arenas = gson.fromJson(new FileReader(FabricLoader.getInstance().getConfigDir().toString()+"/mobarena/arenas.json"), HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readGlobalConfigJson() {
        if (!globalConfigFile.exists()) {

            saveGlobalJson();
        }

        try {
            globalConfig = gson.fromJson(new FileReader(FabricLoader.getInstance().getConfigDir().toString()+"/mobarena/mobarena.json"), GlobalConfig.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void saveGlobalJson() {
        try {
            FileWriter fileWriter = new FileWriter(FabricLoader.getInstance().getConfigDir().toString()+"/mobarena/mobarena.json");
            String json = gson.toJson(globalConfig);
            fileWriter.write(json);
            fileWriter.close();

//            gson.toJson(globalConfig, new FileWriter(FabricLoader.getInstance().getConfigDir().toString()+"/mobarena/mobarena.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
