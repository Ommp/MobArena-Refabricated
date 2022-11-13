package mobarena.abilities;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.Vec3d;

public class Fireball implements Ability {

    private final String name = "fireball";
    public final AbilityTargetType type = AbilityTargetType.SINGLE_TARGET;

    @Override
    public void use(MobEntity user) {
        //spawn fireball from user and send towards target
        Vec3d vec3d = user.getRotationVec(1.0f);

        var target = user.getTarget();
        double f = target.getX() - (user.getX() + vec3d.x * 4.0);
        double g = target.getBodyY(0.5) - (0.5 + user.getBodyY(0.5));
        double h = target.getZ() - (user.getZ() + vec3d.z * 4.0);

        FireballEntity fireballEntity = new FireballEntity(user.world, user, f,g,h, 1);
        fireballEntity.setPosition(user.getX() + vec3d.x * 4.0, user.getBodyY(0.5) + 0.5, fireballEntity.getZ() + vec3d.z * 4.0);
        user.world.spawnEntity(fireballEntity);
    }

    @Override
    public String getName() {
        return this.name;
    }
}
