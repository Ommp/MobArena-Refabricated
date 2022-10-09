package mobarena;

import mobarena.Wave.WaveType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.HashMap;

import static mobarena.MobArena.team;

public class Spawner {
    private ArrayList<MobEntity> monsters = new ArrayList<>();

    private int deadMonsters;
    private String arenaName;
    private ServerWorld world;

    public void addEntitiesToSpawn(HashMap<String, Integer> mobs) {
        //for every string in mobs, create an entity as many times as the value of the number
        for (var str: mobs.keySet()) {
            for (int i = 0; i < mobs.get(str); i++) {
                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putString("id", str);
                Entity entity = EntityType.loadEntityWithPassengers(nbtCompound, world, entity2 -> entity2);
                if (entity instanceof MobEntity) {
                    monsters.add((MobEntity) entity);
                }
            }
        }
    }
    public void addStatusEffects(WaveType type) {
        for (MobEntity monster : monsters) {
            if (type.equals(WaveType.BOSS)) {
                monster.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 10000, 3));
                monster.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 10000, 2));
            }
            if (type.equals(WaveType.SWARM)) {
                monster.setHealth(monster.getMaxHealth() / 3);
                monster.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 10000, 2));
            }
        }
    }

    public void spawnMobs(){
        for (MobEntity entity: monsters) {
            ServerPlayerEntity p = ArenaManager.getArena(arenaName).getRandomArenaPlayer();
            Vec3i spawnPoint = ArenaManager.getArena(arenaName).getSpawnPointNearPlayer(p);
            ArenaManager.connectMobToArena(entity.getUuidAsString(), arenaName);
            entity.updatePosition(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());
            entity.initialize(world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.SPAWNER, null, null);
            world.spawnEntity(entity);
            world.getScoreboard().addPlayerToTeam(entity.getUuidAsString(), team);
            var closestPlayer = ArenaManager.getArena(arenaName).getClosestPlayer(entity);
            entity.setTarget(closestPlayer);
        }
    }

    public Spawner(String arenaName, ServerWorld world) {
        this.arenaName = arenaName;
        this.world = world;
    }

    public void count() {
        deadMonsters++;
    }

    public void resetDeadMonsters() {
        deadMonsters = 0;
    }

    public int getDeadMonsters() {
        return deadMonsters;
    }

    public void clearMonsters() {
        for (LivingEntity entity : monsters) {
            entity.remove(Entity.RemovalReason.DISCARDED);
        }
        monsters.clear();
    }
}