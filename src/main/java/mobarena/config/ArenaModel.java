package mobarena.config;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ArenaModel {

    @SerializedName("Use custom spawns")
    private boolean useCustomSpawns = false;

    @SerializedName("Monsters")
    private final ArrayList<String> monsters = new ArrayList<>();

    private final ArrayList<SingleWave> singleWaves = new ArrayList<>();
    private final ArrayList<RecurrentWave> recurrentWaves = new ArrayList<>();

    private final ArrayList<Reinforcement> reinforcements = new ArrayList<>();

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

    public void setUseCustomSpawns(boolean useCustomSpawns) {
        this.useCustomSpawns = useCustomSpawns;
    }

    public ArrayList<Reinforcement> getReinforcements() {
        return reinforcements;
    }

    public void createDefaultReinforcements() {
        //create default reinforcements for wave 5
        var reinforcement = new Reinforcement(5, true);
        ArrayList<String> items = new ArrayList<>();
        items.add("{Count:1,id:\"minecraft:golden_apple\"}");
        items.add("{Count:4,id:\"minecraft:apple\"}");
        reinforcement.getClassItems().put("all", items);
        reinforcements.add(reinforcement);
    }
}
