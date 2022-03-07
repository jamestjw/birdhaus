//Based on the Jorrit Tyberghein's Loot Table Convenience Class.

package com.ocelotslovebirds.birdhaus.datagen;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public abstract class BaseLootTableProvider extends LootTableProvider {

    private static final Logger logger = LogManager.getLogger();
    //Output check.
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    //Hash map houses the loot table when associated with the block.
    protected final Map<Block, LootTable.Builder> lootTables = new HashMap<>();
    private final DataGenerator generator;


    /**
     * @param dataGeneratorIn Data Generator being passed in.
     */
    public BaseLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
        this.generator = dataGeneratorIn;
    }

    protected abstract void addTables();

    //<editor-fold desc="Loot Table Generators">

    /**
     * Note: This class was part of the initial block-loot generation. Remains in the code because in case we need it
     * in initial development.
     *
     * @param name  The name of the item to create a recipe for. Use the ItemID name, not the language display name.
     * @param block The registered block that the recipe will use.
     * @param type  The block entity that is being used. This will allow for block entities to generate different loot
     *              based on state.
     * @return Returns the loot table builder that is turned into a loot table .json.
     */
    //This creates a standard loot table that creates entity-action-item loops.
    protected LootTable.Builder createStandardTable(String name, Block block, BlockEntityType<?> type) {
        LootPool.Builder builder = LootPool.lootPool()
                .name(name)
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(block)
                        .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                        .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                .copy("Info", "BlockEntityTag.Info", CopyNbtFunction.MergeStrategy.REPLACE)
                                .copy("Inventory", "BlockEntityTag.Inventory", CopyNbtFunction.MergeStrategy.REPLACE))
                        .apply(SetContainerContents.setContents(type)
                                .withEntry(DynamicLoot.dynamicEntry(new ResourceLocation("minecraft", "contents"))))
                );
        return LootTable.lootTable().withPool(builder);
    }

    /**
     * Note: This class was part of the initial block-loot generation. Remains in the code in case we need it in initial
     * development.
     *
     * @param name  The name of the item to create a recipe for. Use the ItemID name, not the language display name.
     * @param block The registered block that the recipe will use.
     * @return The simple loot table that returns for a 1-for-1 block action loop.
     */
    protected LootTable.Builder createSimpleTable(String name, Block block) {
        LootPool.Builder builder = LootPool.lootPool()
                .name(name)
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(block));
        return LootTable.lootTable().withPool(builder);
    }

    /**
     * @param name     The name of the item to create a recipe for. Use the ItemID name, not the language display name.
     * @param block    The registered block that the recipe will use.
     * @param lootItem The loot item that will result from breaking the block.
     * @param min      The minimum quantity of loot the block will generate.
     * @param max      The maximum amount of loot the block will generate.
     * @return The silk-touch compatible loot table generated. Will become the loot table .json.
     */
    protected LootTable.Builder createSilkTouchTable(String name, Block block, Item lootItem, float min, float max) {
        LootPool.Builder builder = LootPool.lootPool()
                .name(name)
                .setRolls(ConstantValue.exactly(1))
                .add(AlternativesEntry.alternatives(
                                LootItem.lootTableItem(block)
                                        .when(MatchTool.toolMatches(ItemPredicate.Builder.item()
                                                .hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH,
                                                        MinMaxBounds.Ints.atLeast(1))))),
                                LootItem.lootTableItem(lootItem)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                                        .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 1))
                                        .apply(ApplyExplosionDecay.explosionDecay())
                        )
                );
        return LootTable.lootTable().withPool(builder);
    }
    //</editor-fold>

    //<editor-fold desc="Run-Write Functions">
    @Override
    public void run(HashCache cache) {
        addTables();

        Map<ResourceLocation, LootTable> tables = new HashMap<>();
        for (Map.Entry<Block, LootTable.Builder> entry : lootTables.entrySet()) {
            tables.put(entry.getKey().getLootTable(), entry.getValue().setParamSet(LootContextParamSets.BLOCK).build());
        }
        writeTables(cache, tables);
    }

    private void writeTables(HashCache cache, Map<ResourceLocation, LootTable> tables) {
        Path outputFolder = this.generator.getOutputFolder();
        tables.forEach((key, lootTable) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
            try {
                DataProvider.save(GSON, cache, LootTables.serialize(lootTable), path);
            } catch (IOException e) {
                logger.error("Couldn't write loot table {}", path, e);
            }
        });
    }

    @Override
    public String getName() {
        return "Birdhaus LootTables";
    }
    //</editor-fold>

}
