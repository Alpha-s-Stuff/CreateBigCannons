package rbasamoyai.createbigcannons.crafting;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.fabric.RegistryObject;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.core.Registry;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCRegistries;
import rbasamoyai.createbigcannons.crafting.builtup.BuiltUpHeatingRecipe;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastingRecipe;

public interface BlockRecipeType<T extends BlockRecipe> {
	
	public static final Entry<CannonCastingRecipe> CANNON_CASTING = register("cannon_casting");
	public static final Entry<BuiltUpHeatingRecipe> BUILT_UP_HEATING = register("built_up_heating");
	
	private static <T extends BlockRecipe> Entry<T> register(String id) {
		AbstractRegistrate<?> reg = CreateBigCannons.registrate();
		return reg.entry(id, cb -> new Builder<>(reg, reg, id, cb, () -> new Simple<T>(id))).register();
	}
	
	public static class Simple<T extends BlockRecipe> implements BlockRecipeType<T> {
		private final String id;
		
		public Simple(String id) {
			this.id = id;
		}
		
		@Override public String toString() { return this.id; }
	}
	
	static class Entry<T extends BlockRecipe> extends RegistryEntry<BlockRecipeType<T>> {
		public Entry(AbstractRegistrate<?> owner, RegistryObject<BlockRecipeType<T>> delegate) {
			super(owner, delegate);
		}
	}
	
	static class Builder<T extends BlockRecipe, P> extends AbstractBuilder<BlockRecipeType<?>, BlockRecipeType<T>, P, Builder<T, P>> {
		private final NonNullSupplier<BlockRecipeType<T>> factory;
		
		public Builder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, NonNullSupplier<BlockRecipeType<T>> factory) {
			super(owner, parent, name, callback, CBCRegistries.Keys.BLOCK_RECIPE_TYPES);
			this.factory = factory;
		}

		@Override protected @NonnullType BlockRecipeType<T> createEntry() { return this.factory.get(); }
		
		@Override public Entry<T> register() { return (Entry<T>) super.register(); }
		@Override protected Entry<T> createEntryWrapper(RegistryObject<BlockRecipeType<T>> delegate) {
			Registry.register(CBCRegistries.BLOCK_RECIPE_TYPES.get(), delegate.getId(), createEntry());
			return new Entry<>(this.getOwner(), RegistryObject.of(delegate.getId(), CBCRegistries.BLOCK_RECIPE_TYPES.get()));
		}
	}
	
	public static void register() {}
	
}
