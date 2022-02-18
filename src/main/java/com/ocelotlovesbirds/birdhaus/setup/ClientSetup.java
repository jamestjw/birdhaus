package com.ocelotlovesbirds.birdhaus.setup;

import com.ocelotlovesbirds.birdhaus.gui.BirdhouseGui;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


public class ClientSetup {

    /**
     * Sets up the client side elements such as the GUI and rendering.
     * This should not be used to set up anything that multiple players will interact with, but instead things that
     * only are seen by one player's screen. Anything that effects multiple players should go in the specific entity
     * behavior.
     *
     * @param event The setup event when the game is loading.
     */
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(Registration.BIRDHOUSE_CONTAINER.get(), BirdhouseGui::new));
        ItemBlockRenderTypes.setRenderLayer(Registration.BIRDHOUSE_BLOCK.get(), RenderType.translucent());
    }
}
