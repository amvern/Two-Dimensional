package github.mishkis.twodimensional.client.rendering;

import github.mishkis.twodimensional.client.TwoDimensionalClient;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.util.Identifier;

public class TwoDimensionalCrosshairRenderer {
    public static void intialize() {
        HudRenderCallback.EVENT.register(((drawContext, tickDelta) -> {
            if (TwoDimensionalClient.plane != null) {
                Mouse mouse = MinecraftClient.getInstance().mouse;
                drawContext.drawTexture(new Identifier("textures/gui/icons.png"), (int) (mouse.getX()/2) - 6, (int) (mouse.getY()/2) - 5, 0, 0, 15, 15);
            }
        }));
    }
}
