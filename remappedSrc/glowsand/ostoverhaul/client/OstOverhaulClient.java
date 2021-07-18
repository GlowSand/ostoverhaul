package glowsand.ostoverhaul.client;

import glowsand.ostoverhaul.OstOverhaul;
import glowsand.ostoverhaul.duck.MusicTrackerDuck;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.gen.feature.StructureFeature;

@Environment(EnvType.CLIENT)
public class OstOverhaulClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ClientPlayNetworking.registerGlobalReceiver(OstOverhaul.STRUCT_PACKET_ID,((client, handler, buf, responseSender) ->{
            int structure = buf.readInt();
            switch (structure){
                case 0:
                    ((MusicTrackerDuck) client.getMusicTracker()).getTracker().structureFeature = null;
                    break;
                case 1:
                    ((MusicTrackerDuck) client.getMusicTracker()).getTracker().setStructureFeature(StructureFeature.STRONGHOLD);
                    break;
                case 2:
                    ((MusicTrackerDuck) client.getMusicTracker()).getTracker().setStructureFeature(StructureFeature.BASTION_REMNANT);
                    break;
            }

        }));
    }
}
