package mobarena;

import mobarena.Wave.WaveType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static mobarena.MobArena.team;

public class Spawner {
    private ArrayList<MobEntity> monsters = new ArrayList<>();

    private int deadMonsters;
    private String arenaName;

    public void addEntitiesToSpawn(HashMap<String, Integer> mobs, ServerWorld world) {
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
    public void modifyMobStats(WaveType type) {
        for (MobEntity monster : monsters) {
            if (type.equals(WaveType.BOSS)) {

                Objects.requireNonNull(monster.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)).addTemporaryModifier(new EntityAttributeModifier("movement", monster.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)*1.2, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
                Objects.requireNonNull(monster.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).addTemporaryModifier(new EntityAttributeModifier("max health", 3, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
                monster.setHealth(monster.getMaxHealth());
                Objects.requireNonNull(monster.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE)).addTemporaryModifier(new EntityAttributeModifier("attack damage", 2, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
                LocalDifficulty localDifficulty = new LocalDifficulty(Difficulty.HARD, 10,10000,10);
                monster.initEquipment(localDifficulty);

            }
            if (type.equals(WaveType.SWARM)) {
                Objects.requireNonNull(monster.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)).addTemporaryModifier(new EntityAttributeModifier("movement", monster.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED)*2.5, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
                Objects.requireNonNull(monster.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH)).addTemporaryModifier(new EntityAttributeModifier("health", monster.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH)/-2, EntityAttributeModifier.Operation.ADDITION));
                monster.setHealth(monster.getMaxHealth());

            }
        }
    }

    public void spawnMobs(ServerWorld world){
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

    public Spawner(String arenaName) {
        this.arenaName = arenaName;
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

    public ArrayList<MobEntity> getMonsters() {
        return monsters;
    }
}