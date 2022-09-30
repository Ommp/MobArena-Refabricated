package mobarena.config;

import mobarena.Wave.WaveType;

import java.util.HashMap;

public class SingleWave {

    private int wave;
    private WaveType type;
    private boolean fixed = false;

    //monster names and the number of them
    private final HashMap<String, Integer> monsters = new HashMap<>();

}
