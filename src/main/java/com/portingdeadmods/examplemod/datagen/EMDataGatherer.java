package com.portingdeadmods.examplemod.datagen;

import com.portingdeadmods.examplemod.ExampleMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ExampleMod.MODID)
public final class EMDataGatherer {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new IRItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new IRBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new IREnUSLangProvider(packOutput));

        IRTagsProvider.createTagProviders(generator, packOutput, lookupProvider, existingFileHelper, event.includeServer());
        generator.addProvider(event.includeServer(), new IRDataMapProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new IRRecipeProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new IRWorldGenProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new IRBlockLootModifierProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(IRBlockLootTableProvider::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(IRChestLootTableProvider::new, LootContextParamSets.CHEST)
        ), lookupProvider));
    }
}
