package github.mishkis.twodimensional;

import github.mishkis.twodimensional.access.EntityPlaneGetterSetter;
import github.mishkis.twodimensional.network.InteractionLayerHolder;
import github.mishkis.twodimensional.network.InteractionLayerPayload;
import github.mishkis.twodimensional.utils.Plane;
import github.mishkis.twodimensional.utils.PlanePersistentState;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class TwoDimensional implements ModInitializer {
    public static final String MOD_ID = "two_dimensional";
    public static final Logger LOGGER = Logger.getLogger(MOD_ID);

    public record PlaneSyncPayload(double x, double z, double yaw) implements CustomPacketPayload {
        public static final Type<PlaneSyncPayload> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(TwoDimensional.MOD_ID, "plane_sync"));

        public static final StreamCodec<RegistryFriendlyByteBuf, PlaneSyncPayload> CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.DOUBLE, PlaneSyncPayload::x,
                        ByteBufCodecs.DOUBLE, PlaneSyncPayload::z,
                        ByteBufCodecs.DOUBLE, PlaneSyncPayload::yaw,
                        PlaneSyncPayload::new
                );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static void setPlayerPlane(MinecraftServer server, ServerPlayer player) {
        // Default 2D plane at player's current X/Z with yaw 0
        double x = player.getBlockX() + 0.5;
        double z = player.getBlockZ() + 0.5;
        double yaw = 0.0;

        final Plane plane = new Plane(new Vec3(x, 0, z));
        PlanePersistentState.setPlayerPlane(player, x, z, yaw);

        server.execute(() -> {
            ServerPlayNetworking.send(player, new PlaneSyncPayload(x, z, yaw));

            // Assign plane to player
            ((EntityPlaneGetterSetter) player).twoDimensional$setPlane(plane);
            player.setPosRaw(x, player.position().y, z);
        });
    }

    @Override
    public void onInitialize() {
        // Automatically assign 2D plane on player join
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            setPlayerPlane(server, handler.getPlayer());
        });

        PayloadTypeRegistry.playS2C().register(
                PlaneSyncPayload.TYPE,
                PlaneSyncPayload.CODEC
        );

        PayloadTypeRegistry.playC2S().register(
                InteractionLayerPayload.TYPE,
                InteractionLayerPayload.CODEC
        );

        ServerPlayNetworking.registerGlobalReceiver(
                InteractionLayerPayload.TYPE,
                (payload, ctx) -> {
                    // Runs on the network thread; schedule on server thread
                    try {
                        ctx.server().execute(() -> {
                            ((InteractionLayerHolder) ctx.player()).setInteractionLayer(payload.mode());
                        });
                    } catch (Exception err) {
                        TwoDimensional.LOGGER.info(err.getMessage());
                    }
                }
        );
    }
}