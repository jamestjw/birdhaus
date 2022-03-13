package com.ocelotslovebirds.birdhaus.mobai;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.ocelotslovebirds.birdhaus.Core;
import com.ocelotslovebirds.birdhaus.setup.Registration;
import com.ocelotslovebirds.birdhaus.blocks.BirdhouseBlock;
import com.ocelotslovebirds.birdhaus.blocks.BirdhouseBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.GoalUtils;

public class HangAroundBirdhouseGoal extends RandomStrollGoal {

    private double checkInterval;
    private final boolean checkNoActionTime;
    private BlockPos bHousePos;
    private Vec3 bHousePosVec;

    public HangAroundBirdhouseGoal(PathfinderMob pMob, double pSpeedModifier, int pInterval, boolean pCheckNoActionTime, BlockPos bHousePos) {
        super(pMob, pSpeedModifier, pInterval, pCheckNoActionTime);
        this.checkInterval = pInterval/3;
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

    @Override
    /**
     * Returns the position for the parrot to move to. This will be the position of the birdhouse that gave it the goal.
     * If for some reason, the bird is left with this goal with no birdhouse in sight, it will default to behaving like
     * "RandomWanderGoal".
     */
    protected Vec3 getPosition(){
        if(this.bHousePos != null) {
            return this.bHousePosVec;
        } else {
            return DefaultRandomPos.getPos(this.mob, 7, 4);
        }
        
    }
    

}
