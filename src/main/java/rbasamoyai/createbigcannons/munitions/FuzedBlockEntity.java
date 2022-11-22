package rbasamoyai.createbigcannons.munitions;

import java.util.List;

import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.tileEntity.SyncedTileEntity;
import com.simibubi.create.foundation.utility.Lang;

import io.github.fabricators_of_create.porting_lib.transfer.item.ItemTransferable;
import io.github.fabricators_of_create.porting_lib.util.NBTSerializer;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public class FuzedBlockEntity extends SyncedTileEntity implements IHaveGoggleInformation, ItemTransferable {

	protected ItemStack fuze = ItemStack.EMPTY;
	private FuzeItemHandler fuzeStorage = new FuzeItemHandler(this);
	
	public FuzedBlockEntity(BlockEntityType<? extends FuzedBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Nullable
	@Override
	public Storage<ItemVariant> getItemStorage(@Nullable Direction side) {
		if (side == this.getBlockState().getValue(BlockStateProperties.FACING)) {
			return fuzeStorage;
		}
		return null;
	}
	
	public void setFuze(ItemStack stack) { this.fuze = stack; }
	public ItemStack getFuze() { return this.fuze; }
	
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

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		Lang.builder("block")
			.translate(CreateBigCannons.MOD_ID + ".shell.tooltip.fuze")
			.style(ChatFormatting.YELLOW)
			.forGoggles(tooltip);
		if (!this.fuze.isEmpty() && this.fuze.getItem() instanceof FuzeItem fuzeItem) {
			Lang.builder()
			.add(fuzeItem.getDescription().copy())
			.style(ChatFormatting.GREEN)
			.forGoggles(tooltip, 1);
			fuzeItem.addExtraInfo(tooltip, isPlayerSneaking, this.fuze);
		} else {
			Lang.builder("block")
			.translate(CreateBigCannons.MOD_ID + ".shell.tooltip.fuze.none")
			.style(ChatFormatting.DARK_GRAY)
			.forGoggles(tooltip, 1);
		}
		return true;
	}

}
