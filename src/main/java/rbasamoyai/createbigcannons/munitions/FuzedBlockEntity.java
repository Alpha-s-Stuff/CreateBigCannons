package rbasamoyai.createbigcannons.munitions;

import com.simibubi.create.foundation.tileEntity.SyncedTileEntity;

import io.github.fabricators_of_create.porting_lib.transfer.item.ItemTransferable;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import io.github.fabricators_of_create.porting_lib.util.NBTSerializer;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

public class FuzedBlockEntity extends SyncedTileEntity implements ItemTransferable {

	protected ItemStack fuze = ItemStack.EMPTY;
	private LazyOptional<Storage<ItemVariant>> fuzeOptional;
	
	public FuzedBlockEntity(BlockEntityType<? extends FuzedBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Nullable
	@Override
	public Storage<ItemVariant> getItemStorage(@Nullable Direction side) {
		if (side == this.getBlockState().getValue(BlockStateProperties.FACING)) {
			if (this.fuzeOptional == null) {
				this.fuzeOptional = LazyOptional.of(this::createHandler);
			}
			return this.fuzeOptional.getValueUnsafer();
		}
		return null;
	}
	
	public void setFuze(ItemStack stack) { this.fuze = stack; }
	public ItemStack getFuze() { return this.fuze; }
	
	private Storage<ItemVariant> createHandler() {
		return new FuzeItemHandler(this);
	}
	
	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		if (this.fuzeOptional != null) {
			this.fuzeOptional.invalidate();
		}
	}
	
	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (!this.fuze.isEmpty()) {
			tag.put("Fuze", NBTSerializer.serializeNBT(this.fuze));
		}
	}
	
	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.fuze = tag.contains("Fuze", Tag.TAG_COMPOUND) ? ItemStack.of(tag.getCompound("Fuze")) : ItemStack.EMPTY;
	}
}
