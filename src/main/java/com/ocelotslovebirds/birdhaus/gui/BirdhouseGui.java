package com.ocelotslovebirds.birdhaus.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.ocelotslovebirds.birdhaus.Core;
import com.ocelotslovebirds.birdhaus.blocks.BirdhouseContainer;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BirdhouseGui extends AbstractContainerScreen<BirdhouseContainer> {

    private final ResourceLocation gui = new ResourceLocation(Core.MODID, "textures/gui/birdhouse_gui.png");

    /**
     * @param pMenu            The owner block of the menu.
     * @param pPlayerInventory The player inventory being opened.
     * @param pTitle           The component behavior being interacted with.
     */
    public BirdhouseGui(BirdhouseContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    /**
     * @param pPoseStack   The background of the inventory.
     * @param mouseX       Mouse x position.
     * @param mouseY       Mouse y position.
     * @param partialTicks Update if the GUI is using a different ticker.
     */
    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(pPoseStack, mouseX, mouseY);
    }

    /**
     * Renders the birdhouse text on the menu window.
     *
     * @param pPoseStack The background of the inventory window.
     * @param mouseX     Mouse x position.
     * @param mouseY     Mouse y position.
     */
    @Override
    protected void renderLabels(PoseStack pPoseStack, int mouseX, int mouseY) {
        this.font.draw(pPoseStack, "Birdhouse", 66, 10, 0x404040);
        this.font.draw(pPoseStack, "Inventory", 10, 57, 0x404040);
    }

    /**
     * @param pPoseStack  The background being rendered.
     * @param partialTick Update if the GUI is using a different ticker.
     * @param mouseX      Mouse X position.
     * @param mouseY      Mouse Y position.
     */
    @Override
    protected void renderBg(PoseStack pPoseStack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, gui);
        int relX = (this.width - this.imageWidth) / 2;
        int relY = (this.height - this.imageHeight) / 2;
        this.blit(pPoseStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
    }
}
