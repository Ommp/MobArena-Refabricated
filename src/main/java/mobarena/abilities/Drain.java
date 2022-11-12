package mobarena.abilities;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.ParticleTypes;

import java.util.Random;


public class Drain implements Ability{


    public final String name = "drain";
    public final AbilityTargetType type = AbilityTargetType.SINGLE_TARGET;

    @Override
    public void use(MobEntity user) {

        var target = user.getTarget();

        var drainedHealth = target.getHealth()/2;
        System.out.println(target.getHealth());
        target.setHealth(target.getHealth()-drainedHealth/10);
        user.setHealth(user.getHealth()+drainedHealth);
        System.out.println("drained " + drainedHealth);
        System.out.println(target.getHealth());
        System.out.println(user.getHealth());

        Random random = new Random();

        float f = (random.nextFloat() - 0.5f) * 8.0f;
        float g = (random.nextFloat() - 0.5f) * 4.0f;
        float h = (random.nextFloat() - 0.5f) * 8.0f;
        user.world.addParticle(ParticleTypes.DRAGON_BREATH, user.getX() + (double)f, user.getY() + 2.0 + (double)g, user.getZ() + (double)h, 0.0, 0.0, 0.0);

    }

    @Override
    public String getName() {
        return this.name;
    }
}
