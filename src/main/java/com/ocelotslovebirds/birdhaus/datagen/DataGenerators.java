package com.ocelotslovebirds.birdhaus.datagen;

import com.ocelotslovebirds.birdhaus.Core;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

//Loosely based on the Jorrit Tyberghein Data Generator Convenience Class.

@Mod.EventBusSubscriber(modid = Core.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    /**
     * @param event The gather data parameter is supplied by minecraft when it runs.
     */
    //Calls the creation of data packages upon running the runData mode of Minecraft.
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        //Splits the items and blocks into server-side and client-side data packages depending on if a server or client
        //is firing the event. Doesn't matter for single player and in most cases both the client and server events
        //should be called.
        if (event.includeServer()) {
            generator.addProvider(new RecipesGen(generator));
            generator.addProvider(new LootTablesGen(generator));
            BlockTagsGen blockTags = new BlockTagsGen(generator, event.getExistingFileHelper());
            generator.addProvider(blockTags);
            generator.addProvider(new ItemTagsGen(generator, blockTags, event.getExistingFileHelper()));
        }
        if (event.includeClient()) {
            generator.addProvider(new BlockStatesGen(generator, event.getExistingFileHelper()));
            generator.addProvider(new ItemModelsGen(generator, event.getExistingFileHelper()));
            generator.addProvider(new LanguageProviderGen(generator, "en_us"));
        }
    }
}
