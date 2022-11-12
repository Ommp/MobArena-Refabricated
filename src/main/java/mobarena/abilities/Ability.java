package mobarena.abilities;

import net.minecraft.entity.mob.MobEntity;

public interface Ability {

    String getName();

    void use(MobEntity user);

}
