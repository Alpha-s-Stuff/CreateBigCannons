
package rbasamoyai.createbigcannons;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.fabricators_of_create.porting_lib.event.client.FogEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import rbasamoyai.createbigcannons.cannonmount.CannonPlumeParticle;
import rbasamoyai.createbigcannons.cannonmount.CannonSmokeParticle;
import rbasamoyai.createbigcannons.cannonmount.carriage.CannonCarriageEntity;
import rbasamoyai.createbigcannons.munitions.fluidshell.FluidBlobParticle;
import rbasamoyai.createbigcannons.network.CBCNetwork;
import rbasamoyai.createbigcannons.network.ServerboundFiringActionPacket;
import rbasamoyai.createbigcannons.ponder.CBCPonderIndex;

import java.util.Arrays;
import java.util.List;

public class CreateBigCannonsClient implements ClientModInitializer {

	private static final String KEY_ROOT = "key." + CreateBigCannons.MOD_ID;
	private static final String KEY_CATEGORY = KEY_ROOT + ".category";
	public static final KeyMapping PITCH_MODE = new KeyMapping(KEY_ROOT + ".pitch_mode", InputConstants.KEY_C, KEY_CATEGORY);
	public static final KeyMapping FIRE_CONTROLLED_CANNON = new KeyMapping(KEY_ROOT + ".fire_controlled_cannon", InputConstants.KEY_F, KEY_CATEGORY);

	public static void prepareClient() {
		CBCBlockPartials.init();
		CreateBigCannonsClient.onRegisterParticleFactories();
		
		FogEvents.SET_COLOR.register(CreateBigCannonsClient::getFogColor);
		FogEvents.ACTUAL_RENDER_FOG.register(CreateBigCannonsClient::getFogDensity);
		ClientTickEvents.END_CLIENT_TICK.register(CreateBigCannonsClient::onClientGameTick);
	}
	
	public static void onRegisterParticleFactories() {
		ParticleFactoryRegistry.getInstance().register(CBCParticleTypes.CANNON_PLUME.get(), new CannonPlumeParticle.Provider());
		ParticleFactoryRegistry.getInstance().register(CBCParticleTypes.FLUID_BLOB.get(), new FluidBlobParticle.Provider());
		ParticleFactoryRegistry.getInstance().register(CBCParticleTypes.CANNON_SMOKE.get(), CannonSmokeParticle.Provider::new);
	}

	@Override
	public void onInitializeClient() {
		CBCPonderIndex.register();
		CBCPonderIndex.registerTags();
		CBCBlockPartials.resolveDeferredModels();
		KeyBindingHelper.registerKeyBinding(PITCH_MODE);
		KeyBindingHelper.registerKeyBinding(FIRE_CONTROLLED_CANNON);

		prepareClient();
	}
	
	public static void getFogColor(FogEvents.ColorData event, float partialTicks) {
		Camera info = event.getCamera();
		Minecraft mc = Minecraft.getInstance();
		Level level = mc.level;
		BlockPos blockPos = info.getBlockPosition();
		FluidState fluidState = level.getFluidState(blockPos);
		if (info.getPosition().y > blockPos.getY() + fluidState.getHeight(level, blockPos)) return;

		Fluid fluid = fluidState.getType();

		if (CBCFluids.MOLTEN_CAST_IRON.get().isSame(fluid)) {
			event.setRed(70 / 255f);
			event.setGreen(10 / 255f);
			event.setBlue(11 / 255f);
			return;
		}
		if (CBCFluids.MOLTEN_BRONZE.get().isSame(fluid)) {
			event.setRed(99 / 255f);
			event.setGreen(66 / 255f);
			event.setBlue(22 / 255f);
			return;
		}
		if (CBCFluids.MOLTEN_STEEL.get().isSame(fluid)) {
			event.setRed(111 / 255f);
			event.setGreen(110 / 255f);
			event.setBlue(106 / 255f);
			return;
		}
		if (CBCFluids.MOLTEN_NETHERSTEEL.get().isSame(fluid)) {
			event.setRed(76 / 255f);
			event.setGreen(50 / 255f);
			event.setBlue(58 / 255f);
			return;
		}
	}
	
	public static boolean getFogDensity(FogRenderer.FogMode type, Camera info, FogEvents.FogData fogData) {
		Minecraft mc = Minecraft.getInstance();
		Level level = mc.level;
		BlockPos blockPos = info.getBlockPosition();
		FluidState fluidState = level.getFluidState(blockPos);
		if (info.getPosition().y > blockPos.getY() + fluidState.getHeight(level, blockPos)) return false;

		Fluid fluid = fluidState.getType();
		
		List<Fluid> moltenMetals = Arrays.asList(
				CBCFluids.MOLTEN_CAST_IRON.get(),
				CBCFluids.MOLTEN_BRONZE.get(),
				CBCFluids.MOLTEN_STEEL.get(),
				CBCFluids.MOLTEN_NETHERSTEEL.get());
		
		for (Fluid fluid1 : moltenMetals) {
			if (fluid1.isSame(fluid)) {
				fogData.scaleFarPlaneDistance(1f / 32f);
				return true;
			}
		}
		return false;
	}

	public static void onClientGameTick(Minecraft mc) {
		if (mc.player == null) return;
		if (mc.player.getVehicle() instanceof CannonCarriageEntity carriage) {
			net.minecraft.client.player.Input input = mc.player.input;
			boolean isPitching = CreateBigCannonsClient.PITCH_MODE.isDown();
			boolean isFiring = CreateBigCannonsClient.FIRE_CONTROLLED_CANNON.isDown();
			carriage.setInput(input.left, input.right, input.up, input.down, isPitching);
			mc.player.handsBusy |= input.left | input.right | input.up | input.down | isFiring;

			if (isFiring) {
				CBCNetwork.INSTANCE.sendToServer(new ServerboundFiringActionPacket());
			}
		}
	}
	
}
