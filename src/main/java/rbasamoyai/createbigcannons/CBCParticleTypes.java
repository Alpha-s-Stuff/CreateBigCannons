package rbasamoyai.createbigcannons;

import com.mojang.serialization.Codec;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import rbasamoyai.createbigcannons.cannonmount.CannonPlumeParticleData;
import rbasamoyai.createbigcannons.cannonmount.CannonSmokeParticleData;
import rbasamoyai.createbigcannons.munitions.fluidshell.FluidBlobParticleData;

public class CBCParticleTypes {

	public static final LazyRegistrar<ParticleType<?>> PARTICLE_TYPES = LazyRegistrar.create(Registry.PARTICLE_TYPE, CreateBigCannons.MOD_ID);
	
	public static final RegistryObject<ParticleType<CannonPlumeParticleData>> CANNON_PLUME = PARTICLE_TYPES.register("cannon_plume",
			() -> new ParticleType<CannonPlumeParticleData>(false, CannonPlumeParticleData.DESERIALIZER) {
				@Override
				public Codec<CannonPlumeParticleData> codec() {
					return CannonPlumeParticleData.CODEC;
				}
			});

	public static final RegistryObject<ParticleType<FluidBlobParticleData>> FLUID_BLOB = PARTICLE_TYPES.register("fluid_blob",
			() -> new ParticleType<FluidBlobParticleData>(false, FluidBlobParticleData.DESERIALIZER) {
				@Override
				public Codec<FluidBlobParticleData> codec() {
					return FluidBlobParticleData.CODEC;
				}
			});
	
	public static final RegistryObject<ParticleType<CannonSmokeParticleData>> CANNON_SMOKE = PARTICLE_TYPES.register("cannon_smoke",
			() -> new ParticleType<CannonSmokeParticleData>(false, CannonSmokeParticleData.DESERIALIZER) {
				@Override
				public Codec<CannonSmokeParticleData> codec() {
					return CannonSmokeParticleData.CODEC;
				}
			});
	
}
