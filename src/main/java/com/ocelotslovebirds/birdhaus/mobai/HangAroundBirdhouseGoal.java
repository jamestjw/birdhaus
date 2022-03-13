package com.ocelotslovebirds.birdhaus.mobai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;


public class HangAroundBirdhouseGoal extends RandomStrollGoal {

    private final boolean checkNoActionTime;
    private BlockPos bHousePos;
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
        this.checkNoActionTime = pCheckNoActionTime;
        this.bHousePos = bHousePos;
        this.bHousePosVec = new Vec3(this.bHousePos.getX(), this.bHousePos.getY(), this.bHousePos.getZ());
    }

    @Override
    public boolean canUse() {
        if (this.mob.isVehicle()) {
            return false;
        } else {
            if (!this.forceTrigger) {
                if (this.checkNoActionTime && this.mob.getNoActionTime() >= 100) {
                    return false;
                }
                if (this.mob.getRandom().nextInt(reducedTickDelay(this.interval)) != 0) {
                    return false;
                }
            }
            Vec3 vec3 = this.getPosition();
            if (vec3 == null) {
                return false;
            } else {
                this.wantedX = vec3.x;
                this.wantedY = vec3.y;
                this.wantedZ = vec3.z;
                this.forceTrigger = false;
                return true;
            }
        }
    }

    /**
     * Returns the position for the parrot to move to. This will be the position of the birdhouse that gave it the goal.
     * If for some reason, the bird is left with this goal with no birdhouse in sight, it will default to behaving like
     * "RandomWanderGoal".
     */
    @Override
       protected Vec3 getPosition() {
        if (this.bHousePos != null) {
            return this.bHousePosVec;
        } else {
            return DefaultRandomPos.getPos(this.mob, 7, 4);
        }
    }
}
