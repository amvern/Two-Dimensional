package github.mishkis.twodimensional.client.rendering;

import github.mishkis.twodimensional.client.TwoDimensionalClient;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.resources.ResourceLocation;

public class TwoDimensionalCrosshairRenderer {
    public static void initialize() {
        HudRenderCallback.EVENT.register(((guiGraphics, tickDelta) -> {
            if (TwoDimensionalClient.plane != null) {
                MouseHandler mouse = Minecraft.getInstance().mouseHandler;
                int scaleFactor = Minecraft.getInstance().getWindow().getWidth()/guiGraphics.guiWidth();
                guiGraphics.blit(
                        ResourceLocation.withDefaultNamespace("textures/gui/icons.png"),
                    (int) (mouse.xpos()/scaleFactor), // half of 16
                        (int) (mouse.ypos()/scaleFactor), // half of 16
                        0, 0, 1, 1, 1, 1
                );
            }
        }));
    }
}
