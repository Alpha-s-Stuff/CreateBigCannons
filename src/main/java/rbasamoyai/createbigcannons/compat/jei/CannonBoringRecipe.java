package rbasamoyai.createbigcannons.compat.jei;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.crafting.boring.TransformableByBoring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CannonBoringRecipe extends HardcodedBlockRecipe {

	private final List<ItemStack> ingredients = new ArrayList<>(1);
	private final Block ingredientRaw;
	
	public CannonBoringRecipe(ResourceLocation id, Block result, Block ingredient) {
		super(id, result);
		this.ingredients.add(new ItemStack(ingredient));
		this.ingredientRaw = ingredient;
	}
	
	@Override public List<ItemStack> ingredients() { return this.ingredients; }
	
	public Block ingredientBlock() { return this.ingredientRaw; }
	
	public static Collection<CannonBoringRecipe> makeAllBoringRecipes() {
		return Registry.BLOCK
		.stream()
		.filter(TransformableByBoring.class::isInstance)
		.map(b -> new CannonBoringRecipe(Registry.BLOCK.getKey(b), ((TransformableByBoring) b).getBoredBlockState(b.defaultBlockState()).getBlock(), b))
		.toList();
	}

}
