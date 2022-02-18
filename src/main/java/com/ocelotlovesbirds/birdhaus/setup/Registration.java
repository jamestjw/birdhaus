package com.ocelotlovesbirds.birdhaus.setup;

import com.ocelotlovesbirds.birdhaus.Core;
import com.ocelotlovesbirds.birdhaus.blocks.BirdhouseBlock;
import com.ocelotlovesbirds.birdhaus.blocks.BirdhouseBlockEntity;
import com.ocelotlovesbirds.birdhaus.blocks.BirdhouseContainer;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registration {
    //<editor-fold desc="Variables and Item Registrations">
    private static final DeferredRegister<Block> blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, Core.MODID);
    private static final Item.Properties itemProperties = new Item.Properties().tab(ModSetup.ITEM_GROUP);
    public static final RegistryObject<BirdhouseBlock> BIRDHOUSE_BLOCK = blocks.register("birdhouse",
            BirdhouseBlock::new);

    private static final DeferredRegister<Item> items = DeferredRegister.create(ForgeRegistries.ITEMS, Core.MODID);
    private static final DeferredRegister<BlockEntityType<?>> blockEntities = DeferredRegister.create(
            ForgeRegistries.BLOCK_ENTITIES, Core.MODID);
    private static final DeferredRegister<MenuType<?>> containers = DeferredRegister.create(ForgeRegistries.CONTAINERS,
            Core.MODID);

    public static final RegistryObject<MenuType<BirdhouseContainer>> BIRDHOUSE_CONTAINER = containers.register(
            "birdhouse", () -> IForgeMenuType.create((windowId, inv, data) ->
                    new BirdhouseContainer(windowId, data.readBlockPos(), inv, inv.player)));

    public static final RegistryObject<Item> BIRDHOUSE_ITEM = fromBlock(BIRDHOUSE_BLOCK);
    public static final RegistryObject<BlockEntityType<BirdhouseBlockEntity>> BIRDHOUSE_BLOCK_ENTITY =
            blockEntities.register("birdhouse", () ->
                    BlockEntityType.Builder.of(BirdhouseBlockEntity::new, BIRDHOUSE_BLOCK.get()).build(null));
    //</editor-fold>
    //<editor-fold desc="Public Functions">
    /**
     * The init function registers all added blocks and items to the mod event bus, meaning the game will recognize
     * them and load them upon startup.
     */
    public static void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        blocks.register(bus);
        items.register(bus);
        blockEntities.register(bus);
        containers.register(bus);
    }

    /**
     * A quick helper method used from Jorrit's mod that makes generating an item from a block more concise.
     *
     * @param block The registered block that an item is needed for.
     * @param <B>   The block passed must either be a block or extend block.
     * @return Return the item generated from the block.
     */
    public static <B extends Block> RegistryObject<Item> fromBlock(RegistryObject<B> block) {
        return items.register(block.getId().getPath(), () -> new BlockItem(block.get(), itemProperties));
    }
    //</editor-fold>
}
