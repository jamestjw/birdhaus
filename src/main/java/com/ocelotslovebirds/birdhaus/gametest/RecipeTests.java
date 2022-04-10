package com.ocelotslovebirds.birdhaus.gametest;

import com.ocelotslovebirds.birdhaus.Core;
import com.ocelotslovebirds.birdhaus.setup.Registration;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@PrefixGameTestTemplate(false)
@GameTestHolder(Core.MODID)
public class RecipeTests {
    @GameTest(template = "empty")
    public static void recipeExists(final GameTestHelper test) {
        ResourceLocation recipeLoc = new ResourceLocation("birdhaus", "birdhouse");

        var recipe = test.getLevel().getRecipeManager().byKey(recipeLoc);
        if (recipe.isPresent()) {
            if (recipe.get().getResultItem().is(Registration.BIRDHOUSE_ITEM.get())) {
                test.succeed();
            } else {
                test.fail("Recipe Test Failed: Recipe exists but does NOT create birdhouse item.");
            }
        } else {
            test.fail("Recipe Existence Test Failed: Recipe does not exist.");
        }
    }
}
