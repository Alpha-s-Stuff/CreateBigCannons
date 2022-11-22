package rbasamoyai.createbigcannons.munitions.fluidshell;

import com.simibubi.create.foundation.fluid.SmartFluidTank;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.FluidTank;
import io.github.fabricators_of_create.porting_lib.transfer.fluid.FluidTransferable;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.FuzedBlockEntity;

import java.util.List;

public class FluidShellBlockEntity extends FuzedBlockEntity implements FluidTransferable {

	protected FluidTank tank;
	
	public FluidShellBlockEntity(BlockEntityType<? extends FluidShellBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.tank = new SmartFluidTank(getFluidShellCapacity(), this::onFluidStackChanged);
	}
	
	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.put("FluidContent", this.tank.writeToNBT(new CompoundTag()));
	}
	
	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.tank.readFromNBT(tag.getCompound("FluidContent"));
	}

	@Nullable
	@Override
	public Storage<FluidVariant> getFluidStorage(@Nullable Direction side) {
		if (side == this.getBlockState().getValue(BlockStateProperties.FACING) && this.fuze.isEmpty()) {
			return tank;
		}
		return null;
	}
	
	public static int getFluidShellCapacity() {
		return CBCConfigs.SERVER.munitions.fluidShellCapacity.get();
	}
	
	protected void onFluidStackChanged(FluidStack newStack) {
		if (!this.hasLevel()) return;
		if (!this.level.isClientSide) {
			this.notifyUpdate();
		}
	}
	
	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		super.addToGoggleTooltip(tooltip, isPlayerSneaking);
		this.containedFluidTooltip(tooltip, isPlayerSneaking, this.tank);
		return true;
	}

}
