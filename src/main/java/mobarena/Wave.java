package mobarena;

public class Wave {
    private int currentWave = 0;
    private int finalWave = 10;
    private int mobsToSpawn;

    private WaveType waveType;

    public void startWave() {
        nextWave();
        mobsToSpawn = calculateMobsToSpawn();
    }

    private int calculateMobsToSpawn() {
        if (waveType == WaveType.SWARM) {
            return (currentWave + 10);
        } else {
            return (currentWave + 3);
        }
    }

    private void nextWave() {
        currentWave++;
    }

    public void setWaveType(WaveType waveType) {
        this.waveType = waveType;
    }

    public int getMobsToSpawn() {
        return mobsToSpawn;
    }

    public boolean isFinalWave() {
        return finalWave == currentWave;
    }
}
