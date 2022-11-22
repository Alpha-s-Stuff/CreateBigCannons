package rbasamoyai.createbigcannons.crafting;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.pepperbell.simplenetworking.S2CPacket;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.network.CBCNetwork;

public class BlockRecipesManager {

	private static final Map<ResourceLocation, BlockRecipe> BLOCK_RECIPES_BY_NAME = new HashMap<>();
	private static final Map<BlockRecipeType<?>, Map<ResourceLocation, BlockRecipe>> BLOCK_RECIPES_BY_TYPE = new HashMap<>();
	
	public static Collection<BlockRecipe> getRecipes() {
		return BLOCK_RECIPES_BY_NAME.values();
	}

	public static Collection<BlockRecipe> getRecipesOfType(BlockRecipeType<?> type) {
		return BLOCK_RECIPES_BY_TYPE.getOrDefault(type, new HashMap<>()).values();
	}
	
	public static void clear() {
		BLOCK_RECIPES_BY_NAME.clear();
		BLOCK_RECIPES_BY_TYPE.clear();
	}
	
	public static void writeBuf(FriendlyByteBuf buf) {
		buf.writeVarInt(BLOCK_RECIPES_BY_NAME.size());
		for (Map.Entry<ResourceLocation, BlockRecipe> entry : BLOCK_RECIPES_BY_NAME.entrySet()) {
			buf.writeResourceLocation(entry.getKey());
			toNetworkCasted(buf, entry.getValue());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends BlockRecipe> void toNetworkCasted(FriendlyByteBuf buf, T recipe) {
		BlockRecipeSerializer<T> ser = (BlockRecipeSerializer<T>) recipe.getSerializer();
		buf.writeResourceLocation(CBCRegistries.BLOCK_RECIPE_SERIALIZERS.get().getKey(ser));
		ser.toNetwork(buf, recipe);
	}
	
	public static void readBuf(FriendlyByteBuf buf) {
		clear();
		int sz = buf.readVarInt();
		for (int i = 0; i < sz; ++i) {
			ResourceLocation id = buf.readResourceLocation();
			ResourceLocation type = buf.readResourceLocation();
			BlockRecipe recipe = CBCRegistries.BLOCK_RECIPE_SERIALIZERS.get().get(type).fromNetwork(id, buf);
			BLOCK_RECIPES_BY_NAME.put(id, recipe);
			BlockRecipeType<?> recipeType = CBCRegistries.BLOCK_RECIPE_TYPES.get().get(type);
			if (!BLOCK_RECIPES_BY_TYPE.containsKey(recipeType))
				BLOCK_RECIPES_BY_TYPE.put(recipeType, new HashMap<>());
			BLOCK_RECIPES_BY_TYPE.get(recipeType).put(id, recipe);
		}
	}
	
	public static void syncTo(ServerPlayer player) {
		CBCNetwork.INSTANCE.sendToClient(new ClientboundRecipesPacket(), player);
	}
	
	public static void syncToAll() {
		CBCNetwork.INSTANCE.sendToClientsInCurrentServer(new ClientboundRecipesPacket());
	}
	
	public static class ReloadListener extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final ReloadListener INSTANCE = new ReloadListener();
		
		public ReloadListener() {
			super(GSON, "block_recipes");
		}

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resources, ProfilerFiller profiler) {
			clear();
			
			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
				if (el.isJsonObject()) {
					ResourceLocation id = entry.getKey();
					JsonObject obj = el.getAsJsonObject();
					ResourceLocation type = new ResourceLocation(obj.get("type").getAsString());
					BlockRecipe recipe = CBCRegistries.BLOCK_RECIPE_SERIALIZERS.get().get(type).fromJson(id, obj);
					BLOCK_RECIPES_BY_NAME.put(id, recipe);
					BlockRecipeType<?> recipeType = CBCRegistries.BLOCK_RECIPE_TYPES.get().get(type);
					if (!BLOCK_RECIPES_BY_TYPE.containsKey(recipeType))
						BLOCK_RECIPES_BY_TYPE.put(recipeType, new HashMap<>());
					BLOCK_RECIPES_BY_TYPE.get(recipeType).put(id, recipe);
				}
			}
		}

		@Override
		public ResourceLocation getFabricId() {
			return CreateBigCannons.resource("block_recipes");
		}
	}
	
	public static class ClientboundRecipesPacket implements S2CPacket {
		private FriendlyByteBuf buf;
		
		public ClientboundRecipesPacket() {}
		
		public ClientboundRecipesPacket(FriendlyByteBuf buf) {
			this.buf = buf;
		}
		
		public void encode(FriendlyByteBuf buf) {
			writeBuf(buf);
		}
		
		public void handle(Minecraft client, ClientPacketListener listener, PacketSender responseSender, SimpleChannel channel) {
			client.execute(() -> {
				readBuf(this.buf);
			});
		}
	}
	
}
