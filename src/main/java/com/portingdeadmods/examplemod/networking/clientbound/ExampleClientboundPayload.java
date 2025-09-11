package com.portingdeadmods.examplemod.networking.clientbound;

import com.portingdeadmods.examplemod.ExampleMod;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ExampleClientboundPayload(int payload) implements CustomPacketPayload {
    public static final Type<ExampleClientboundPayload> TYPE = new Type<>(ExampleMod.rl("example_clientbound_payload"));
    public static final StreamCodec<? super RegistryFriendlyByteBuf, ExampleClientboundPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ExampleClientboundPayload::payload,
            ExampleClientboundPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void exampleClientboundAction(IPayloadContext context) {
        context.enqueueWork(() -> {
            LocalPlayer player = (LocalPlayer) context.player();
            ClientLevel level = (ClientLevel) player.level();
        }).exceptionally(err -> {
            ExampleMod.LOGGER.error("Failed to handle " + TYPE.id(), err);
            return null;
        });
    }

}
