package github.mishkis.twodimensional.mixin;

import net.minecraft.core.HolderSet;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Holder;

//TODO: further bug test and implement POI & BIOMES
//TODO: force structure spawning along Z axis (which maybe entirely replace the need for this mixin)

@Mixin(targets = "net.minecraft.server.commands.LocateCommand")
public class LocateCommandMixin {
    private static final int Z_TOLERANCE = 5;

    @Redirect(
            method = "locateStructure",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/chunk/ChunkGenerator;findNearestMapStructure(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/HolderSet;Lnet/minecraft/core/BlockPos;IZ)Lcom/mojang/datafixers/util/Pair;"
            )
    )
    private static Pair<BlockPos, Holder<Structure>> filterStructureByZ(
            ChunkGenerator chunkGenerator,
            ServerLevel level,
            HolderSet<Structure> holderSet,
            BlockPos searchPos,
            int radius,
            boolean skipExisting
    ) {
        Pair<BlockPos, Holder<Structure>> pair = chunkGenerator.findNearestMapStructure(level, holderSet, searchPos, radius, skipExisting);
        if (pair == null) return null;

        int playerZ = searchPos.getZ();
        int structureZ = pair.getFirst().getZ();

        if (Math.abs(structureZ - playerZ) <= Z_TOLERANCE) return pair;
        return null;
    }
}

