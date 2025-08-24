package github.mishkis.twodimensional.client.mixin;

import github.mishkis.twodimensional.client.TwoDimensionalClient;
import github.mishkis.twodimensional.client.access.MouseNormalizedGetter;
import github.mishkis.twodimensional.utils.Plane;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow private @Nullable Entity vehicle;
    @Shadow public float prevPitch;

    @Shadow public abstract float getPitch();

    @Shadow public float prevYaw;

    @Shadow public abstract float getYaw();

    @Shadow public abstract void setPitch(float pitch);

    @Shadow public abstract void setYaw(float yaw);

    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    public void changeLookDirection(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        Plane plane = TwoDimensionalClient.plane;
        if (plane != null) {
            this.prevPitch = this.getPitch();
            this.prevYaw = this.getYaw();

            MouseNormalizedGetter mouse = (MouseNormalizedGetter) MinecraftClient.getInstance().mouse;
            float pitch = (float) (MathHelper.atan2(-mouse.twoDimensional$getNormalizedY() * 0.60, Math.abs(mouse.twoDimensional$getNormalizedX())) * MathHelper.DEGREES_PER_RADIAN);
            this.setPitch(MathHelper.clamp(pitch, -90, 90));

            double base = plane.getYaw() * MathHelper.DEGREES_PER_RADIAN;
            if (TwoDimensionalClient.turnedAround.isPressed()) {
                this.setYaw((float) MathHelper.lerp(MathHelper.clamp(3. * mouse.twoDimensional$getNormalizedX() + 0.5, 0, 1), base + 90, base - 90));
            } else {
                this.setYaw((float) MathHelper.lerp(MathHelper.clamp(7 * mouse.twoDimensional$getNormalizedX() + 0.5, 0, 1), base + 90, base + 270));
            }

            if (this.vehicle != null) {
                this.vehicle.onPassengerLookAround((Entity) (Object) this);
            }

            ci.cancel();
        }
    }
}
