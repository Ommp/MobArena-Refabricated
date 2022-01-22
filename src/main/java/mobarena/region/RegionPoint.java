package mobarena.region;

public enum RegionPoint {
    ARENA,
    LOBBY,
    SPECTATOR,
    EXIT;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
