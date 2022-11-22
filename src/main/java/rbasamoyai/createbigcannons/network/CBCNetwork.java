package rbasamoyai.createbigcannons.network;

import me.pepperbell.simplenetworking.SimpleChannel;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.BlockRecipesManager.ClientboundRecipesPacket;

public class CBCNetwork {

	public static final String VERSION = "1.3.0";
	
	public static final SimpleChannel INSTANCE = construct();
	
	public static SimpleChannel construct() {
		SimpleChannel channel = new SimpleChannel(CreateBigCannons.resource("network"));
		
		int id = 0;
		
		channel.registerC2SPacket(ServerboundTimedFuzePacket.class, id++);
		
		channel.registerS2CPacket(ClientboundRecipesPacket.class, id++);
		
		channel.registerS2CPacket(ClientboundUpdateContraptionPacket.class, id++);
		
		channel.messageBuilder(ServerboundProximityFuzePacket.class, id++)
				.encoder(ServerboundProximityFuzePacket::encode)
				.decoder(ServerboundProximityFuzePacket::new)
				.consumer(ServerboundProximityFuzePacket::handle)
				.add();

		channel.messageBuilder(ServerboundFiringActionPacket.class, id++)
				.encoder(ServerboundFiringActionPacket::encode)
				.decoder(ServerboundFiringActionPacket::new)
				.consumer(ServerboundFiringActionPacket::handle)
				.add();

		channel.messageBuilder(ServerboundCarriageWheelPacket.class, id++)
				.encoder(ServerboundCarriageWheelPacket::encode)
				.decoder(ServerboundCarriageWheelPacket::new)
				.consumer(ServerboundCarriageWheelPacket::handle)
				.add();
		
		return channel;
	}
	
	public static void init() {}
	
}
