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
     * Creates a frame stub model and registers it as belonging to the birdhouse, then creates a complex interior.
     */
    private void registerBirdhouseStub() {
        BlockModelBuilder frame = models().getBuilder("block/birdhouse/main");
        frame.parent(models().getExistingFile(mcLoc("cube")));

        floatingCube(frame, 0f, 0f, 0f, 1f, 16f, 1f);
        floatingCube(frame, 15f, 0f, 0f, 16f, 16f, 1f);
        floatingCube(frame, 0f, 0f, 15f, 1f, 16f, 16f);
        floatingCube(frame, 15f, 0f, 15f, 16f, 16f, 16f);

        floatingCube(frame, 1f, 0f, 0f, 15f, 1f, 1f);
        floatingCube(frame, 1f, 15f, 0f, 15f, 16f, 1f);
        floatingCube(frame, 1f, 0f, 15f, 15f, 1f, 16f);
        floatingCube(frame, 1f, 15f, 15f, 15f, 16f, 16f);

        floatingCube(frame, 0f, 0f, 1f, 1f, 1f, 15f);
        floatingCube(frame, 15f, 0f, 1f, 16f, 1f, 15f);
        floatingCube(frame, 0f, 15f, 1f, 1f, 16f, 15f);
        floatingCube(frame, 15f, 15f, 1f, 16f, 16f, 15f);

        floatingCube(frame, 1f, 1f, 1f, 15f, 15f, 15f);

        frame.texture("window", modLoc("block/birdhouse_window"));
        frame.texture("particle", modLoc("block/birdhouse"));

        createBirdhouseStubModel(Registration.BIRDHOUSE_BLOCK.get(), frame);
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
    private void createBirdhouseStubModel(BirdhouseBlock block, BlockModelBuilder frame) {

        BlockModelBuilder singleOff = models().getBuilder("block/birdhouse/singleoff")
                .element().from(3, 3, 3).to(13, 13, 13).face(Direction.DOWN).texture("#single").end()
                .end().texture("single", modLoc("block/birdhouse_off"));
        BlockModelBuilder singleOn = models().getBuilder("block/birdhouse/singleon")
                .element().from(3, 3, 3).to(13, 13, 13).face(Direction.DOWN).texture("#single").end()
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
     */
    private void floatingCube(BlockModelBuilder builder, float fx, float fy, float fz, float tx, float ty, float tz) {
        builder.element()
                .from(fx, fy, fz)
                .to(tx, ty, tz)
                .allFaces((direction, faceBuilder) -> faceBuilder.texture("#window"))
                .end();
    }

}
