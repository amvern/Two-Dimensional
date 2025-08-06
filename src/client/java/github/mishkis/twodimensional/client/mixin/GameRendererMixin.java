package github.mishkis.twodimensional.client.mixin;

import github.mishkis.twodimensional.TwoDimensional;
import github.mishkis.twodimensional.client.TwoDimensionalClient;
import github.mishkis.twodimensional.utils.Plane;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class GameRendererMixin {
    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
    private static void shouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction side, BlockPos otherPos, CallbackInfoReturnable<Boolean> cir) {
        Plane plane = TwoDimensionalClient.plane;
        if (plane != null) {
            double dist = plane.sdf(pos.toCenterPos());
            if (dist <= -0.05){
                cir.setReturnValue(false);
            } else if (dist <= 0.05 && plane.getNormal().dotProduct(Vec3d.of(side.getVector())) <= 0) {
                cir.setReturnValue(true);
            }
        }
    }
}
