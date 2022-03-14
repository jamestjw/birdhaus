package com.ocelotslovebirds.birdhaus.mobai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;


public class HangAroundBirdhouseGoal extends RandomStrollGoal {

    private Vec3 bHousePosVec;

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
        super(pMob, pSpeedModifier, pInterval, pCheckNoActionTime);
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
            return this.bHousePosVec;
        } else {
            return super.getPosition();
        }
    }
}
