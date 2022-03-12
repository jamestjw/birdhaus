package com.ocelotslovebirds.birdhaus.datagen;

import com.ocelotslovebirds.birdhaus.Core;
import com.ocelotslovebirds.birdhaus.setup.Registration;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemTagsGen extends ItemTagsProvider {

    public ItemTagsGen(DataGenerator generator, BlockTagsProvider blockTags, ExistingFileHelper helper) {
        super(generator, blockTags, Core.MODID, helper);
    }

    /**
     * Add tags to the registered item.
     */
    @Override
    protected void addTags() {
        tag(Tags.Items.CHESTS_WOODEN)
                .add(Registration.BIRDHOUSE_ITEM.get());
    }

    /**
     * @return Get the name of the tag file.
     */
    @Override
    public String getName() {
        return "Birdhaus Item Tags";
    }
}
