package com.seuusuario.itemduplicator;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class ItemDuplicatorMod implements ModInitializer {
    public static final String MOD_ID = "itemduplicator";

    @Override
    public void onInitialize() {
        // Este evento roda no servidor para CADA jogador online a cada tick
        ServerTickEvents.END_PLAYER_TICK.register(player -> {
            // Garante que o código só executa no lado do servidor
            if (player.getWorld().isClient()) {
                return;
            }

            // Verifica se este jogador específico está agachado (Shift / Sneak)
            if (player.isSneaking()) {
                // Pega o item da mão principal desse jogador
                ItemStack mainHandItem = player.getStackInHand(Hand.MAIN_HAND);

                // Verifica se a mão não está vazia, se o item é empacotável (> 1) e se ainda não está cheio
                if (!mainHandItem.isEmpty() && mainHandItem.getMaxCount() > 1 && mainHandItem.getCount() < mainHandItem.getMaxCount()) {
                    
                    // Cria uma cópia do item e define a quantidade para o máximo do pack (ex: 64)
                    ItemStack duplicatedStack = mainHandItem.copy();
                    duplicatedStack.setCount(duplicatedStack.getMaxCount());
                    
                    // Atualiza a mão principal apenas deste jogador
                    player.setStackInHand(Hand.MAIN_HAND, duplicatedStack);
                }
            }
        });
    }
}
