package com.ocelotslovebirds.birdhaus.datagen;

import com.ocelotslovebirds.birdhaus.Core;
import com.ocelotslovebirds.birdhaus.blocks.BirdhouseBlock;
import com.ocelotslovebirds.birdhaus.setup.Registration;

import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStatesGen extends BlockStateProvider {
    public BlockStatesGen(DataGenerator gen, ExistingFileHelper helper) {
        super(gen, Core.MODID, helper);
    }

    /**
     * The basic model registration. Add additional models or test model registration here.
     */
    // Initial Block data is just a simple block. Will be more complicated later.
    @Override
    protected void registerStatesAndModels() {
        registerBirdhouseStub();
    }


    /**
     * Registers the BirdhouseStub as belonging to the birdhouse.
     */
    private void registerBirdhouseStub() {

        createFrameBirdhouseStub("wood_light", "block/stripped_oak_log", "wood_dark", "block/stripped_dark_oak_log");

    }

    /**
     * Creates a frame stub model, then creates a complex interior.
     *
     * @param lightTextureName Texture to be used for the lighter areas of the birdhouse
     * @param lightTexturePath Address where the light texture file is saved
     * @param darkTextureName Texture to be used for the darker areas of the birdhouse
     * @param darkTexturePath Address where the dark texture file is saved
     */

    private void createFrameBirdhouseStub(String lightTextureName, String lightTexturePath, String darkTextureName,
    		String darkTexturePath) {
        BlockModelBuilder frame = models().getBuilder("block/birdhouse/main");
        frame.parent(models().getExistingFile(mcLoc("cube")));
        frame.texture(lightTextureName, modLoc(lightTexturePath));
        frame.texture(darkTextureName, modLoc(darkTexturePath));

        floatingCube(frame, 3f, 0f, 2f, 13f, 3f, 12f, "#" + darkTextureName); //base
        floatingCube(frame, 4f, 3f, 2f, 5f, 4f, 3f, "#" + darkTextureName); //front perch leg 1
        floatingCube(frame, 11f, 3f, 2f, 12f, 4f, 3f, "#" + darkTextureName); //front perch leg 2
        floatingCube(frame, 3f, 4f, 2f, 13f, 5f, 3f, "#" + darkTextureName); //front perch
        floatingCube(frame, 3f, 3f, 4f, 13f, 6f, 12f, "#" + lightTextureName); //house base
        floatingCube(frame, 3f, 6f, 4f, 6f, 10f, 12f, "#" + lightTextureName); //house wall 1
        floatingCube(frame, 10f, 6f, 4f, 13f, 10f, 12f, "#" + lightTextureName); //house wall 2
        floatingCube(frame, 3f, 10f, 4f, 13f, 11f, 12f, "#" + lightTextureName); //house top
        floatingCube(frame, 6f, 6f, 11f, 10f, 10f, 12f, "#" + lightTextureName); //house back wall
        floatingCube(frame, 0f, 5f, 7f, 3f, 6f, 8f, "#" + darkTextureName); //side perch 1
        floatingCube(frame, 13f, 5f, 7f, 16f, 6f, 8f, "#" + darkTextureName); //side perch 2
        floatingCube(frame, 4f, 11f, 4f, 12f, 12f, 12f, "#" + lightTextureName); //roof 1
        floatingCube(frame, 5f, 12f, 4f, 11f, 13f, 12f, "#" + lightTextureName); //roof 2
        floatingCube(frame, 6f, 13f, 4f, 10f, 14f, 12f, "#" + lightTextureName); //roof 3
        floatingCube(frame, 7f, 14f, 4f, 9f, 15f, 12f, "#" + lightTextureName); //roof 4
        floatingCube(frame, 6f, 15f, 4f, 10f, 16f, 12f, "#" + darkTextureName); //roof shingles 1
        floatingCube(frame, 5f, 14f, 4f, 7f, 15f, 12f, "#" + darkTextureName); //roof shingles 2
        floatingCube(frame, 4f, 13f, 4f, 6f, 14f, 12f, "#" + darkTextureName); //roof shingles 3
        floatingCube(frame, 3f, 12f, 4f, 5f, 13f, 12f, "#" + darkTextureName); //roof shingles 4
        floatingCube(frame, 2f, 11f, 4f, 4f, 12f, 12f, "#" + darkTextureName); //roof shingles 5
        floatingCube(frame, 1f, 10f, 4f, 3f, 11f, 12f, "#" + darkTextureName); //roof shingles 6
        floatingCube(frame, 9f, 14f, 4f, 11f, 15f, 12f, "#" + darkTextureName); //roof shingles 7
        floatingCube(frame, 10f, 13f, 4f, 12f, 14f, 12f, "#" + darkTextureName); //roof shingles 8
        floatingCube(frame, 11f, 12f, 4f, 13f, 13f, 12f, "#" + darkTextureName); //roof shingles 9
        floatingCube(frame, 12f, 11f, 4f, 14f, 12f, 12f, "#" + darkTextureName); //roof shingles 10
        floatingCube(frame, 13f, 10f, 4f, 15f, 11f, 12f, "#" + darkTextureName); //roof shingles 11

        //frame.texture("particle", modLoc("block/birdhouse"));

        createComplexBirdhouseStubModel(Registration.BIRDHOUSE_BLOCK.get(), frame);
    }

    /**
     * Creates a complex stub model that is capabable of changing appearance based on two states and animates the
     * active state.
     * <p>
     * Creates a complex stub birdhouse.
     *
     * @param block The block being created
     * @param frame The frame that the stub is created in.
     */
    private void createComplexBirdhouseStubModel(BirdhouseBlock block, BlockModelBuilder frame) {

        BlockModelBuilder singleOff = models().getBuilder("block/birdhouse/singleoff")
                .element().from(5, 5, 5).to(11, 11, 11).face(Direction.DOWN).texture("#single").end()
                .end().texture("single", modLoc("block/birdhouse_off"));
        BlockModelBuilder singleOn = models().getBuilder("block/birdhouse/singleon")
                .element().from(5, 5, 5).to(11, 11, 11).face(Direction.DOWN).texture("#single").end()
                .end().texture("single", modLoc("block/birdhouse_on"));

        MultiPartBlockStateBuilder bld = getMultipartBuilder(block);
        bld.part().modelFile(frame).addModel();

        BlockModelBuilder[] models = new BlockModelBuilder[]{singleOff, singleOn};
        for (int i = 0; i < 2; i++) {
            boolean conditional = i == 1;
            bld.part().modelFile(models[i]).addModel().condition(BlockStateProperties.CONDITIONAL, conditional);
            bld.part().modelFile(models[i]).rotationX(180).addModel()
                    .condition(BlockStateProperties.CONDITIONAL, conditional);
            bld.part().modelFile(models[i]).rotationX(90).addModel()
                    .condition(BlockStateProperties.CONDITIONAL, conditional);
            bld.part().modelFile(models[i]).rotationX(270).addModel()
                    .condition(BlockStateProperties.CONDITIONAL, conditional);
            bld.part().modelFile(models[i]).rotationY(90).rotationX(90).addModel()
                    .condition(BlockStateProperties.CONDITIONAL, conditional);
            bld.part().modelFile(models[i]).rotationY(270).rotationX(90).addModel()
                    .condition(BlockStateProperties.CONDITIONAL, conditional);
        }
    }

    /**
     * Easy method for adding a basic rectangle stub. It creates a cube based on two vertexes where a line is
     * horizontally drawn between the two and the other points extrapolated from them.
     *
     * @param builder The model builder.
     * @param fx      floor x
     * @param fy      floor y
     * @param fz      floor x
     * @param tx      top x
     * @param ty      top y
     * @param tz      top z
     * @param texture value to pass on as texture
     */
    private void floatingCube(BlockModelBuilder builder, float fx, float fy, float fz, float tx, float ty, float tz,
    		String texture) {
        builder.element()
                .from(fx, fy, fz)
                .to(tx, ty, tz)
                .allFaces((direction, faceBuilder) -> faceBuilder.texture(texture))
                .end();
    }

}
