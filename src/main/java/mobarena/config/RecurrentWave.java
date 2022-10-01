package mobarena.config;

import mobarena.Wave.WaveType;

import java.util.HashMap;

public class RecurrentWave {

    private int wave;
    private int frequency;
    private int priority;

    private WaveType type;

    private final HashMap<String, Integer> monsters = new HashMap<>();
    private boolean fixed = false;

    public int getWave() {
        return wave;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getPriority() {
        return priority;
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
