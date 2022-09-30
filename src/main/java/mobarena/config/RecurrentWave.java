package mobarena.config;

import mobarena.Wave.WaveType;

import java.util.HashMap;

public class RecurrentWave {

    private int wave;
    private int frequency;
    private int priority;

    private WaveType type;

    private final HashMap<String, Integer> monsters = new HashMap<>();

}
