package glowsand.ostoverhaul;

import glowsand.ostoverhaul.duck.MusicTrackerDuck;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StructureMessage  {
    private final int id;
    public StructureMessage(int id1) {
        this.id = id1;
    }


    public StructureMessage(PacketByteBuf buf){
        this.id = buf.readInt();
    }

    public void encode(PacketByteBuf buf){
        buf.writeInt(this.id);
    }


    public void handle(Supplier<NetworkEvent.Context> contextSupplier){
        if (contextSupplier.get().getDirection().equals(NetworkDirection.PLAY_TO_CLIENT)){
            contextSupplier.get().enqueueWork(()->{
                MinecraftClient client = MinecraftClient.getInstance();
                switch (this.id){
                case 0:
                    ((MusicTrackerDuck) client.getMusicTracker()).getTracker().structureFeature = null;
                    break;
                case 1:
                    ((MusicTrackerDuck)client.getMusicTracker()).getTracker().setStructureFeature(StructureFeature.STRONGHOLD);
                    break;
                case 2:
                    ((MusicTrackerDuck) client.getMusicTracker()).getTracker().setStructureFeature(StructureFeature.BASTION_REMNANT);
                    break;
                }
                contextSupplier.get().setPacketHandled(true);
            });
        }
    }

}
