package mobarena;

import mobarena.region.ArenaRegion;
import net.minecraft.block.Block;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface Arena {
    String getName();

    void setName(String name);

    ServerWorld getWorld();

    void setWorld(ServerWorld world);

    BlockPos getLobbyWarp();

    void setLobbyWarp(BlockPos lobbyWarp);

    List<ServerPlayerEntity> getAllPlayers();

    Collection<ArenaPlayer> getArenaPlayerSet();

    List<ServerPlayerEntity> getNonreadyPlayers();

    Set<ServerPlayerEntity> getReadyPlayersInLobby();

    Set<ServerPlayerEntity> getSpectators();

    Set<ServerPlayerEntity> getPlayersInArena();

    MobArena getMod();

    ArenaRegion getRegion();

    boolean playerJoin(ServerPlayerEntity p, BlockPos loc);

    void playerReady(ServerPlayerEntity p);

    boolean startArena();

    boolean endArena();

    boolean inArena(ServerPlayerEntity p);

    boolean inLobby(ServerPlayerEntity p);

    boolean inSpec(ServerPlayerEntity p);

    boolean isDead(ServerPlayerEntity p);

    boolean isRunning();

    boolean inEditMode();

    void setEditMode(boolean value);

    Set<Block> getBlocks();

    void addBlock(Block b);

    boolean removeBlock(Block b);

    void forceEnd();

    MonsterManager getMonsterManager();

    boolean isEnabled();

}
