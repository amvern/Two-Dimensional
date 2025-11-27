package github.mishkis.twodimensional.client.rendering;

import github.mishkis.twodimensional.client.TwoDimensionalClient;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.resources.ResourceLocation;

public class TwoDimensionalCrosshairRenderer {
    public static void intialize() {
        HudRenderCallback.EVENT.register(((guiGraphics, tickDelta) -> {
            if (TwoDimensionalClient.plane != null) {
                MouseHandler mouse = Minecraft.getInstance().mouseHandler;
                int scaleFactor = Minecraft.getInstance().getWindow().getWidth()/guiGraphics.guiWidth();
                guiGraphics.blit(ResourceLocation.withDefaultNamespace("textures/gui/sprites/hud/crosshair.png"), (int) (mouse.xpos()/scaleFactor) - 6, (int) (mouse.ypos()/scaleFactor) - 5, 0, 0, 15, 15, 15, 15);
            }
        }));
    }
}
