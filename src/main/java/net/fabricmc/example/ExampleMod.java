package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.render.EntityRendererRegistry;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.client.render.entity.PillagerEntityRenderer;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ExampleMod implements ModInitializer {

    public static final EntityType<Minuteman> COOKIE_CREEPER =
            Registry.register(
                    Registry.ENTITY_TYPE,
                    new Identifier("us", "pillager-ally"),
                    FabricEntityTypeBuilder.create(EntityCategory.AMBIENT, Minuteman::new).size(1, 2).build()
            );
    public static final Block RunDistance = new Block(FabricBlockSettings.of(Material.METAL).lightLevel(7).build());
    public static final Block CloseDistance = new Block(FabricBlockSettings.of(Material.METAL).lightLevel(7).build());
    @Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("Hello Fabric world!");
        EntityRendererRegistry.INSTANCE.register(Minuteman.class, (entityRenderDispatcher, context) -> new MinutemanRenderer(entityRenderDispatcher));
	    Registry.register(Registry.BLOCK,new Identifier("us","run_distance"),RunDistance);
        Registry.register(Registry.BLOCK,new Identifier("us","close_distance"),CloseDistance);

        //Registry.register(Registry.ITEM, new Identifier("us", "run_distance"), new BlockItem(RunDistance, new Item.Settings().group(ItemGroup.MISC)));
    }
}
