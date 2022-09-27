
package rbasamoyai.createbigcannons;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import rbasamoyai.createbigcannons.cannonmount.CannonPlumeParticle;
import rbasamoyai.createbigcannons.ponder.CBCPonderIndex;

public class CreateBigCannonsClient implements ClientModInitializer {
	public static void onRegisterParticleFactories() {
		ParticleFactoryRegistry.getInstance().register(CBCParticleTypes.CANNON_PLUME.get(), new CannonPlumeParticle.Provider());
	}

	@Override
	public void onInitializeClient() {
		CBCBlockPartials.init();

		CBCPonderIndex.register();
		CBCPonderIndex.registerTags();

		onRegisterParticleFactories();
	}
	
}
