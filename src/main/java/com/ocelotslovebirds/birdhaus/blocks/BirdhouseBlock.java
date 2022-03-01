package com.ocelotslovebirds.birdhaus.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class BirdhouseBlock extends Block implements EntityBlock {

    public static final String BIRDHOUSE_TEXT = "message.birdhouse";

    private static final VoxelShape renderShape = Shapes.box(
            0.1, 0.1, 0.1,
            0.5, 0.5, 0.5);


    /**
     * Basic constructor.
     */
    public BirdhouseBlock() {
        super(Properties.of(Material.WOOD).sound(SoundType.WOOD));
    }

    // Depreciation suppressed until I can figure out how to accomplish this in 1.18.
    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return renderShape;
    }

    /**
     * @param pPos   The position of the new block entity.
     * @param pState The state the new block entity is instantiated in.
     * @return The new block entity.
     */
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BirdhouseBlockEntity(pPos, pState);
    }

    /**
     * @param builder The State Definition builder that is passed to create the state definitions. Should be in Datagen.
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.CONDITIONAL);
    }

    /**
     * @param context Gets the default state that a block needs to be in for placement. At the moment it is false.
     * @return The block state.
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(BlockStateProperties.CONDITIONAL, false);
    }

    /**
     * @param level The level/world the block is in.
     * @param state The current state the block is in.
     * @param type  The block entity being passed.
     * @param <T>   The type of block entity.
     * @return The block entity's ticker.
     */
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
                                                                  BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof BirdhouseBlockEntity tile) {
                tile.tickServer();
            }
        };
    }

    /**
     * @param state  The state of the block.
     * @param level  The level/world of the block.
     * @param pos    The block position.
     * @param player The player interacting with the block.
     * @param hand   Unused for this particular instance. Reference to the player "hand" that is using the block.
     * @param trace  The data on the hit for the block. Unused so far.
     * @return The data of the interaction with the block. See InteractionResult for more details.
     */
    // Depreciation suppressed until I can figure out how to get this same functionality in 1.18.
    @SuppressWarnings("depreciation")
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult trace) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BirdhouseBlockEntity) {
                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return new TranslatableComponent(BIRDHOUSE_TEXT);
                    }


                    @Override
                    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
                        return new BirdhouseContainer(pContainerId, pos, pInventory, pPlayer);
                    }
                };
                NetworkHooks.openGui((ServerPlayer) player, containerProvider, be.getBlockPos());
            } else {
                throw new IllegalStateException("Named container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }
}
