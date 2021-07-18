package glowsand.ostoverhaul.mixin;

import glowsand.ostoverhaul.OstOverhaul;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends EntityMixin {
    @Shadow public abstract ItemStack getStack();

        @Shadow public abstract void setStack(ItemStack stack);


    @Override
    public void lightingStruck(ServerWorld world, LightningEntity lightning, CallbackInfo ci) {
        if (this.getStack().getItem() instanceof MusicDiscItem) {
            this.setStack(OstOverhaul.SHOCK_ITEM.getDefaultStack());
            ci.cancel();
        }
    }


}
