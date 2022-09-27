package rbasamoyai.createbigcannons;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import me.alphamode.forgetags.Tags;
import net.minecraft.core.Registry;
import net.minecraft.data.tags.TagsProvider.TagAppender;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
public class CBCTags {

	private static final CreateRegistrate REGISTRATE = CreateBigCannons.registrate();
	
	public static class BlockCBC {
		public static final TagKey<Block>
			THICK_TUBING = createAndGenerateBlockTag(CreateBigCannons.resource("thick_tubing")),
			REDUCES_SPREAD = createAndGenerateBlockTag(CreateBigCannons.resource("reduces_spread")),
			SHRAPNEL_SHATTERABLE = createAndGenerateBlockTag(CreateBigCannons.resource("shrapnel_shatterable")),
			GRAPESHOT_SHATTERABLE = createAndGenerateBlockTag(CreateBigCannons.resource("grapeshot_shatterable")),
			WEAK_CANNON_END = createAndGenerateBlockTag(CreateBigCannons.resource("weak_cannon_end")),
			DRILL_CAN_PASS_THROUGH = createAndGenerateBlockTag(CreateBigCannons.resource("drill_can_pass_through"));
		
		public static TagKey<Block> createAndGenerateBlockTag(ResourceLocation loc) {
			TagKey<Block> tag = TagKey.create(Registry.BLOCK_REGISTRY, loc);
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> prov.tag(tag));
			return tag;
		}
		
		public static void addBlocksToBlockTag(TagKey<Block> tag, Block... blocks) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				prov.tag(tag).add(blocks);
			});
		}
		
		public static void addBlocksToBlockTag(TagKey<Block> tag, List<Supplier<? extends Block>> blocks) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				TagAppender<Block> app = prov.tag(tag);
				for (Supplier<? extends Block> bp : blocks) {
					app.add(bp.get());
				}
			});
		}
		
		public static void addTagsToBlockTag(TagKey<Block> tag, List<TagKey<Block>> tags) {
			REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
				TagAppender<Block> app = prov.tag(tag);
				tags.forEach(app::addTag);
			});
		}
		
		public static void sectionRegister() {
			addTagsToBlockTag(SHRAPNEL_SHATTERABLE, Arrays.asList(Tags.Blocks.GLASS, Tags.Blocks.GLASS_PANES, BlockTags.LEAVES, BlockTags.WOOL));
			addBlocksToBlockTag(SHRAPNEL_SHATTERABLE, Blocks.FLOWER_POT);
			
			addTagsToBlockTag(GRAPESHOT_SHATTERABLE, Arrays.asList(SHRAPNEL_SHATTERABLE, BlockTags.PLANKS, BlockTags.WOODEN_SLABS, BlockTags.WOODEN_STAIRS, BlockTags.WOODEN_FENCES, BlockTags.FENCE_GATES, BlockTags.LOGS));
			addBlocksToBlockTag(GRAPESHOT_SHATTERABLE, Blocks.MELON, Blocks.PUMPKIN);
			addBlocksToBlockTag(GRAPESHOT_SHATTERABLE, Arrays.asList(AllBlocks.ANDESITE_CASING));
		}
	}
	
	public static class ItemCBC {
		public static final TagKey<Item>
			IMPACT_FUZE_HEAD = createAndGenerateItemTag(CreateBigCannons.resource("impact_fuze_head")),
			NUGGET_CAST_IRON = forgeTag("nuggets/cast_iron"),
			INGOT_CAST_IRON = forgeTag("ingots/cast_iron"),
			BLOCK_CAST_IRON = forgeTag("blocks/cast_iron"),
			NUGGET_BRONZE = forgeTag("nuggets/bronze"),
			INGOT_BRONZE = forgeTag("ingots/bronze"),
			BLOCK_BRONZE = forgeTag("blocks/bronze"),
			NUGGET_STEEL = forgeTag("nuggets/steel"),
			INGOT_STEEL = forgeTag("ingots/steel"),
			BLOCK_STEEL = forgeTag("blocks/steel");
		
		public static TagKey<Item> createAndGenerateItemTag(ResourceLocation loc) {
			TagKey<Item> tag = TagKey.create(Registry.ITEM_REGISTRY, loc);
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> prov.tag(tag));
			return tag;
		}
		
		public static TagKey<Item> forgeTag(String path) {
			TagKey<Item> tag = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("c", path));
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> prov.tag(tag));
			return tag;
		}
		
		public static void addItemsToItemTag(TagKey<Item> tag, Item... items) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> {
				prov.tag(tag).add(items);
			});
		}
		
		public static void addItemsToItemTag(TagKey<Item> tag, ItemLike... items) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> {
				TagAppender<Item> app = prov.tag(tag);
				for (ItemLike bp : items) {
					app.add(bp.asItem());
				}
			});
		}
		
		public static void addTagsToItemTag(TagKey<Item> tag, List<TagKey<Item>> tags) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> {
				TagAppender<Item> app = prov.tag(tag);
				tags.forEach(app::addTag);
			});
		}
		
		public static void addIdsToItemTag(TagKey<Item> tag, ResourceLocation... ids) {
			REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> {
				TagAppender<Item> app = prov.tag(tag);
				for (ResourceLocation id : ids) {
					app.addOptional(id);
				}
			});
		}
		
		public static void sectionRegister() {
			addItemsToItemTag(IMPACT_FUZE_HEAD, Items.STONE_BUTTON, Items.POLISHED_BLACKSTONE_BUTTON);
			addIdsToItemTag(BLOCK_BRONZE, alloyed("bronze_block"));
			addIdsToItemTag(BLOCK_STEEL, alloyed("steel_block"));
			addIdsToItemTag(BLOCK_CAST_IRON, createdeco("cast_iron_block"));
		}
	}
		
	public static void register() {
		BlockCBC.sectionRegister();
		ItemCBC.sectionRegister();
	}
	
	private static ResourceLocation alloyed(String path) { return new ResourceLocation("alloyed", path); }
	private static ResourceLocation createdeco(String path) { return new ResourceLocation("createdeco", path); }
	
}
