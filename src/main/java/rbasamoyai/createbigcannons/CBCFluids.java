package rbasamoyai.createbigcannons;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributeHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class CBCFluids {

	public static final FluidEntry<SimpleFlowableFluid.Flowing> MOLTEN_CAST_IRON =
			standardFluid("molten_cast_iron")
			.lang(f -> "fluid.createbigcannons.molten_cast_iron", "Molten Cast Iron")
			.tag(AllTags.forgeFluidTag("molten_cast_iron"))
//			.attributes(b -> b.viscosity(1250)
//					.density(7100)
//					.temperature(1200))
			.fluidProperties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.levelDecreasePerBlock(3)
					.blastResistance(100f))
			.source(SimpleFlowableFluid.Source::new)
			.register();
	
	public static final FluidEntry<SimpleFlowableFluid.Flowing> MOLTEN_BRONZE =
			standardFluid("molten_bronze")
			.lang(f -> "fluid.createbigcannons.molten_bronze", "Molten Bronze")
			.tag(AllTags.forgeFluidTag("molten_bronze"))
//			.attributes(b -> b.viscosity(1250)
//					.density(8770)
//					.temperature(920))
			.fluidProperties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.levelDecreasePerBlock(3)
					.blastResistance(100f))
			.source(SimpleFlowableFluid.Source::new)
			.register();
	
	public static final FluidEntry<SimpleFlowableFluid.Flowing> MOLTEN_STEEL =
			standardFluid("molten_steel")
			.lang(f -> "fluid.createbigcannons.molten_steel", "Molten Steel")
			.tag(AllTags.forgeFluidTag("molten_steel"))
//			.attributes(b -> b.viscosity(1250)
//					.density(7040)
//					.temperature(1430))
			.fluidProperties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.levelDecreasePerBlock(3)
					.blastResistance(100f))
			.source(SimpleFlowableFluid.Source::new)
			.register();
	
	public static final FluidEntry<SimpleFlowableFluid.Flowing> MOLTEN_NETHERSTEEL =
			standardFluid("molten_nethersteel")
			.lang(f -> "fluid.createbigcannons.molten_nethersteel", "Molten Nethersteel")
//			.attributes(b -> b.viscosity(1250)
//					.density(7040)
//					.temperature(1430))
			.fluidProperties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.levelDecreasePerBlock(3)
					.blastResistance(100f))
			.source(SimpleFlowableFluid.Source::new)
			.register();
	
	private static FluidBuilder<SimpleFlowableFluid.Flowing, CreateRegistrate> standardFluid(String name) {
		return CreateBigCannons.registrate()
				.fluid(name, CreateBigCannons.resource("fluid/" + name + "_still"), CreateBigCannons.resource("fluid/" + name + "_flow")/*, factory*/);
	}
	
	public static void register() {
		FluidVariantAttributes.register(MOLTEN_BRONZE.get(), new FluidVariantAttributeHandler() {

			@Override
			public int getTemperature(FluidVariant variant) {
				return 920;
			}

			@Override
			public int getViscosity(FluidVariant variant, @Nullable Level world) {
				return 1250;
			}
		});
	}
	
}
