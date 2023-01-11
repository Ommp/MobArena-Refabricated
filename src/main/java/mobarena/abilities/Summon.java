package mobarena.abilities;

import mobarena.ArenaManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;

public class Summon implements Ability {

    AbilityTargetType type = AbilityTargetType.AREA;
    private final String name = "summon";

    @Override
    public void use(MobEntity user) {
        ServerWorld world = (ServerWorld) user.getEntityWorld();
        var arenaName = ArenaManager.getArenaFromMob(user.getUuidAsString());

        var nbtCompound = new NbtCompound();
        nbtCompound.putString("id", String.valueOf(RegistryKey.ofRegistry(Registries.ENTITY_TYPE.getId(user.getType())).getValue()));

        MobEntity entity = (MobEntity) EntityType.loadEntityWithPassengers(nbtCompound, world, entity1 -> entity1);
        ArenaManager.getArena(arenaName).spawner.addMonster(entity);
        ArenaManager.connectMobToArena(entity.getUuidAsString(), arenaName);
        entity.updatePosition(user.getX(), user.getY(), user.getZ());
        entity.initialize(world, world.getLocalDifficulty(user.getBlockPos()), SpawnReason.SPAWNER, null, null);
        world.spawnEntity(entity);
    }

    @Override
    public String getName() {
        return name;
    }

}
