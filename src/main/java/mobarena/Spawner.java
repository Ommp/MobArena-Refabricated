package mobarena;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;

public class Spawner {
    private ArrayList<EntityType> potentialMonsters = new ArrayList<>();
    private ArrayList<HostileEntity> monsters = new ArrayList<>();

    private int deadMonsters;
    private String arenaName;
    private ServerWorld world;

    public void addPotentialMonsters() {
        potentialMonsters.add(EntityType.ZOMBIE);
        potentialMonsters.add(EntityType.HUSK);
        potentialMonsters.add(EntityType.BLAZE);
        potentialMonsters.add(EntityType.SPIDER);
    }
    public void prepareSpawner(int mobsToSpawn, WaveType waveType) {
        for (int i = 0; i < mobsToSpawn; i++) {
            int index = (int)(Math.random() * potentialMonsters.size());
            if (potentialMonsters.get(index) == EntityType.ZOMBIE) {
                monsters.add(new ZombieEntity(EntityType.ZOMBIE, world));
            }
            else if (potentialMonsters.get(index) == EntityType.HUSK) {
                monsters.add(new HuskEntity(EntityType.HUSK, world));
            }
            else if (potentialMonsters.get(index) == EntityType.SPIDER) {
                monsters.add(new SpiderEntity(EntityType.SPIDER, world));
            }
            else if (potentialMonsters.get(index) == EntityType.BLAZE) {
                monsters.add(new BlazeEntity(EntityType.BLAZE, world));
            }
            if (waveType.equals(WaveType.BOSS)) {
                monsters.get(i).addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 1000000, 3));
                monsters.get(i).addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 1000000, 2));
                monsters.get(i).addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1000000, 3));
                monsters.get(i).addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 1000000, 2));
            }
            if (waveType.equals(WaveType.SWARM)) {
                monsters.get(i).setHealth(monsters.get(i).getMaxHealth() / 2);
                monsters.get(i).setMovementSpeed(monsters.get(i).getMovementSpeed() * 2);
            }
        }
    }

    public void spawnMobs(){
        for (HostileEntity entity: monsters) {
            Vec3i spawnPoint = MobArena.arenaManager.arenas.get(arenaName).getRandomSpawnPoint();
            MobArena.arenaManager.connectMobToArena(entity.getUuidAsString(), arenaName);
            entity.updatePosition(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());
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
        monsters.clear();
    }
}