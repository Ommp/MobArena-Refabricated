package mobarena.config;

import mobarena.Wave.WaveType;

import java.util.HashMap;

public class SingleWave {

    private int wave;
    private WaveType type;

    //monster names and the number of them
    private final HashMap<String, Integer> monsters = new HashMap<>();
    private boolean fixed = false;

    public int getWave() {
        return wave;
    }

    public WaveType getType() {
        return type;
    }

    public HashMap<String, Integer> getMonsters() {
        return monsters;
    }

    public boolean isFixed() {
        return fixed;
    }
}
