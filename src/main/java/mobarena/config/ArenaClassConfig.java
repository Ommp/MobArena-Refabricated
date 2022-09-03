package mobarena.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import mobarena.ArenaClass;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.util.ArrayList;

public class ArenaClassConfig {

    private transient final File configFile = new File(FabricLoader.getInstance().getConfigDir().toString()+ "/mobarena/classes.json");
    private transient final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    @SerializedName("Classes")
    private final ArrayList<ArenaClass> arenaClasses = new ArrayList<>();

    public void addClass(ArenaClass arenaClass){
        arenaClasses.add(arenaClass);
    }

    public ArenaClassConfig load() {
        if (!configFile.exists()) {
            save();
        }
        FileReader json;
        try {
            json = new FileReader(configFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return gson.fromJson(json, ArenaClassConfig.class);
    }
    public void save() {
        try {
            FileWriter fileWriter = new FileWriter(configFile);
            gson.toJson(this, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
