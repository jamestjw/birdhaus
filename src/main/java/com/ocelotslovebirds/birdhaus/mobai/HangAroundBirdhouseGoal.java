package com.ocelotslovebirds.birdhaus.mobai;

import com.ocelotslovebirds.birdhaus.blocks.BirdhouseBlock;
import com.ocelotslovebirds.birdhaus.ticker.FixedIntervalTicker;
import com.ocelotslovebirds.birdhaus.ticker.Ticker;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.phys.Vec3;

public class HangAroundBirdhouseGoal extends WaterAvoidingRandomFlyingGoal {

    private Vec3 bHousePosVec;
    private BlockPos bHouseBlockPos; // keep the raw blockpos for use with birdhouseblockentity functions
    // Low tick value as the getPosition func. not called on every tick
    private Ticker verifyBHouseTicker = new FixedIntervalTicker(5);

    /**
     * Constructor for the parrot goal. Makes parrots stay
     * around a birdhouse when it has seeds in its internal inventory.
     * @param pMob the mob getting this goal
     * @param pSpeedModifier modifies mob move speed
     * @param pInterval interval to check if the goal should be sought
     * @param pCheckNoActionTime boolean to check to see if the mob has not been performing an action
     * @param bHousePos position of the birdhouse passing the goal to the mob
     */
    public HangAroundBirdhouseGoal(PathfinderMob pMob, double pSpeedModifier,
        int pInterval, boolean pCheckNoActionTime, BlockPos bHousePos) {
        super(pMob, pSpeedModifier);
        this.bHouseBlockPos = bHousePos;
        this.bHousePosVec = new Vec3(bHousePos.getX(), bHousePos.getY(), bHousePos.getZ());
    }

    /**
     * Returns the position for the parrot to move to. This will be the position of the birdhouse that gave it the goal.
     * If for some reason, the bird is left with this goal with no birdhouse in sight, it will default to behaving like
     * "RandomWanderGoal".
     */
    @Override
    protected Vec3 getPosition() {
        if (this.bHousePosVec != null) {
            this.maybeCleanBhouse();
            return this.bHousePosVec;
        } else {
            return super.getPosition();
        }
    }

    /**
     * Checks if the birdhouse linked to the parrot still exists, if it doesn't, it
     * calls the routine used to clear the birdhouses position.
     */
    private void maybeCleanBhouse() {
        if (this.verifyBHouseTicker.tick()) {
            if (!this.bhouseStillExists()) {
                this.forgetBirdHouse();
            }
        }
    }

    /**
     * @return returns true if the birdhouse the parrot is following is still there, false otherwise
     */
    private boolean bhouseStillExists() {
        if (this.mob.level.getBlockState(this.bHouseBlockPos).getBlock() instanceof BirdhouseBlock) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Clear the birdhouse linked to this goal and have it ready to be assigned to a different location
     */
    private void forgetBirdHouse() {
        this.bHouseBlockPos = null;
        this.bHousePosVec = null;
    }

    public BlockPos getBhouseBlockPos() {
        return this.bHouseBlockPos;
    }

    public void setBhouseLoc(BlockPos newPos) {
        this.bHouseBlockPos = newPos;
        this.bHousePosVec = new Vec3(newPos.getX(), newPos.getY(), newPos.getZ());
    }

}
