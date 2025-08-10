package github.mishkis.twodimensional.client.mixin;

import github.mishkis.twodimensional.client.TwoDimensionalClient;
import github.mishkis.twodimensional.duck_interface.MouseNormalizedGetter;
import github.mishkis.twodimensional.utils.Plane;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Unique
    double twoDimensional$x_mouse_offset = 0;

    @Unique
    double twoDimensional$y_mouse_offset = 0;

    @Shadow private boolean thirdPerson;

    @Shadow protected abstract void setPos(double x, double y, double z);

    @Shadow protected abstract void setRotation(float yaw, float pitch);

    @Shadow protected abstract void moveBy(double x, double y, double z);

    @Shadow public abstract Vec3d getPos();

    @Shadow @Final private Vector3f horizontalPlane;

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"), cancellable = true)
    public void update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        Plane plane = TwoDimensionalClient.plane;
        if (plane != null) {
            this.thirdPerson = true;

            this.setRotation((float) (plane.getYaw() * MathHelper.DEGREES_PER_RADIAN), 0);

            Vec3d pos = new Vec3d(MathHelper.lerp(tickDelta, focusedEntity.prevX, focusedEntity.getX()), MathHelper.lerp(tickDelta, focusedEntity.prevY, focusedEntity.getY()) + focusedEntity.getStandingEyeHeight(), MathHelper.lerp(tickDelta, focusedEntity.prevZ, focusedEntity.getZ()));
            this.setPos(pos.x, pos.y, pos.z);

            MouseNormalizedGetter mouse = (MouseNormalizedGetter) MinecraftClient.getInstance().mouse;
            twoDimensional$x_mouse_offset = MathHelper.lerp(0.2, twoDimensional$x_mouse_offset, mouse.twoDimensional$getNormalizedX());
            twoDimensional$y_mouse_offset = MathHelper.lerp(0.2, twoDimensional$y_mouse_offset, mouse.twoDimensional$getNormalizedY());

            this.moveBy(-8, twoDimensional$y_mouse_offset, twoDimensional$x_mouse_offset);

            ci.cancel();
        }
    }
}
