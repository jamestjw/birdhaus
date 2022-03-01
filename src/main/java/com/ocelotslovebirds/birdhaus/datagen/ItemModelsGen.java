package com.ocelotslovebirds.birdhaus.datagen;

import com.ocelotslovebirds.birdhaus.Core;
import com.ocelotslovebirds.birdhaus.setup.Registration;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;


public class ItemModelsGen extends ItemModelProvider {

    /**
     * @param generator          The data generator being used.
     * @param existingFileHelper The existing file helper from forge being used. Should be passed from the main data
     *                           generator.
     */
    public ItemModelsGen(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Core.MODID, existingFileHelper);
    }
    /**
     * Registers the models for the birdhouseBlock
     */
    @Override
    protected void registerModels() {
        withExistingParent(Registration.BIRDHOUSE_BLOCK.get().getRegistryName().getPath(), modLoc(
                "block/birdhouse/main"));
    }
}
