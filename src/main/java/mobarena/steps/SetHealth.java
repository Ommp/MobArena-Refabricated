package mobarena.steps;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.server.network.ServerPlayerEntity;

public class SetHealth extends PlayerStep{
    private static final int NORMAL_FIRE = -20;
    private static final int NORMAL_AIR = 300;

    private float health;
    private int fire;
    private int air;

    private SetHealth(ServerPlayerEntity player) {
        super(player);
    }

    @Override
    public void run(){
        health = player.getHealth();
        fire = player.getFireTicks();
        air = player.getAir();

        player.setAir(NORMAL_AIR);
        player.setFireTicks(NORMAL_FIRE);

        player.setHealth(20);
    }

    @Override
    public void undo(){
        player.setHealth(health);
        player.setFireTicks(fire);
        player.setAir(air);
    }

    static StepFactory create(){
        return SetHealth::new;
    }
}
