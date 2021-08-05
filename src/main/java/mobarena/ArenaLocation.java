package mobarena;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.text.BaseText;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class ArenaLocation {
    public final Vec3d pos;
    public final RegistryKey<World> dim;

    public ArenaLocation(RegistryKey<World> dim, double x, double y, double z) {
        this.dim = dim;
        this.pos = new Vec3d(x,y,z);
    }

//    public LiteralText toLiteralTextSimple() {
//        return new LiteralText(String.format("(%.1f, %.1f,%.1f)", pos.x,pos.y,pos, pos.z));
//    }


}
