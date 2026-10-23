​package com.example;
​import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
​import java.util.HashMap;
import java.util.UUID;
​public class ExampleMod implements ModInitializer {
public static final String MOD_ID = "itemduplicator";
public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
​private final HashMap<UUID, Integer> sneakTimers = new HashMap<>();
​@Override
public void onInitialize() {
LOGGER.info("Mod inicializado com sucesso!");
​ServerTickEvents.END_SERVER_TICK.register(server -> {
for (ServerPlayerEntity player : server.getPlayerList().getPlayers()) {
UUID uuid = player.getUuid();
​if (player.isSneaking()) {
ItemStack handStack = player.getMainHandStack();
​if (!handStack.isEmpty()) {
int timer = sneakTimers.getOrDefault(uuid, 0);
timer++;
​if (timer >= 20) {
duplicateItem(player, handStack);
sneakTimers.put(uuid, 0);
} else {
sneakTimers.put(uuid, timer);
}
} else {
sneakTimers.remove(uuid);
}
} else {
sneakTimers.remove(uuid);
}
}
});
}
​private void duplicateItem(ServerPlayerEntity player, ItemStack stack) {
int currentCount = stack.getCount();
int newCount = currentCount * 2;
​if (stack.getMaxCount() == 1) {
for (int i = 0; i < currentCount; i++) {
ItemStack duplicate = stack.copy();
duplicate.setCount(1);
ItemEntity itemEntity = new ItemEntity(
player.getWorld(),
player.getX(),
player.getY(),
player.getZ(),
duplicate
);
player.getWorld().spawnEntity(itemEntity);
}
player.sendMessage(Text.literal("§a[Duplicador] §eItens duplicados!"), true);
} else {
if (newCount <= stack.getMaxCount()) {
stack.setCount(newCount);
} else {
stack.setCount(stack.getMaxCount());
ItemStack surplus = stack.copy();
surplus.setCount(newCount - stack.getMaxCount());
ItemEntity itemEntity = new ItemEntity(
player.getWorld(),
player.getX(),
player.getY(),
player.getZ(),
surplus
);
player.getWorld().spawnEntity(itemEntity);
}
player.sendMessage(Text.literal("§a[Duplicador] §eItem duplicado!"), true);
}
}
}
