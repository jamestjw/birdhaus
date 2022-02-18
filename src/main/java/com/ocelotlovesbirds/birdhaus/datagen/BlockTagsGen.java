package com.ocelotlovesbirds.birdhaus.datagen;

import com.ocelotlovesbirds.birdhaus.Core;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
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
    }
}
