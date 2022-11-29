package mobarena.abilities;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;

public class Drain implements Ability{


    public final String name = "drain";
    public final AbilityTargetType type = AbilityTargetType.SINGLE_TARGET;

    @Override
    public void use(MobEntity user) {

        var target = user.getTarget();

        var drainedHealth = target.getHealth()/2;
        target.setHealth(target.getHealth()-drainedHealth/5);
        user.setHealth(user.getHealth()+drainedHealth);

        ServerWorld world = (ServerWorld) user.world;
        world.spawnParticles(ParticleTypes.HEART, user.getX() + world.random.nextDouble(), user.getY() + 1, user.getZ() + world.random.nextDouble(), 10, 0.0, 0.01, 0.0, 0.2);
        world.spawnParticles(ParticleTypes.SOUL, user.getTarget().getX() + world.random.nextDouble(), user.getTarget().getY() + 1, user.getTarget().getZ() + world.random.nextDouble(), 10, 0.0, 0.01, 0.0, 0.2);

    }

    @Override
    public String getName() {
        return this.name;
    }
}
