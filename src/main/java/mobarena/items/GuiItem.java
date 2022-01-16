package mobarena.items;

import mobarena.gui.CreateArenaGui;
import mobarena.gui.CreateArenaScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GuiItem extends Item {

    public GuiItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand){
        MinecraftClient.getInstance().openScreen(new CreateArenaScreen(new CreateArenaGui()));

        return super.use(world, user, hand);
    }
}
