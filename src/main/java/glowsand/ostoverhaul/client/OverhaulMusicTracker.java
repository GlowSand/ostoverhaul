package glowsand.ostoverhaul.client;

import glowsand.ostoverhaul.OstOverhaul;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.client.sound.*;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.StructureFeature;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Environment(EnvType.CLIENT)
public class OverhaulMusicTracker implements SoundInstanceListener{
    private final Random random = new Random();
    private final MinecraftClient client;
    private SoundInstance current = null;
    private int currentPriority = 0;
    private int timeUntilNextSong = 100;
    private boolean frozen = false;
    private boolean deep = false;
    private Biome currentBiome;
    public StructureFeature<?> structureFeature;
    public OverhaulMusicTracker(MinecraftClient client) {
        this.client = client;
    }

    public void tick() {
        if (!this.client.getSoundManager().isPlaying(this.current)) {
            this.current = null;
            this.currentPriority = 0;
        }
        this.startMusicShid();
        this.considerStructure();
        this.playMusicOrSmthn();
        if (this.current == null){
            this.timeUntilNextSong--;
        }
    }


    public void play(SoundEvent music, int priority,int nextRandomThin) {
        if (this.currentPriority <= priority && (this.current == null || !this.current.getId().equals(music.getId()))) {
            this.client.getSoundManager().stop(this.current);
            this.timeUntilNextSong = this.random.nextInt(nextRandomThin);
            this.currentPriority = priority;
            this.current = PositionedSoundInstance.music(music);
            if (this.current.getSound() != SoundManager.MISSING_SOUND) {
                this.client.getSoundManager().play(this.current);
            }
        }
    }

    public void setStructureFeature(@Nullable StructureFeature<?> structureFeatureToSet) {
        if (Objects.equals(structureFeatureToSet, StructureFeature.STRONGHOLD) && !Objects.equals(structureFeature,StructureFeature.STRONGHOLD)) {
            this.play(OstOverhaul.AS_ABOVE_SO_BELOW_SOUND_EVENT, 1,12000);
            structureFeature = StructureFeature.STRONGHOLD;
        }
        else if (Objects.equals(structureFeatureToSet, StructureFeature.BASTION_REMNANT) && !Objects.equals(structureFeature,StructureFeature.BASTION_REMNANT)){
            this.play(OstOverhaul.REMNANT_SOUND_EVENT, 1,12000);
            structureFeature = StructureFeature.BASTION_REMNANT;
        }
        else {
            this.structureFeature = structureFeatureToSet;
        }
    }

    public void startMusicShid() {

        if (this.client.player != null && this.client.world != null && !((this.client.inGameHud != null && this.client.inGameHud.getBossBarHud().shouldPlayDragonMusic()) && !(this.client.currentScreen instanceof CreditsScreen))) {
            this.currentBiome = this.client.player.world.getBiome(this.client.player.getBlockPos());
            this.frozen = this.currentBiome.getTemperature(this.client.player.getBlockPos()) <= 0.15F || this.currentBiome.getPrecipitation().equals(Biome.Precipitation.SNOW);
            this.deep = this.client.player.getY() <= 60 && !this.client.world.isSkyVisibleAllowingSea(this.client.player.getBlockPos());
        } else if (this.client.inGameHud != null && this.client.inGameHud.getBossBarHud().shouldPlayDragonMusic()) {
            play(MusicType.DRAGON.getSound(), 5,12000);
        }
        else if (this.client.currentScreen instanceof CreditsScreen) {
            this.play(MusicType.CREDITS.getSound(), 5,1000);
        } else {
            this.play(MusicType.MENU.getSound(), 5, 200);
        }
    }
    public void playMusicOrSmthn(){
        if (this.client.player != null && this.timeUntilNextSong <= 0 && currentBiome != null && (this.current == null)) {
            if (this.currentBiome.getMusic().isPresent() && this.random.nextBoolean()) {
                this.play(this.currentBiome.getMusic().get().getSound(), 0, 12000);
            } else if (this.client.player.world.getRegistryKey() == World.END) {
                play(MusicType.END.getSound(), 0, 12000);

            }  else if (this.client.player.isSubmergedInWater() && (this.currentBiome.getCategory() == Biome.Category.OCEAN || this.currentBiome.getCategory() == Biome.Category.RIVER)) {
                this.play(MusicType.UNDERWATER.getSound(), 0, 12000);
            }
            else if (this.frozen && (!this.deep || this.random.nextBoolean())) {
                this.play(OstOverhaul.FROZEN_SOUND_EVENT, 0, 12000);
            } else if (this.client.player.world.getRegistryKey() == World.OVERWORLD && this.deep ) {
                this.play(OstOverhaul.DEEP_SOUND_EVENT, 0, 12000);
            } else if (this.client.player.world.getRegistryKey() == World.OVERWORLD && !this.deep && !this.frozen) {
                this.play(MusicType.GAME.getSound(), 0, 12000);
            }
        }
    }

    public void considerStructure(){
        if (this.client.player != null && this.timeUntilNextSong <= 0 && currentBiome != null && this.current == null) {
            if (this.structureFeature == StructureFeature.STRONGHOLD) {
                this.play(OstOverhaul.AS_ABOVE_SO_BELOW_SOUND_EVENT, 1, 12000);
            } else if (this.structureFeature == StructureFeature.BASTION_REMNANT) {
                this.play(OstOverhaul.REMNANT_SOUND_EVENT, 1, 12000 );
            }
        }
    }

    @Override
    public void onSoundPlayed(SoundInstance sound, WeightedSoundSet soundSet) {
        if (sound != null && sound.getCategory() != null && this.current != null) {
            if (sound.getCategory().equals(SoundCategory.RECORDS) & this.current.getId() != MusicType.DRAGON.getSound().getId()) {
                if (this.current != null) {
                    this.client.getSoundManager().stop(this.current);
                    this.timeUntilNextSong = this.random.nextInt(1200) + 2800;
                }
                if (this.timeUntilNextSong <= 2800) {
                    this.timeUntilNextSong += this.random.nextInt(1200) + 2800;
                }
            }
        }
    }
}
