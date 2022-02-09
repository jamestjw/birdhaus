package com.droubabanana.birdhouseMod.core.init;

import com.droubabanana.birdhouseMod.Birdhouse;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BirdhouseModItemInit {
	
	private BirdhouseModItemInit() {}
	
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Birdhouse.MODID);
	
	public static final RegistryObject<Item> EXAMPLE_BIRDHOUSE = ITEMS.register("example_birdhouse", 
			() -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
	
}
