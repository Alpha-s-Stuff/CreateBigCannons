package rbasamoyai.createbigcannons.compat.jei;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.crafting.incomplete.IncompleteCannonBlock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IncompleteCannonBlockRecipe extends HardcodedBlockRecipe {

	private final List<ItemStack> ingredients = new ArrayList<>();
	
	public IncompleteCannonBlockRecipe(ResourceLocation id, Block result, Block start) {
		super(id, result);
		this.ingredients.add(new ItemStack(start));
		if (start instanceof IncompleteCannonBlock incomplete) {
			incomplete.requiredItems().stream().map(ItemStack::new).forEach(this.ingredients::add);
		}
	}
	
	@Override public List<ItemStack> ingredients() { return this.ingredients; }
	
	public static Collection<IncompleteCannonBlockRecipe> makeAllIncompleteRecipes() {
		return Registry.BLOCK
		.stream()
		.filter(IncompleteCannonBlock.class::isInstance)
		.map(b -> new IncompleteCannonBlockRecipe(null, ((IncompleteCannonBlock) b).getCompleteBlockState(b.defaultBlockState()).getBlock(), b))
		.toList();
	}

}
