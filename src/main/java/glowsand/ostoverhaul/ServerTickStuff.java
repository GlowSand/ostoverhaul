package glowsand.ostoverhaul;

import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = "ostoverhaul",bus = Mod.EventBusSubscriber.Bus.FORGE,value= Dist.DEDICATED_SERVER)
public class ServerTickStuff  {
    public static int ticksorsmthn = 0;
    @SubscribeEvent
    public static void serverTickEnding(TickEvent.WorldTickEvent worldTickEvent){
        if (!worldTickEvent.world.isClient && worldTickEvent.phase.equals(TickEvent.Phase.END)) {
            ticksorsmthn++;
            if (ticksorsmthn % 20 == 0) {
                //shit ass code lol

                List<ServerPlayerEntity> playerList = Objects.requireNonNull(worldTickEvent.world.getServer()).getPlayerManager().getPlayerList();//server.getPlayerManager().getPlayerList();
                for (ServerPlayerEntity playerEntity : playerList) {
                    if ((playerEntity != null && playerEntity.getServerWorld() != null && playerEntity.getPos() != null) && LocationPredicate.feature(StructureFeature.STRONGHOLD).test(playerEntity.getServerWorld(), playerEntity.getX(), playerEntity.getY(), playerEntity.getZ()) && !OstOverhaul.serverPlayerEntityStructureFeatureMap.getOrDefault(playerEntity, StructureFeature.BASTION_REMNANT).equals(StructureFeature.STRONGHOLD)) {

                        OstOverhaul.serverPlayerEntityStructureFeatureMap.put(playerEntity, StructureFeature.STRONGHOLD);
                        OstOverhaul.INSTANCE.send(PacketDistributor.PLAYER.with(()->playerEntity),new StructureMessage(1));
                    } else if (((playerEntity != null && playerEntity.getServerWorld() != null && playerEntity.getPos() != null) && LocationPredicate.feature(StructureFeature.BASTION_REMNANT).test(playerEntity.getServerWorld(), playerEntity.getX(), playerEntity.getY(), playerEntity.getZ()) && !OstOverhaul.serverPlayerEntityStructureFeatureMap.getOrDefault(playerEntity, StructureFeature.STRONGHOLD).equals(StructureFeature.BASTION_REMNANT))) {

                        OstOverhaul.serverPlayerEntityStructureFeatureMap.put(playerEntity, StructureFeature.BASTION_REMNANT);
                        OstOverhaul.INSTANCE.send(PacketDistributor.PLAYER.with(()->playerEntity),new StructureMessage(2));
                    } else {
                        if ((playerEntity != null) && OstOverhaul.serverPlayerEntityStructureFeatureMap.getOrDefault(playerEntity, null) != null) {

                            OstOverhaul.serverPlayerEntityStructureFeatureMap.remove(playerEntity);
                            OstOverhaul.INSTANCE.send(PacketDistributor.PLAYER.with(()->playerEntity),new StructureMessage(0));
                        }
                    }

                }
            }
            if (ticksorsmthn == 400) {
                OstOverhaul.serverPlayerEntityStructureFeatureMap.clear();
            }
        }
    }
}
