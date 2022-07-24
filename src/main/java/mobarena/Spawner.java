package mobarena;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.*;
import net.minecraft.server.world.ServerWorld;
import org.apache.logging.log4j.Level;

import java.util.*;

public class Spawner {

    private ArrayList<EntityType> potentialMonsters = new ArrayList<>();
    ArrayList<HostileEntity> monsters = new ArrayList<>();
    private ServerWorld world;

    public void addPotentialMobs() {
        potentialMonsters.add(EntityType.ZOMBIE);
        potentialMonsters.add(EntityType.HUSK);
        potentialMonsters.add(EntityType.BLAZE);
        potentialMonsters.add(EntityType.SPIDER);
    }

    public void prepareMobs(String arenaName, int mobsToSpawn, double x, double y, double z) {
        world = MobArena.arenaManager.arenas.get(arenaName).getWorld();

        monsters.clear();

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
            MobArena.log(Level.INFO, String.valueOf(potentialMonsters.get(i)));
        }

        MobArena.log(Level.INFO, String.valueOf(monsters.size()));

        for (HostileEntity entity: monsters) {
            MobArena.log(Level.INFO, String.valueOf(entity.getUuid()));
            entity.updatePosition(x, y, z);

            world.spawnEntity(entity);
        }


        MobArena.log(Level.INFO, "finished wave.");

    }

    public ArrayList<HostileEntity> getMonsters() {
        return monsters;
    }
}