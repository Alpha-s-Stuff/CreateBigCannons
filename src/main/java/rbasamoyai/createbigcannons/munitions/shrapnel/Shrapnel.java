package rbasamoyai.createbigcannons.munitions.shrapnel;

import java.util.Random;

import io.github.fabricators_of_create.porting_lib.mixin.common.accessor.DamageSourceAccessor;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;

public class Shrapnel extends AbstractHurtingProjectile {
	
	private int age;
	protected float damage;
	
	public Shrapnel(EntityType<? extends Shrapnel> type, Level level) {
		super(type, level);
	}
	
	@Override
	public void tick() {
		super.tick();
		++this.age;
		if (!this.level.isClientSide && this.age > 200) {
			this.discard();
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Age", this.age);
		tag.putFloat("Damage", this.damage);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.age = tag.getInt("Age");
		this.damage = tag.getFloat("Damage");
	}
	
	public void setDamage(float damage) { this.damage = damage; }
	public float getDamage() { return this.damage; }
	
	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		BlockPos pos = result.getBlockPos();
		BlockState state = this.level.getBlockState(pos);
		if (!this.level.isClientSide) {
			if (state.is(this.getDestroyBlockTag())) {
				this.level.destroyBlock(pos, false, this);
			} else {
				this.level.playSound(null, pos, state.getSoundType().getBreakSound(), SoundSource.NEUTRAL, 1.0f, 2.0f);
			}
		}
		this.discard();
	}
	
	protected TagKey<Block> getDestroyBlockTag() {
		return CBCTags.BlockCBC.SHRAPNEL_SHATTERABLE;
	}
	
	@Override
	protected void onHitEntity(EntityHitResult result) {
		if (result.getEntity().getType() == this.getType()) {
			return;
		}
		result.getEntity().hurt(this.getDamageSource(), this.damage);
		if (!CBCConfigs.SERVER.munitions.invulProjectileHurt.get()) result.getEntity().invulnerableTime = 0;
		this.discard();
	}
	
	public static final DamageSource SHRAPNEL = DamageSourceAccessor.port_lib$init(CreateBigCannons.MOD_ID + ".shrapnel");
	protected DamageSource getDamageSource() { return SHRAPNEL; }
	
	public static void build(FabricEntityTypeBuilder<? extends Shrapnel> builder) {
		builder.trackedUpdateRate(3)
				.trackedUpdateRate(20)
				.forceTrackedVelocityUpdates(true)
				.fireImmune()
				.dimensions(EntityDimensions.fixed(0.25f, 0.25f));
	}
	
	@Override protected float getEyeHeight(Pose pose, EntityDimensions dimensions) { return 0.125f; }
	
	@Override protected float getInertia() { return 0.99f; }
	
	public static void spawnShrapnelBurst(Level level, EntityType<? extends Shrapnel> type, Vec3 position, Vec3 initialVelocity, int count, double spread, float damage) {
		Vec3 forward = initialVelocity.normalize();
		Vec3 right = forward.cross(new Vec3(Direction.UP.step()));
		Vec3 up = forward.cross(right);
		double length = initialVelocity.length();
		Random random = level.getRandom();
		
		int failCount = 0;
		
		for (int i = 0; i < count; ++i) {
			double velScale = length * (1.4d + 0.2d * random.nextDouble());
			Vec3 vel = forward.scale(velScale)
					.add(right.scale((random.nextDouble() - random.nextDouble()) * velScale * spread))
					.add(up.scale((random.nextDouble() - random.nextDouble()) * velScale * spread));
			
			Shrapnel shrapnel = type.create(level);
			double rx = position.x + (random.nextDouble() - random.nextDouble()) * 0.0625d;
			double ry = position.y + (random.nextDouble() - random.nextDouble()) * 0.0625d;
			double rz = position.z + (random.nextDouble() - random.nextDouble()) * 0.0625d;
			shrapnel.setPos(rx, ry, rz);
			shrapnel.setDeltaMovement(vel);
			shrapnel.setDamage(damage);
			
			if (!level.addFreshEntity(shrapnel)) {
				++failCount;
			}
		}
		
		if (failCount > 0) {
			CreateBigCannons.LOGGER.info("Shrapnel burst failed to spawn {} out of {} shrapnel bullets", failCount, count);
		}
	}

}
