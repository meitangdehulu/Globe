package me.ichun.mods.globe.common.core;

import me.ichun.mods.globe.common.Globe;
import me.ichun.mods.globe.common.block.BlockGlobeCreator;
import me.ichun.mods.globe.common.block.BlockGlobeStand;
import me.ichun.mods.globe.common.item.ItemGlobe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerServer
{
    @SubscribeEvent
    public void onRegisterBlock(RegistryEvent.Register<Block> event)
    {
        Globe.blockGlobeCreator = new BlockGlobeCreator();
        event.getRegistry().register(Globe.blockGlobeCreator);

        Globe.blockGlobeStand= new BlockGlobeStand();
        event.getRegistry().register(Globe.blockGlobeStand);
    }

    @SubscribeEvent
    public void onRegisterItem(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(new ItemBlock(Globe.blockGlobeCreator).setRegistryName(Globe.blockGlobeCreator.getRegistryName()));
        event.getRegistry().register(new ItemBlock(Globe.blockGlobeStand).setRegistryName(Globe.blockGlobeStand.getRegistryName()));

        Globe.itemGlobe = new ItemGlobe();
        event.getRegistry().register(Globe.itemGlobe);

        Globe.proxy.applyItemRenderers();
    }

    @SubscribeEvent
    public void onRegisterSound(RegistryEvent.Register<SoundEvent> event)
    {
        ResourceLocation rs = new ResourceLocation("globe", "chargeup");
        Globe.soundChargeup = new SoundEvent(rs).setRegistryName(rs);
        event.getRegistry().register(Globe.soundChargeup);

        rs = new ResourceLocation("globe", "ding");
        Globe.soundDing = new SoundEvent(rs).setRegistryName(rs);
        event.getRegistry().register(Globe.soundDing);
    }
}
