package mobarena;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;

public class Spawner {
    private ArrayList<String> potentialMobs = new ArrayList<>();
    private ArrayList<HostileEntity> monsters = new ArrayList<>();

    private int deadMonsters;
    private String arenaName;
    private ServerWorld world;

    public void addDefaultMobs() {
        potentialMobs.add("minecraft:zombie");
        potentialMobs.add("minecraft:husk");
        potentialMobs.add("minecraft:spider");
        potentialMobs.add("minecraft:cave_spider");
        potentialMobs.add("minecraft:skeleton");
        potentialMobs.add("minecraft:pillager");
    }

    public void setPotentialMobs(ArrayList<String> mobs) {
        this.potentialMobs = mobs;
    }

    public void addEntitiesToSpawn(int mobsToSpawn, WaveType waveType) {
        for (int i = 0; i < mobsToSpawn; i++) {
            int index = (int)(Math.random() * potentialMobs.size());


            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putString("id", potentialMobs.get(index));
            Entity entity = EntityType.loadEntityWithPassengers(nbtCompound, world, entity2 -> entity2);
            if (entity instanceof HostileEntity) {
                monsters.add((HostileEntity) entity);
            }

            if (waveType.equals(WaveType.BOSS)) {
                monsters.get(i).addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1000000, 3));
                monsters.get(i).addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 1000000, 2));
            }
            if (waveType.equals(WaveType.SWARM)) {
                monsters.get(i).setHealth(monsters.get(i).getMaxHealth() / 2);
                monsters.get(i).addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1000000, 2));
            }
        }
    }

    public void spawnMobs(){
        for (MobEntity entity: monsters) {
            Vec3i spawnPoint = ArenaManager.getArena(arenaName).getSpawnPointNearPlayer();
            ArenaManager.connectMobToArena(entity.getUuidAsString(), arenaName);
            entity.updatePosition(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());
            entity.initialize(world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.SPAWNER, null, null);
            world.spawnEntity(entity);
        }
    }

    public ArrayList<HostileEntity> getMonsters() {
        return monsters;
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