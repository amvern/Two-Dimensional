package github.mishkis.twodimensional.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import github.mishkis.twodimensional.TwoDimensional;
import github.mishkis.twodimensional.client.TwoDimensionalClient;
import github.mishkis.twodimensional.utils.Plane;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
    private static void cullPlane(BlockState state, BlockView world, BlockPos pos, Direction side, BlockPos otherPos, CallbackInfoReturnable<Boolean> cir) {
        Plane plane = TwoDimensionalClient.plane;
        if (plane != null) {
            double dist = plane.sdf(pos.toCenterPos());
            if (dist <= Plane.CULL_DIST || dist >= 32){
                cir.setReturnValue(false);
                return;
            } else if (dist <= 0.5) {
                if (side.getOffsetY() == 0 && plane.sdf(pos.offset(side, 1).toCenterPos()) <= Plane.CULL_DIST){
                    cir.setReturnValue(true);
                    return;
                }
            }
        }

        if (((AbstractBlockStateAccessor) world.getBlockState(pos.offset(side, 1))).getOpaque()) {
            cir.setReturnValue(false);
        }
    }
}
