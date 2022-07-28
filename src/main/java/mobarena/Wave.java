package mobarena;

import java.util.Random;

public class Wave {
    private int currentWave = 0;
    private int finalWave = 10;
    private int mobsToSpawn;

    private WaveType waveType;

    public void startWave() {
        nextWave();
        calculateMobsToSpawn();
    }

    private void calculateMobsToSpawn() {
        if (waveType == WaveType.SWARM) {
            mobsToSpawn = currentWave + 10;
        } else if (waveType == WaveType.DEFAULT) {
            mobsToSpawn =  currentWave + 3;
        } else if (waveType == WaveType.BOSS) {
            mobsToSpawn = 1;
        }
    }

    private void nextWave() {
        currentWave++;
    }

    public void setWaveType(WaveType waveType) {
        this.waveType = waveType;
    }
    public void setRandomWaveType() {
        Random random = new Random();
        int number = random.nextInt(100 - 1 + 1) + 1;
        if (number <= 60) {
            this.waveType = WaveType.DEFAULT;
        } else if (number <= 85) {
            this.waveType = WaveType.SWARM;
        } else {
            this.waveType = WaveType.BOSS;
        }
    }

    public int getMobsToSpawn() {
        return mobsToSpawn;
    }

    public boolean isFinalWave() {
        return finalWave == currentWave;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public WaveType getWaveType() {
        return waveType;
    }
}
