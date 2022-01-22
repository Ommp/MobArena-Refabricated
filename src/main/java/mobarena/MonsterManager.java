package mobarena;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.*;

public class MonsterManager {

    private Set<LivingEntity> monsters, sheep, golems;
    private Map<LivingEntity, List<ItemStack>> suppliers;

    public MonsterManager() {
        this.monsters   = new HashSet<>();
        this.sheep      = new HashSet<>();
        this.golems     = new HashSet<>();
        this.suppliers  = new HashMap<>();
    }

    public void reset() {
        monsters.clear();
        sheep.clear();
        golems.clear();
        suppliers.clear();
    }

    public void clear() {
        removeAll(monsters);
        removeAll(sheep);
        removeAll(golems);
        removeAll(suppliers.keySet());

        reset();
    }

    private void removeAll(Collection<? extends Entity> collection) {
        for (Entity e : collection) {
            if (e != null) {
                e.remove();
            }
        }
    }

    public void remove(Entity e) {
        if (monsters.remove(e)) {
            sheep.remove(e);
            golems.remove(e);
            suppliers.remove(e);
        }
    }

    public Set<LivingEntity> getMonsters() {
        return monsters;
    }

    public void addMonster(LivingEntity e) {
        monsters.add(e);
    }

    public boolean removeMonster(Entity e) {
        return monsters.remove(e);
    }

    public Set<LivingEntity> getExplodingSheep() {
        return sheep;
    }

    public void addExplodingSheep(LivingEntity e) {
        sheep.add(e);
    }

    public boolean removeExplodingSheep(LivingEntity e) {
        return sheep.remove(e);
    }

    public Set<LivingEntity> getGolems() {
        return golems;
    }

    public void addGolem(LivingEntity e) {
        golems.add(e);
    }

    public boolean removeGolem(LivingEntity e) {
        return golems.remove(e);
    }

    public void addSupplier(LivingEntity e, List<ItemStack> drops) {
        suppliers.put(e, drops);
    }

    public List<ItemStack> getLoot(Entity e) {
        return suppliers.get(e);
    }



}
