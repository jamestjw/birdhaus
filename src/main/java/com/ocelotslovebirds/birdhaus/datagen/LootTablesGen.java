package com.ocelotslovebirds.birdhaus.datagen;

import com.ocelotslovebirds.birdhaus.setup.Registration;

import net.minecraft.data.DataGenerator;

public class LootTablesGen extends BaseLootTableProvider {


    public LootTablesGen(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    /**
     * Create tables for the birdhouse, compatible with silk-touch tools.
     */
    @Override
    protected void addTables() {

        lootTables.put(Registration.BIRDHOUSE_BLOCK.get(), createSilkTouchTable("birdhouse",
                Registration.BIRDHOUSE_BLOCK.get(), Registration.BIRDHOUSE_ITEM.get(), 1, 1));
    }
}
