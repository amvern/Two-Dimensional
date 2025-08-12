package github.mishkis.twodimensional.mixin;

import github.mishkis.twodimensional.duck_interface.EntityPlaneGetterSetter;
import github.mishkis.twodimensional.utils.Plane;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "isBlockBreakingRestricted", at = @At("HEAD"), cancellable = true)
    private void disableBlockBreakingOutsidePlane(World world, BlockPos pos, GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
        if (Plane.shouldCull(pos, ((EntityPlaneGetterSetter) this).twoDimensional$getPlane())) {
            cir.setReturnValue(true);
        }
    }
}
