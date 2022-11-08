package com.ocelotslovebirds.birdhaus.gametest;

import com.ocelotslovebirds.birdhaus.Core;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@PrefixGameTestTemplate(false)
@GameTestHolder(Core.MODID)
public class BasicTests {
    /**
     * The template test is a simple check to make sure the template exists and will auto-halt the tests if it doesn't.
     * The function is not particularly insightful, but more like a canary-in-a-coal mine, if this one goes bad
     * the rest of the tests can be assumed to fail.
     * @param helper The gameTest helper will be automatically passed by the runGameTestServer.
     */
    @GameTest(template = "empty3x3x3")
    public static void templateTest(final GameTestHelper helper) {
        helper.succeed();
    }



}
