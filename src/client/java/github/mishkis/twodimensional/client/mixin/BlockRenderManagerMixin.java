package github.mishkis.twodimensional.client.mixin;

import github.mishkis.twodimensional.TwoDimensional;
import github.mishkis.twodimensional.client.TwoDimensionalClient;
import github.mishkis.twodimensional.utils.Plane;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRenderManager.class)
public abstract class BlockRenderManagerMixin {
    @Inject(method = "renderBlock", at = @At("HEAD"), cancellable = true)
    public void renderBlock(
            BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices, VertexConsumer vertexConsumer, boolean cull, Random random, CallbackInfo ci
    ) {
        ci.cancel();

        Plane plane = TwoDimensionalClient.plane;
        TwoDimensional.LOGGER.info("Hey woah");
        if (plane != null) {
            TwoDimensional.LOGGER.info(plane.toString());
            TwoDimensional.LOGGER.info(String.valueOf(plane.sdf(pos.toCenterPos())));
            if (plane.sdf(pos.toCenterPos()) <= -0.05) {
                ci.cancel();
            }
        }
    }
}
