package github.mishkis.twodimensional.client.mixin;

import github.mishkis.twodimensional.client.TwoDimensionalClient;
import github.mishkis.twodimensional.utils.Plane;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Shadow public abstract boolean isOpaque();

    @Inject(method = "getAmbientOcclusionLightLevel", at = @At("HEAD"), cancellable = true)
    private void getAmbientOcclusionLightLevel(BlockView world, BlockPos pos, CallbackInfoReturnable<Float> cir) {
        if (TwoDimensionalClient.plane != null && TwoDimensionalClient.plane.sdf(pos.toCenterPos()) <= Plane.CULL_DIST){
            cir.setReturnValue(1f);
        }
    }

    @Inject(method = "getOpacity", at = @At("HEAD"), cancellable = true)
    private void getOpacity(BlockView world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        Plane plane = TwoDimensionalClient.plane;
        if (Plane.shouldCull(pos, plane)) {
            if (this.isOpaque()) {
                cir.setReturnValue(1);
            } else {
                cir.setReturnValue(0);
            }
        }
    }
}
