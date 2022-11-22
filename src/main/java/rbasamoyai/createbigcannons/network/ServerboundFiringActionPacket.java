package rbasamoyai.createbigcannons.network;

import me.pepperbell.simplenetworking.C2SPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import rbasamoyai.createbigcannons.cannonmount.carriage.CannonCarriageEntity;

public class ServerboundFiringActionPacket implements C2SPacket {

    public ServerboundFiringActionPacket() {}
    public ServerboundFiringActionPacket(FriendlyByteBuf buf) {}
    public void encode(FriendlyByteBuf buf) {}

    public void handle(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, PacketSender responseSender, SimpleChannel channel) {
        server.execute(() -> {
            if (player.getVehicle() instanceof CannonCarriageEntity carriage) carriage.tryFiringShot();
        });
    }

}
