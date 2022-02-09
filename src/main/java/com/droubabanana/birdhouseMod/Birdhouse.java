package com.droubabanana.birdhouseMod;

import com.droubabanana.birdhouseMod.core.init.BirdhouseModItemInit;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Birdhouse.MODID)
public class Birdhouse {
	public static final String MODID = "birdhousemod";
	
	public Birdhouse() {
		var bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		BirdhouseModItemInit.ITEMS.register(bus);
		
		
	}
}
