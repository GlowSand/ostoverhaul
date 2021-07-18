package glowsand.ostoverhaul;

import glowsand.ostoverhaul.items.MusicDiscConstructorBecauseItsNotPublic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

@Mod("ostoverhaul")
public class OstOverhaul {
    //ik this is bullshit I gotta fix it later lmao
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE;

    public static final DeferredRegister<SoundEvent> SOUND_REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,"ostoverhaul");
    public static final DeferredRegister<Item> ITEM_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS,"ostoverhaul");
    private static final DeferredRegister<GlobalLootModifierSerializer<?>> GLM = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, "ostoverhaul");
    private static final RegistryObject<LootTableModifier.Serializer> loottablething = GLM.register("weird_realm_loot_modifier", LootTableModifier.Serializer::new);
    public static final Identifier WEIRD_REALM_ID = new Identifier("ostoverhaul:weird_realm");
    public static final Identifier AS_ABOVE_SO_BELOW_ID = new Identifier("ostoverhaul:as_above_so_below");
    public static final SoundEvent AS_ABOVE_SO_BELOW_SOUND_EVENT =new SoundEvent(AS_ABOVE_SO_BELOW_ID);
    public static RegistryObject<SoundEvent> AS_ABOVE_SO_BELOW_SOUND_REGISTER = SOUND_REGISTER.register(AS_ABOVE_SO_BELOW_ID.getPath(),()->AS_ABOVE_SO_BELOW_SOUND_EVENT);
    public static final Identifier STRUCT_PACKET_ID = new Identifier("ostoverhaul","struct");
    public static final SoundEvent WEIRD_REALM_SOUND_EVENT = new SoundEvent(WEIRD_REALM_ID);
    public static RegistryObject<SoundEvent> WEIRD_REALM_SOUND_EVENT_REGISTER = SOUND_REGISTER.register(WEIRD_REALM_ID.getPath(),()->WEIRD_REALM_SOUND_EVENT);
    public static final Identifier SHOCK_ID = new Identifier("ostoverhaul:shock");
    public static final SoundEvent SHOCK_SOUND_EVENT = new SoundEvent(SHOCK_ID);
    public static RegistryObject<SoundEvent>  SHOCK_SOUND_EVENT_REGISTER = SOUND_REGISTER.register(SHOCK_ID.getPath(),()-> SHOCK_SOUND_EVENT);
    public static final Identifier DEEP_ID = new Identifier("ostoverhaul:claustrophobic");
    public static final SoundEvent DEEP_SOUND_EVENT = new SoundEvent(DEEP_ID);
    public static RegistryObject<SoundEvent> DEEP_SOUND_EVENT_REGISTER = SOUND_REGISTER.register(DEEP_ID.getPath(),()->DEEP_SOUND_EVENT);
    public static final Identifier FROZEN_ID = new Identifier("ostoverhaul:frosty");
    public static final SoundEvent FROZEN_SOUND_EVENT = new SoundEvent(FROZEN_ID);
    public static RegistryObject<SoundEvent> FROZEN_SOUND_EVENT_REGISTRY = SOUND_REGISTER.register(FROZEN_ID.getPath(),()->FROZEN_SOUND_EVENT);
    public static final RegistryObject<Item> SHOCK_ITEM = ITEM_DEFERRED_REGISTER.register("shock_disc",()->new MusicDiscConstructorBecauseItsNotPublic(20,SHOCK_SOUND_EVENT,new Item.Settings().maxCount(1).group(ItemGroup.MISC).rarity(Rarity.RARE).fireproof()));
    public static final RegistryObject<Item> WEIRD_REALM_ITEM = ITEM_DEFERRED_REGISTER.register("weird_realm_disc",()->new MusicDiscConstructorBecauseItsNotPublic(14,WEIRD_REALM_SOUND_EVENT, new Item.Settings().maxCount(1).group(ItemGroup.MISC).rarity(Rarity.RARE)));
    public static final Identifier REMNANT_ID = new Identifier("ostoverhaul:remnant");
    public static final SoundEvent REMNANT_SOUND_EVENT =new SoundEvent(REMNANT_ID);
    public static RegistryObject<SoundEvent> REMNANT_SOUND_REGISTER= SOUND_REGISTER.register(REMNANT_ID.getPath(),()->REMNANT_SOUND_EVENT);
    public static final Map<ServerPlayerEntity, StructureFeature<?>> serverPlayerEntityStructureFeatureMap = new HashMap<>();


    public OstOverhaul() {
        ITEM_DEFERRED_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        SOUND_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
        GLM.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(ServerTickStuff.class);

    }





    private void setup(final FMLCommonSetupEvent commonSetupEvent){
        INSTANCE = NetworkRegistry.ChannelBuilder.named(OstOverhaul.STRUCT_PACKET_ID).clientAcceptedVersions((a)->true).serverAcceptedVersions((a)->true).networkProtocolVersion(()->PROTOCOL_VERSION).simpleChannel();
        INSTANCE.registerMessage(0,StructureMessage.class,StructureMessage::encode,StructureMessage::new,StructureMessage::handle);
    }





}
