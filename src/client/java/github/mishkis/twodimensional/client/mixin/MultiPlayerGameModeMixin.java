package github.mishkis.twodimensional.client.mixin;

import github.mishkis.twodimensional.access.EntityPlaneGetterSetter;
import github.mishkis.twodimensional.client.TwoDimensionalClient;
import github.mishkis.twodimensional.network.InteractionLayerHolder;
import github.mishkis.twodimensional.network.LayerMode;
import github.mishkis.twodimensional.utils.Plane;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void disableInteractionOutsidePlane(LocalPlayer player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
//        Plane plane = TwoDimensionalClient.plane;
//        if (plane != null) {
//            double dist = plane.sdf(hitResult.getBlockPos().getCenter());
//            if (dist <= Plane.CULL_DIST || dist >= 1.8) {
//                cir.setReturnValue(InteractionResult.FAIL);
//            }
//        }

        Plane plane = ((EntityPlaneGetterSetter) this).twoDimensional$getPlane();
        if (plane == null) return;

        double dist = plane.sdf(hitResult.getBlockPos().getCenter());
        boolean isOnPlane = hitResult.getBlockPos().getCenter().z == plane.getOffset().z;

        if (this instanceof InteractionLayerHolder holder) {
            LayerMode mode = holder.getInteractionLayer();

            boolean cancel = switch (mode) {
                case BASE -> !isOnPlane;
                case FACE_AWAY -> Plane.shouldCull(hitResult.getBlockPos(), plane) || dist >= 1.8 || isOnPlane;
                case FACE_CAMERA -> dist >= 1.8 || isOnPlane || Plane.shouldCull(new BlockPos(hitResult.getBlockPos().getX(), hitResult.getBlockPos().getY(), hitResult.getBlockPos().getZ() + 1), plane);
            };

            if (cancel) cir.setReturnValue(InteractionResult.FAIL);
        }
    }
}
