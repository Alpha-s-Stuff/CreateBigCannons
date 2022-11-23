package rbasamoyai.createbigcannons;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.function.Consumer;

public class CBCFluids {

	public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_CAST_IRON =
			standardFluid("molten_cast_iron", NoColorFluidAttributes::new)
			.lang(f -> "fluid.createbigcannons.molten_cast_iron", "Molten Cast Iron")
			.tag(AllTags.forgeFluidTag("molten_cast_iron"))
			.properties(b -> b.viscosity(1250)
					.density(7100)
					.temperature(1200))
			.fluidProperties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.slopeFindDistance(3)
					.explosionResistance(100f))
			.source(ForgeFlowingFluid.Source::new)
			.register();
	
	public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_BRONZE =
			standardFluid("molten_bronze", NoColorFluidAttributes::new)
			.lang(f -> "fluid.createbigcannons.molten_bronze", "Molten Bronze")
			.tag(AllTags.forgeFluidTag("molten_bronze"))
			.properties(b -> b.viscosity(1250)
					.density(8770)
					.temperature(920))
			.fluidProperties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.slopeFindDistance(3)
					.explosionResistance(100f))
			.source(ForgeFlowingFluid.Source::new)
			.register();
	
	public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_STEEL =
			standardFluid("molten_steel", NoColorFluidAttributes::new)
			.lang(f -> "fluid.createbigcannons.molten_steel", "Molten Steel")
			.tag(AllTags.forgeFluidTag("molten_steel"))
			.properties(b -> b.viscosity(1250)
					.density(7040)
					.temperature(1430))
			.fluidProperties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.slopeFindDistance(3)
					.explosionResistance(100f))
			.source(ForgeFlowingFluid.Source::new)
			.register();
	
	public static final FluidEntry<ForgeFlowingFluid.Flowing> MOLTEN_NETHERSTEEL =
			standardFluid("molten_nethersteel", NoColorFluidAttributes::new)
			.lang(f -> "fluid.createbigcannons.molten_nethersteel", "Molten Nethersteel")
			.properties(b -> b.viscosity(1250)
					.density(7040)
					.temperature(1430))
			.fluidProperties(p -> p.levelDecreasePerBlock(2)
					.tickRate(25)
					.slopeFindDistance(3)
					.explosionResistance(100f))
			.source(ForgeFlowingFluid.Source::new)
			.register();
	
	private static FluidBuilder<ForgeFlowingFluid.Flowing, CreateRegistrate> standardFluid(String name, FluidBuilder.FluidTypeFactory factory) {
		return CreateBigCannons.registrate()
				.fluid(name, CreateBigCannons.resource("fluid/" + name + "_still"), CreateBigCannons.resource("fluid/" + name + "_flow"), factory);
	}
	
	public static void register() {}
	
	private static class NoColorFluidAttributes extends FluidType {
		private final ResourceLocation stillTexture, flowingTexture;

		protected NoColorFluidAttributes(FluidType.Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
			super(properties);
			this.stillTexture = stillTexture;
			this.flowingTexture = flowingTexture;
		}

		@Override
		public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
			consumer.accept(new IClientFluidTypeExtensions() {
				@Override
				public int getTintColor() {
					return 0x00ffffff;
				}

				@Override
				public ResourceLocation getStillTexture() {
					return stillTexture;
				}

				@Override
				public ResourceLocation getFlowingTexture() {
					return flowingTexture;
				}
			});
		}
	}
	
}
