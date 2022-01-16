package mobarena.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.text.LiteralText;


public class CreateArenaGui extends LightweightGuiDescription {
    public CreateArenaGui() {
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(120,215);


//        WLabel label = new WLabel("Test arena");
        WButton button = new WButton(new LiteralText("Create test arena"));

//        root.add(label, 1,1);
          root.add(button,1,1,50,20);

          button.setOnClick(() -> System.out.println("Arena created"));
          root.validate(this);


    }

}
