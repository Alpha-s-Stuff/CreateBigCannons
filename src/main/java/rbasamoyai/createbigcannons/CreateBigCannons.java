package rbasamoyai.createbigcannons;

import io.github.fabricators_of_create.porting_lib.event.common.OnDatapackSyncCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.players.PlayerList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import rbasamoyai.createbigcannons.base.CBCCommonEvents;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.crafting.BlockRecipeFinder;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;
import rbasamoyai.createbigcannons.crafting.BlockRecipesManager;
import rbasamoyai.createbigcannons.crafting.CBCRecipeTypes;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.munitions.fluidshell.FluidBlob;
import rbasamoyai.createbigcannons.network.CBCNetwork;

import javax.annotation.Nullable;

public class CreateBigCannons implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "createbigcannons";
	
	private static final NonNullSupplier<CreateRegistrate> REGISTRATE = CreateRegistrate.lazy(MOD_ID);

	@Override
	public void onInitialize() {
		CBCRegistries.init();
		
		ModGroup.register();
		CBCBlocks.register();
		CBCItems.register();
		CBCBlockEntities.register();
		CBCEntityTypes.register();
		CBCMenuTypes.register();
		CBCFluids.register();
		CBCRecipeTypes.register();

		CannonCastShape.CANNON_CAST_SHAPES.register();
		CBCContraptionTypes.prepare();
		CBCChecks.register();
		BlockRecipeSerializer.register();
		BlockRecipeType.register();

		CBCParticleTypes.PARTICLE_TYPES.register();
		
		CBCTags.register();
		
		this.onCommonSetup();
		
		this.onAddReloadListeners();
		OnDatapackSyncCallback.EVENT.register(this::onDatapackSync);
		CBCCommonEvents.register();
		
		CBCConfigs.registerConfigs();
		
		this.registerSerializers();

		REGISTRATE.get().register();
	}
	
	private void onCommonSetup() {
		CBCNetwork.init();
	}
	
	private void onAddReloadListeners() {
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(BlockRecipeFinder.LISTENER);
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(BlockRecipesManager.ReloadListener.INSTANCE);
	}
	
	private void onDatapackSync(PlayerList playerList, @Nullable ServerPlayer player) {
		if (player == null) {
			BlockRecipesManager.syncToAll();
		} else {
			BlockRecipesManager.syncTo(player);
		}
	}
	
	public static CreateRegistrate registrate() {
		return REGISTRATE.get();
	}
	
	public static ResourceLocation resource(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
	
	private void registerSerializers() {
		EntityDataSerializers.registerSerializer(FluidBlob.FLUID_STACK_SERIALIZER);
	}

}
