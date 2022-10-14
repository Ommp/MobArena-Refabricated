package mobarena.region;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class Region {

    private final BlockPos pos1, pos2;

    public Region(BlockPos pos1, BlockPos pos2) {
        this.pos1 = min(pos1, pos2);
        this.pos2 = max(pos1, pos2);
    }

    public BlockPos min(BlockPos pos1, BlockPos pos2) {
        return new BlockPos(
                Math.min(pos1.getX(), pos2.getX()),
                Math.min(pos1.getY(), pos2.getY()),
                Math.min(pos1.getZ(), pos2.getZ())
        );
    }

    public BlockPos max(BlockPos pos1, BlockPos pos2) {
        return new BlockPos(
                Math.max(pos1.getX(), pos2.getX()),
                Math.max(pos1.getY(), pos2.getY()),
                Math.max(pos1.getZ(), pos2.getZ())
        );
    }

    public boolean isInsideRegion(BlockPos p) {
        //x
        if (p.getX() < this.pos1.getX() || p.getX() > this.pos2.getX()) {
            return false;
        }
        //z
        else if (p.getZ() < this.pos1.getZ() || p.getZ() > this.pos2.getZ()) {
            return false;
        }
        // y
        else if (p.getY() < this.pos1.getY() || p.getY() > this.pos2.getY()) {
            return false;
        }
        else {
            return true;
        }
    }

    public Box asBox() {
        return new Box( this.pos1.getX(), this.pos1.getY(), this.pos1.getZ(),
                        this.pos2.getX(), this.pos2.getY(), this.pos2.getZ()
        );}
}
