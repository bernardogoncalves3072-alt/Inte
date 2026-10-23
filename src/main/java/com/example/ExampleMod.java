package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class ExampleMod implements ModInitializer {
    public static final String MOD_ID = "itemduplicator";

    @Override
    public void onInitialize() {
        ServerTickEvents.END_PLAYER_TICK.register(player -> {
            if (player.getWorld().isClient()) {
                return;
            }

            if (player.isSneaking()) {
                ItemStack mainHandItem = player.getStackInHand(Hand.MAIN_HAND);

                if (!mainHandItem.isEmpty() && mainHandItem.getMaxCount() > 1 && mainHandItem.getCount() < mainHandItem.getMaxCount()) {
                    ItemStack duplicatedStack = mainHandItem.copy();
                    duplicatedStack.setCount(duplicatedStack.getMaxCount());
                    player.setStackInHand(Hand.MAIN_HAND, duplicatedStack);
                }
            }
        });
    }
}
