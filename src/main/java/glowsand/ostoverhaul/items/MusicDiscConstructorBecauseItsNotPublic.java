package glowsand.ostoverhaul.items;

import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;

public class MusicDiscConstructorBecauseItsNotPublic extends MusicDiscItem {
    public MusicDiscConstructorBecauseItsNotPublic(int comparatorOutput, SoundEvent sound, Settings settings) {
        super(comparatorOutput, sound, settings);
    }


}
