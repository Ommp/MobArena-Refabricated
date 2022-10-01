package mobarena.Wave;

public class Wave {

    public int wave;
    public int frequency;
    public int priority;

    WaveType type;

    int calculateMobs(int wave, int playerCount, WaveType type) {
        int num = 1;
        if (type.equals(WaveType.DEFAULT)) {
            num = wave + (3*playerCount);
        }
        else if(type.equals(WaveType.SWARM)) {
            num = wave + (playerCount*6);
        }
        return num;
    }
}
