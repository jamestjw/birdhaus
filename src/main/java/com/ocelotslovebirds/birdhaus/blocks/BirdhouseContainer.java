package com.ocelotslovebirds.birdhaus.blocks;

import com.ocelotslovebirds.birdhaus.setup.Registration;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class BirdhouseContainer extends AbstractContainerMenu {

    private final BlockEntity blockEntity;
    private final Player playerEntity;
    private final IItemHandler playerInventory;

    /**
     * @param windowId        The window ID that the inventory window will be opened in.
     * @param position        The position of the block that serves as the container.
     * @param playerInventory The interacting players' inventory.
     * @param player          The interacting player.
     */
    //
    public BirdhouseContainer(int windowId, BlockPos position, Inventory playerInventory, Player player) {
        super(Registration.BIRDHOUSE_CONTAINER.get(), windowId);
        blockEntity = player.getCommandSenderWorld().getBlockEntity(position);
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        if (blockEntity != null) {
            blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> addSlot(
                    new SlotItemHandler(h, 0, 64, 24)));
        }
        layoutPlayerInventorySlots(10, 70);
    }

    /**
     * @param playerIn The player interacting.
     * @param index    The inventory slot index being interacted with.
     * @return The item stack from the inventory slot.
     */
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (index == 0) {
                if (!this.moveItemStackTo(stack, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, itemstack);
            } else {
                if (index < 28) {
                    if (!this.moveItemStackTo(stack, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 37 && !this.moveItemStackTo(stack, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }
        return itemstack;
    }

    /**
     * @param pPlayer The interacting player.
     * @return Boolean if the player's interaction is still valid.
     */
    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), playerEntity,
                Registration.BIRDHOUSE_BLOCK.get());
    }

    /**
     * @param leftCol The position in the window of the left most column of the inventory box.
     * @param topRow  The position of the topmost row in the window.
     */
    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    /**
     * @param handler The Item handler used to create an inventory slot.
     * @param index   The current index of the slots to be created.
     * @param x       The x position of the slot's first position.
     * @param y       The y position of the slot's first position.
     * @param amount  Number of slots to add.
     * @param dx      Distance between the slots.
     * @return Return the number of slots cumulatively added to the whole inventory
     */
    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    /**
     * @param handler The Item handler used to create an inventory slot.
     * @param index   The current index of the slots to be created.
     * @param x       The x position of the slot's first position.
     * @param y       The y position of the slot's first position.
     * @param hamount Number of boxes horizontally, i.e. number of columns
     * @param dx      X-Distance between boxes.
     * @param vamount Vertical number of boxes, i.e. number of rows
     * @param dy      Y-distance between boxes.
     * @return Return the final index of the last slot in the added inventory.
     */
    private int addSlotBox(IItemHandler handler, int index, int x, int y, int hamount, int dx, int vamount, int dy) {
        for (int j = 0; j < vamount; j++) {
            index = addSlotRange(handler, index, x, y, hamount, dx);
            y += dy;
        }
        return index;
    }
}
