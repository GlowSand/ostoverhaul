package glowsand.ostoverhaul.mixin;

import glowsand.ostoverhaul.client.OverhaulMusicTracker;
import glowsand.ostoverhaul.duck.MusicTrackerDuck;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.SoundInstance;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicTracker.class)
public abstract class MusicTrackerMixin implements MusicTrackerDuck {


    @Shadow @Final private MinecraftClient client;
    @Unique
    public OverhaulMusicTracker tracker;

    @Inject(method = "<init>", at= @At("TAIL"))
    public void initMixin(MinecraftClient client, CallbackInfo ci){
        tracker = new OverhaulMusicTracker(client);
        this.client.getSoundManager().registerListener(tracker);
    }


    @Inject(method = "tick",at = @At("HEAD"),cancellable = true)
    public void tickmixin(CallbackInfo ci){
        this.tracker.tick();
        ci.cancel();
    }

    @Override
    public OverhaulMusicTracker getTracker(){
        return this.tracker;
    }
}
