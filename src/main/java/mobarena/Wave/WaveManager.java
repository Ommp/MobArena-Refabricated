package mobarena.Wave;

import java.util.Random;

public class WaveManager {

    private int currentWave = 0;
    private int finalWave;

    private Wave wave = new Wave();

    public void incrementWave() {
        currentWave++;
    }

    public void setWaveType(WaveType waveType) {
        wave.type = waveType;
    }

    public void setRandomWaveType() {
        Random random = new Random();
        int number = random.nextInt(100 - 1 + 1) + 1;
        if (number <= 60) {
            wave.type = WaveType.DEFAULT;
        } else if (number <= 85) {
            wave.type = WaveType.SWARM;
        } else {
            wave.type = WaveType.BOSS;
        }
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public WaveType getWaveType() {
        return wave.type;
    }

    public boolean isFinalWave() {
        return finalWave == currentWave;
    }

    public int getMobAmount(int arenaPlayerCount) {
        return wave.calculateMobs(currentWave, arenaPlayerCount, wave.type);
    }

    public void setWave(Wave wave) {
        this.wave = wave;
    }
}
