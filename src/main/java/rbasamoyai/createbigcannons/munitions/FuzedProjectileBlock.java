package rbasamoyai.createbigcannons.munitions;

import com.simibubi.create.foundation.block.ITE;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import rbasamoyai.createbigcannons.CBCBlockEntities;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public abstract class FuzedProjectileBlock extends ProjectileBlock implements ITE<FuzedBlockEntity> {

	protected FuzedProjectileBlock(Properties properties) {
		super(properties);
	}
	
	@Override public Class<FuzedBlockEntity> getTileEntityClass() { return FuzedBlockEntity.class; }
	@Override public BlockEntityType<? extends FuzedBlockEntity> getTileEntityType() { return CBCBlockEntities.FUZED_BLOCK.get(); }

	protected static ItemStack getFuze(BlockEntity blockEntity) {
		if (blockEntity == null) return ItemStack.EMPTY;
		Direction facing = blockEntity.getBlockState().getValue(FACING);
		Storage<ItemVariant> items = TransferUtil.getItemStorage(blockEntity, facing);
		return TransferUtil.getItems(items, 1).get(0);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		return this.onTileEntityUse(level, pos, be -> {
			if (!level.isClientSide) {
				Storage<ItemVariant> h = TransferUtil.getItemStorage(be, result.getDirection());
				if (h != null) {
					if (player.getItemInHand(hand).isEmpty() && !TransferUtil.getItems(h, 1).get(0).isEmpty()) {
						player.addItem(TransferUtil.extractAnyItem(h, 1));
						level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.NEUTRAL, 1.0f, 1.0f);
					} else if (TransferUtil.getItems(h, 1).get(0).isEmpty() && player.getItemInHand(hand).getItem() instanceof FuzeItem) {
						ItemStack handItem = player.getItemInHand(hand);
						long itemResult = TransferUtil.insertItem(h, player.getItemInHand(hand));
						if (!player.getAbilities().instabuild) player.setItemInHand(hand, new ItemStack(handItem.getItem(), (int) itemResult));
						level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.NEUTRAL, 1.0f, 1.0f);
					}
				}
				be.setChanged();
				be.sendData();
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		});
	}
	
}
