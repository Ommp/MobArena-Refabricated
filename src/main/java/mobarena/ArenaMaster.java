package mobarena;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.List;

public interface ArenaMaster {
    MobArena getMod();

    List<Arena> getArenas();

    void addPlayer(ServerPlayerEntity player, Arena arena);

    Arena removePlayer(ServerPlayerEntity player);

    Arena getArenaWithName(Collection<Arena> arenas, String name);

    Arena getArenaWithName(String name);

    List<Arena> getArenasInWorld(ServerWorld world);

    List<ServerPlayerEntity> getAllPlayers();

    List<ServerPlayerEntity> getAllPlayersInArena(String name);

    List<ServerPlayerEntity> getAllLivingPlayers();

    List<ServerPlayerEntity> getLivingPlayersInArena(String arenaName);

    Arena getArenaWithPlayer(ServerPlayerEntity player);

    Arena getArenaWithSpectator(ServerPlayerEntity player);

    Arena getArenaAtLocation(ServerWorld w, BlockPos l);

    boolean isEnabled();

    void setEnabled(boolean value);

    void reloadConfig();

    void saveConfig();

    Arena getArenaWithMonster(Entity e);

}
