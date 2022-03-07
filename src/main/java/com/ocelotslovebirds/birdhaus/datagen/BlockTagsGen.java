package com.ocelotslovebirds.birdhaus.datagen;

import com.ocelotslovebirds.birdhaus.Core;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagsGen extends BlockTagsProvider {

    /**
     * Generates a .json of block tags.
     *
     * @param generator The datagenerator being used.
     * @param helper    The existing file helper from forge being passed.
     */
    public BlockTagsGen(DataGenerator generator, ExistingFileHelper helper) {
        super(generator, Core.MODID, helper);
    }

    /**
     * Possibility to add a tag to a block. Unused. BlockEntity does a better job of implementing complex behaviors.
     */
    @Override
    protected void addTags() {
        tag(BlockTags.PLANKS)
                .add(Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.BIRCH_PLANKS, Blocks.JUNGLE_PLANKS,
                        Blocks.ACACIA_PLANKS, Blocks.DARK_OAK_PLANKS, Blocks.CRIMSON_PLANKS, Blocks.WARPED_PLANKS);
        tag(BlockTags.LOGS)
                .add(Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.BIRCH_LOG, Blocks.JUNGLE_LOG,
                        Blocks.ACACIA_LOG, Blocks.DARK_OAK_LOG, Blocks.CRIMSON_STEM, Blocks.WARPED_STEM);
    }

    /**
     * @return Get the name of the tag file.
     */
    @Override
    public String getName() {
        return "birdhaustags";
    }
}
