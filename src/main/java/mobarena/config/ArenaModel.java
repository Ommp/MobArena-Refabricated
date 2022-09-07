package mobarena.config;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArenaModel {

    @SerializedName("Use custom spawns")
    private boolean useCustomSpawns = false;

    @SerializedName("Monsters")
    private ArrayList<String> monsters = new ArrayList<>();

    public boolean usesCustomSpawns() {
        return useCustomSpawns;
    }

    public ArrayList<String> getMonsters() {
        return monsters;
    }
}
