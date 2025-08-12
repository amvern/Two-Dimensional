package github.mishkis.twodimensional.mixin;

import net.fabricmc.fabric.mixin.object.builder.AbstractBlockAccessor;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockSettingsAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpreadableBlock.class)
public class SpreadableBlockMixin {
    // Override vanilla minecraft spread behaviour due to every block being opaque :)
    @Inject(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/light/ChunkLightProvider;getRealisticOpacity(Lnet/minecraft/world/BlockView;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;I)I"), cancellable = true)
    private static void canSurvive(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!((AbstractBlockStateAccessor) world.getBlockState(pos.up())).getOpaque()
        || !world.getBlockState(pos.up()).isFullCube(world, pos.up()));
    }
}
