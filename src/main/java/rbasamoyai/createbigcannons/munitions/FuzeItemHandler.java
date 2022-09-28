package rbasamoyai.createbigcannons.munitions;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleStackStorage;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public class FuzeItemHandler extends SingleStackStorage {

	private final FuzedBlockEntity be;
	
	public FuzeItemHandler(FuzedBlockEntity be) {
		this.be = be;
	}

	@Override public ItemStack getStack() { return this.be.getFuze(); }

	@Override
	protected boolean canInsert(ItemVariant itemVariant) {
		return !itemVariant.isBlank() && itemVariant.getItem() instanceof FuzeItem && this.be.getFuze().isEmpty();
	}

	@Override
	protected void setStack(ItemStack stack) {
		this.be.setFuze(stack);
	}
}
