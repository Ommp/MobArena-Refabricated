package mobarena.region;

import mobarena.Arena;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ArenaRegion {
    private Arena arena;
    private ServerWorld world;

    private BlockPos p1,p2,l1,l2;
    private BlockPos arenaWarp, lobbyWarp, specWarp, exitWarp;

    private Map<String, BlockPos> spawnpoints, containers;

    private boolean setup, lobbySetup;

    public ArenaRegion(Arena arena) {
        this.arena = arena;
    }

    public boolean isWarp(BlockPos l) {
        return (l.equals(arenaWarp) ||
                l.equals(lobbyWarp) ||
                l.equals(specWarp)  ||
                l.equals(exitWarp));
    }

    public BlockPos getExitWarp() {
        return exitWarp;
    }
    public BlockPos getArenaWarp() {
        return arenaWarp;
    }
    public BlockPos getLobbyWarp() {
        return lobbyWarp;
    }

    public BlockPos getSpecWarp() {
        return specWarp;
    }

    public BlockPos getSpawnpoint(String name) {
        return spawnpoints.get(name);
    }
    public Collection<BlockPos> getSpawnpoints() {
        return spawnpoints.values();
    }
    public List<BlockPos> getSpawnpointList() {
        return new ArrayList<>(spawnpoints.values());
    }

    public Collection<BlockPos> getContainers() {
        return containers.values();
    }

    public boolean contains(ServerWorld w, BlockPos l) {
        if (!w.getDimension().equals(world.getDimension()) || !isDefined()) {
            return false;
        }

        int x = l.getX();
        int y = l.getY();
        int z = l.getZ();

        // Check the lobby first.
        if (lobbySetup) {
            if ((x >= l1.getX() && x <= l2.getX()) &&
                    (z >= l1.getZ() && z <= l2.getZ()) &&
                    (y >= l1.getY() && y <= l2.getY()))
                return true;
        }

        // Returns false if the location is outside of the region.
        return ((x >= p1.getX() && x <= p2.getX()) &&
                (z >= p1.getZ() && z <= p2.getZ()) &&
                (y >= p1.getY() && y <= p2.getY()));
    }

    public boolean contains(ServerWorld w, BlockPos l, int radius) {
        if (!w.getDimension().equals(world.getDimension()) || !isDefined()) {
            return false;
        }

        int x = l.getX();
        int y = l.getY();
        int z = l.getZ();

        if (lobbySetup) {
            if ((x + radius >= l1.getX() && x - radius <= l2.getX()) &&
                    (z + radius >= l1.getZ() && z - radius <= l2.getZ()) &&
                    (y + radius >= l1.getY() && y - radius <= l2.getY()))
                return true;
        }

        return ((x + radius >= p1.getX() && x - radius <= p2.getX()) &&
                (z + radius >= p1.getZ() && z - radius <= p2.getZ()) &&
                (y + radius >= p1.getY() && y - radius <= p2.getY()));
    }

    public boolean isDefined() {
        return (p1 != null && p2 != null);
    }

    public void verifyData() {
        setup = (p1 != null &&
                p2 != null &&
                arenaWarp != null &&
                lobbyWarp != null &&
                specWarp != null &&
                !spawnpoints.isEmpty());

        lobbySetup = (l1 != null &&
                l2 != null);
    }

    public boolean isLobbySetup() {
        return lobbySetup;
    }


}
