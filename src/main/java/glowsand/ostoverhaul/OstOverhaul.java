package glowsand.ostoverhaul;

import glowsand.ostoverhaul.items.MusicDiscConstructorBecauseItsNotPublic;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.loot.BinomialLootTableRange;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OstOverhaul implements ModInitializer {
    //ik this is bullshit I gotta fix it later lmao

    public static final Identifier WEIRD_REALM_ID = new Identifier("ostoverhaul:weird_realm");
    public static final Identifier AS_ABOVE_SO_BELOW_ID = new Identifier("ostoverhaul:as_above_so_below");
    public static SoundEvent AS_ABOVE_SO_BELOW_SOUND_EVENT = new SoundEvent(AS_ABOVE_SO_BELOW_ID);
    public static final Identifier STRUCT_PACKET_ID = new Identifier("overhaul","struct");
    public static SoundEvent WEIRD_REALM_SOUND_EVENT = new SoundEvent(WEIRD_REALM_ID);
    public static Identifier SHOCK_ID = new Identifier("ostoverhaul:shock");
    public static SoundEvent SHOCK_SOUND_EVENT = new SoundEvent(SHOCK_ID);
    public static final Identifier DEEP_ID = new Identifier("ostoverhaul:claustrophobic");
    public static SoundEvent DEEP_SOUND_EVENT = new SoundEvent(DEEP_ID);
    public static final Identifier FROZEN_ID = new Identifier("ostoverhaul:frosty");
    public static SoundEvent FROZEN_SOUND_EVENT = new SoundEvent(FROZEN_ID);
    public static final MusicDiscItem SHOCK_ITEM = new MusicDiscConstructorBecauseItsNotPublic(20,SHOCK_SOUND_EVENT,new Item.Settings().maxCount(1).group(ItemGroup.MISC).rarity(Rarity.RARE).fireproof());
    public static final MusicDiscItem WEIRD_REALM_ITEM = new MusicDiscConstructorBecauseItsNotPublic(14,WEIRD_REALM_SOUND_EVENT, new Item.Settings().maxCount(1).group(ItemGroup.MISC).rarity(Rarity.RARE));
    public static final Identifier REMNANT_ID = new Identifier("ostoverhaul:remnant");
    public static SoundEvent REMNANT_SOUND_EVENT = new SoundEvent(REMNANT_ID);
    public Map<ServerPlayerEntity, StructureFeature<?>> serverPlayerEntityStructureFeatureMap = new HashMap<>();
    @Override
    public void onInitialize() {
        Registry.register(Registry.SOUND_EVENT,WEIRD_REALM_ID,WEIRD_REALM_SOUND_EVENT);
        Registry.register(Registry.SOUND_EVENT,AS_ABOVE_SO_BELOW_ID,AS_ABOVE_SO_BELOW_SOUND_EVENT);
        Registry.register(Registry.SOUND_EVENT,SHOCK_ID,SHOCK_SOUND_EVENT);
        Registry.register(Registry.SOUND_EVENT,DEEP_ID,DEEP_SOUND_EVENT);
        Registry.register(Registry.SOUND_EVENT,FROZEN_ID,FROZEN_SOUND_EVENT);
        Registry.register(Registry.SOUND_EVENT,REMNANT_ID,REMNANT_SOUND_EVENT);
        Registry.register(Registry.ITEM,new Identifier("ostoverhaul","shock_disc"),SHOCK_ITEM);
        Registry.register(Registry.ITEM,new Identifier("ostoverhaul","weird_realm_disc"),WEIRD_REALM_ITEM);
        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter)->{
            if (id.equals(new Identifier("minecraft","chests/end_city_treasure"))){
                FabricLootPoolBuilder fabricLootPool = FabricLootPoolBuilder.builder().rolls(BinomialLootTableRange.create(1,0.3f)).withEntry(ItemEntry.builder(WEIRD_REALM_ITEM).build());
                supplier.withPool(fabricLootPool.build());
            }
        });
        final int[] ticksorsmthn = {0};
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            ticksorsmthn[0]++;
            if (ticksorsmthn[0] %20 == 0) {
                //shit ass code lol
                List<ServerPlayerEntity> playerList = server.getPlayerManager().getPlayerList();
                for (ServerPlayerEntity playerEntity : playerList) {
                    if ((playerEntity != null && playerEntity.getServerWorld() != null && playerEntity.getPos() != null) && LocationPredicate.feature(StructureFeature.STRONGHOLD).test(playerEntity.getServerWorld(), playerEntity.getX(), playerEntity.getY(), playerEntity.getZ()) && !serverPlayerEntityStructureFeatureMap.getOrDefault(playerEntity, StructureFeature.BASTION_REMNANT).equals(StructureFeature.STRONGHOLD)) {
                        PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
                        packet.writeInt(1);
                        serverPlayerEntityStructureFeatureMap.put(playerEntity, StructureFeature.STRONGHOLD);
                        ServerPlayNetworking.send(playerEntity, STRUCT_PACKET_ID, packet);
                    }
                    else if (((playerEntity != null && playerEntity.getServerWorld() != null && playerEntity.getPos() != null) && LocationPredicate.feature(StructureFeature.BASTION_REMNANT).test(playerEntity.getServerWorld(), playerEntity.getX(), playerEntity.getY(), playerEntity.getZ()) && !serverPlayerEntityStructureFeatureMap.getOrDefault(playerEntity, StructureFeature.STRONGHOLD).equals(StructureFeature.BASTION_REMNANT))){
                        PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
                        packet.writeInt(2);
                        serverPlayerEntityStructureFeatureMap.put(playerEntity, StructureFeature.BASTION_REMNANT);
                        ServerPlayNetworking.send(playerEntity, STRUCT_PACKET_ID, packet);
                    }
                    else {
                        if ((playerEntity != null) && serverPlayerEntityStructureFeatureMap.getOrDefault(playerEntity, null) != null) {
                            PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
                            packet.writeInt(0);
                            serverPlayerEntityStructureFeatureMap.remove(playerEntity);
                            ServerPlayNetworking.send(playerEntity, STRUCT_PACKET_ID, packet);
                        }
                    }

                }
            }
            if (ticksorsmthn[0]== 400){
                serverPlayerEntityStructureFeatureMap.clear();
            }
        });
    }
}
