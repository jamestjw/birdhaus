package com.ocelotlovesbirds.birdhaus.blocks;

import com.ocelotlovesbirds.birdhaus.setup.Registration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * The birdhouse block entity describes the behavior of the birdhouse block and serves as an easy way to separate
 * the physical block in the game from the behavior of the block.
 */
public class BirdhouseBlockEntity extends BlockEntity {

    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    private int counter;

    /**
     * @param pos   Position of the block.
     * @param state State of the block being created.
     */
    public BirdhouseBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.BIRDHOUSE_BLOCK_ENTITY.get(), pos, state);
    }

    /**
     * setRemoved is necessary for cleanly removing the block from the world and preventing leaks.
     */
    @Override
    public void setRemoved() {
        super.setRemoved();
        handler.invalidate();
    }

    /**
     * Instructs the game how to load and save the items.
     *
     * @param tag The compoundtag is the blockentity tags that allow for specific load/save instructions.
     */
    @Override
    public void load(CompoundTag tag) {
        if (tag.contains("Inventory")) {
            itemHandler.deserializeNBT(tag.getCompound("Inventory"));
        }
        super.load(tag);
    }


    //TODO: Hook in bird spawning into the Entity ticker.

    /**
     * This is the main loop for the birdhouse. It needs to be improved to allow for the spawning of birds however at
     * the moment it works well for
     * 1. Extracting seeds from the inventory stack,
     * 2. Reacting to seeds being placed in the stack.
     * 3. Creates a ticker that can be used to time bird spawns eventually.
     */

    public void tickServer() {
        if (counter > 0) {
            counter--;
            setChanged();
        }
        if (counter <= 0) {
            if (!itemHandler.extractItem(0, 1, false).isEmpty()) {
                int counterTime = 100;
                counter = counterTime;
                setChanged();
            }
        }

        //When adding seeds, if the seeds are "burnable" then set the block to active.
        BlockState blockState = getBlockState();
        if (blockState.getValue(BlockStateProperties.CONDITIONAL) != counter > 0) {
            level.setBlock(worldPosition, blockState.setValue(BlockStateProperties.CONDITIONAL, counter > 0),
                    Block.UPDATE_ALL);
        }
    }

    /**
     * @param tag The compoundtag is the blockentity tag that allow for specific load/save instructions.
     */
    //Important Note: Do -not- override save.
    //Override "saveAdditional" instead unless you are extremely sure of what you are doing.
    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.put("Inventory", itemHandler.serializeNBT());
    }

    /**
     * @return Returns the item stack handler that will manage the blockentity's allowed items.
     */
    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {
            /** Notifies Minecraft that an item slot has been changed and marks it for serialization.
             * @param slot The inventory slot being changed.
             */
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            /**
             * @param slot The slot that the item is being dragged to.
             * @param stack The item stack being dragged to the item.
             * @return
             */
            //TODO: Expand this function to incorporate all things tagged as seeds. Do so by printing tags and
            // comparing those.
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return stack.getItem().getDescriptionId().equals("item.minecraft.wheat_seeds");
            }

            /**
             * @param slot The slot being inserted into.
             * @param stack The itemstack being inserted.
             * @param simulate Determines if the stack insertion should be simulated before the insertion takes place.
             *                 Serves to make sure the item is placeable in the location.
             * @return Return the item stack that results from the insertion. Returns nothing if an itemstack
             * is placed in an empty slot.
             */
            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    /**
     * @param cap  Capability being retrieved.
     * @param side The side being retrieved from. Currently unused.
     * @param <T>  The block/capability type.
     * @return The handler retrieved.
     */
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }
}

