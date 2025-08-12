package github.mishkis.twodimensional.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import github.mishkis.twodimensional.client.TwoDimensionalClient;
import github.mishkis.twodimensional.utils.Plane;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @ModifyExpressionValue(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/chunk/ChunkBuilder$ChunkData;getBlockEntities()Ljava/util/List;")
    )
    private List<BlockEntity> cullBlockEntites(List<BlockEntity> original) {
        Plane plane = TwoDimensionalClient.plane;
        if (plane != null) {
            original = original.stream().filter(blockEntity ->
                    !Plane.shouldCull(blockEntity.getPos(), plane))
                    .toList();
        }

        return original;
    }
}
