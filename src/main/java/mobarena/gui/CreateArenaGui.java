package mobarena.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import mobarena.MobArena;
import net.minecraft.text.LiteralText;


public class CreateArenaGui extends LightweightGuiDescription {
    public CreateArenaGui() {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(300,150);


        WLabel label = new WLabel("Test arena with various GUI elements");
        WButton button = new WButton(new LiteralText("Create test"));

        root.add(button,10,10,50,30);
        root.add(label, 20,20);


          button.setOnClick(() -> MobArena.setupArenaObject("test"));
          root.validate(this);


    }

}
