package com.ocelotslovebirds.birdhaus.setup;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Fundamental mod setup, creates the tabs and auto-generates icons as needed.
 */
public class ModSetup {

    public static final String TAB_NAME = "birdhouse";

    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(TAB_NAME) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Registration.BIRDHOUSE_ITEM.get());
        }
    };

    public static void init(FMLCommonSetupEvent event) {
    }

}
