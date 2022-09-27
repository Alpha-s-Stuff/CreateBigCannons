package rbasamoyai.createbigcannons.network;

import me.pepperbell.simplenetworking.SimpleChannel;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.BlockRecipesManager.ClientboundRecipesPacket;

public class CBCNetwork {

	public static final String VERSION = "1.1.0";
	
	public static final SimpleChannel INSTANCE = construct();
	
	public static SimpleChannel construct() {
		SimpleChannel channel = new SimpleChannel(CreateBigCannons.resource("network"));
		
		int id = 0;
		
		channel.registerC2SPacket(ServerboundTimedFuzePacket.class, id++);
		
		channel.registerS2CPacket(ClientboundRecipesPacket.class, id++);
		
		channel.registerS2CPacket(ClientboundUpdateContraptionPacket.class, id++);
		
		return channel;
	}
	
	public static void init() {}
	
}
