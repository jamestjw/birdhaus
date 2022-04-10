package com.ocelotslovebirds.birdhaus.gametest;

import com.ocelotslovebirds.birdhaus.Core;
import com.ocelotslovebirds.birdhaus.setup.Registration;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@PrefixGameTestTemplate(false)
@GameTestHolder(Core.MODID)
public class BlockTests {
    @GameTest(template = "empty3x3x3")
    public static void sampleTest(GameTestHelper test) {
        BlockPos birdBlock = new BlockPos(1, 1, 1);

        test.setBlock(birdBlock, Registration.BIRDHOUSE_BLOCK.get());
        test.assertBlockState(birdBlock, b->b.is(Registration.BIRDHOUSE_BLOCK.get()), ()-> "Block not present");

        test.succeed();
    }
}
