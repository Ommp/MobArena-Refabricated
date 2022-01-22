package mobarena.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import mobarena.Arena;
import mobarena.Location;
import mobarena.MobArena;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;


public class CreateArenaGui extends LightweightGuiDescription {
    public CreateArenaGui() {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(300,150);


        WLabel label = new WLabel("Test arena with various GUI elements");
        WButton button = new WButton(new LiteralText("Create test"));

        root.add(button,10,10,50,30);
        root.add(label, 20,20);


          button.setOnClick(() -> {
//              Arena arena = new Arena("test");
//              Location lobbyWarp = new Location(new BlockPos(10,50,10),0,0);
              MobArena.LOGGER.info("Arena button test");
              
          });
          root.validate(this);


    }

}
