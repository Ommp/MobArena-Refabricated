package mobarena.config;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArenaModel {

    @SerializedName("Use custom spawns")
    private boolean useCustomSpawns = false;

    @SerializedName("Monsters")
    private ArrayList<String> monsters = new ArrayList<>();

    private ArrayList<SingleWave> singleWaves;
    private ArrayList<RecurrentWave> recurrentWaves;

    private int finalWave;

    public boolean usesCustomSpawns() {
        return useCustomSpawns;
    }

    public ArrayList<String> getMonsters() {
        return monsters;
    }

    public ArrayList<SingleWave> getSingleWaves() {
        return singleWaves;
    }

    public ArrayList<RecurrentWave> getRecurrentWaves() {
        return recurrentWaves;
    }

    public int getFinalWave() {
        return finalWave;
    }
}
