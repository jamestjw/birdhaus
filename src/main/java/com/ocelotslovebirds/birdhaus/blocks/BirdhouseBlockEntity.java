package com.ocelotslovebirds.birdhaus.blocks;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.ocelotslovebirds.birdhaus.mobai.HangAroundBirdhouseGoal;
import com.ocelotslovebirds.birdhaus.setup.Registration;
import com.ocelotslovebirds.birdhaus.ticker.FixedIntervalTicker;
import com.ocelotslovebirds.birdhaus.ticker.Ticker;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.Tags;
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

    private Ticker tickerForBirdSpawns = new FixedIntervalTicker(150);
    private Ticker tickerForSeedConsumption = new FixedIntervalTicker(100);
    private Ticker tickerForBirdhouseGoalModification = new FixedIntervalTicker(100);

    // The block is active if it able to consume seeds
    private Boolean isActive = false;
    private AABB birdHouseBB = new AABB(
        this.getBlockPos().getX() - 10,
        this.getBlockPos().getY(),
        this.getBlockPos().getZ() - 10,
        this.getBlockPos().getX() + 10,
        this.getBlockPos().getY() + 10,
        this.getBlockPos().getZ() + 10
        );
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

    /**
     * This is the main loop for the birdhouse.
     * It ticks all the events that should be happening
     * after a certain interval.
     */

    public void tickServer() {
        handleSeedsForTick();
        handleBirdSpawnForTick();
        handleParrotGoalForTick();
    }

    /*
     * @param xOffset  x offset (East-West axis) at which to spawn the bird
     * @param yOffset  y offset (Up-Down axis) at which to spawn the bird
     * @param yOffset  z offset (North-South axis) at which to spawn the bird
     * All offsets are relative to the birdhouse
    */
    private void spawnNewBird(int xOffset, int yOffset, int zOffset) {
        // Dirty type casting because it works
        ServerLevel lvl = (ServerLevel) this.getLevel();
        Parrot newBird = new Parrot(EntityType.PARROT, lvl);
        lvl.addFreshEntity(newBird);
        BlockPos desBlockPos = this.getBlockPos().offset(xOffset, yOffset, zOffset);

        // Move the spawned bird to the destination
        // 0.0 because it works, not sure what this supposed to be
        newBird.moveTo(desBlockPos, (float) 0.0, (float) 0.0);
    }

    private void handleBirdSpawnForTick() {
        if (tickerForBirdSpawns.tick()) {
            // Random x offset
            int xOffset = ThreadLocalRandom.current().nextInt(-20, 20);
            // Spawn bird 10 units higher than the birdhouse
            int yOffset = this.getBlockPos().getY() + 10;
            // Random z offset
            int zOffset = ThreadLocalRandom.current().nextInt(-20, 20);
            spawnNewBird(xOffset, yOffset, zOffset);

        }
    }

    private void handleParrotGoalForTick() {
        if (tickerForBirdhouseGoalModification.tick()) {
            if (this.isActive) {
                applyBirdHouseGoalToSurroundingParrots();
            } else {
                removeBirdHouseGoalToSurroundingParrots();
            }
        }
    }

    /**
     * Applies the "HangingAroundBirdhouseGoal" to birds in the
     * birdhouse bounding box.
     *
     * TODO: Make this threadsafe.
     */
    private void applyBirdHouseGoalToSurroundingParrots() {
        List<Parrot> parrots = this.level.getEntitiesOfClass(Parrot.class, this.birdHouseBB);
        for (Parrot temp:parrots) {
            // Check to see if they already have the goal applied. If not, apply it.
            if (!temp.goalSelector.getAvailableGoals().stream()
                .anyMatch(wg -> wg.getGoal() instanceof HangAroundBirdhouseGoal)) {
                temp.goalSelector.addGoal(0, new HangAroundBirdhouseGoal(temp, 1.0, 60, false, this.getBlockPos()));
            }
        }
    }

    /**
     * Removes the "HangingAroundBirdhouseGoal" to birds in the
     * birdhouse bounding box.
     *
     * TODO: Make this threadsafe.
     */
    private void removeBirdHouseGoalToSurroundingParrots() {
        List<Parrot> parrots = this.level.getEntitiesOfClass(Parrot.class, this.birdHouseBB);
        for (Parrot temp:parrots) {
            // Check to see if they already have the goal applied. If not, apply it.
            if (temp.goalSelector.getAvailableGoals().stream()
                .anyMatch(wg -> wg.getGoal() instanceof HangAroundBirdhouseGoal)) {
                Goal toRemove = null;
                for (WrappedGoal g : temp.goalSelector.getAvailableGoals()) {
                    if (g.getGoal() instanceof HangAroundBirdhouseGoal) {
                        toRemove = g.getGoal();
                    }
                }
                if (toRemove != null) {
                    temp.goalSelector.removeGoal(toRemove);
                }
            }
        }
    }

    private void handleSeedsForTick() {
        // Tick the birdhouse to check it needs to consume a seed
        if (tickerForSeedConsumption.tick()) {
            // If a seed was successfully extracted from the birdhouse
            if (!itemHandler.extractItem(0, 1, false).isEmpty()) {
                // Check if the state of the BHouse is not active
                if (!this.isActive) {
                    // If not active, set to active
                    this.isActive = true;
                }
            } else { // If nothing was extracted from the BHouse (inventory was empty)
                // Set BHouse state to false
                this.isActive = false;
            }
        }

        // Update the block state if there was a change
        BlockState blockState = getBlockState();
        if (blockState.getValue(BlockStateProperties.CONDITIONAL) != this.isActive) {
            level.setBlock(
                worldPosition,
                blockState.setValue(BlockStateProperties.CONDITIONAL, this.isActive),
                Block.UPDATE_ALL
            );
        }
    }

    /**
     * @param tag The compoundtag is the blockentity tag that allow for specific load/save instructions.
     */
    // Important Note: Do -not- override save.
    // Override "saveAdditional" instead unless you are extremely sure of what you are doing.
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
             * @return If the item is valid.
             */
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return stack.is(Tags.Items.SEEDS);
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

