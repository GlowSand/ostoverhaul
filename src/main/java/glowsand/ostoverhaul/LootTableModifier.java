package glowsand.ostoverhaul;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LootTableModifier extends LootModifier {
    private static final Identifier id = new Identifier("minecraft","chests/end_city_treasure");

    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected LootTableModifier(LootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @NotNull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> list, LootContext arg) {
        if (arg.getQueriedLootTableId().equals(id)){
            ItemStack itemStack = new ItemStack(OstOverhaul.WEIRD_REALM_ITEM.get());
            list.add(itemStack);
        }
        return list;
    }

    public static class Serializer extends GlobalLootModifierSerializer<LootTableModifier>  {

        @Override
        public LootTableModifier read(Identifier arg, JsonObject jsonObject, LootCondition[] args) {
            return new LootTableModifier(args);
        }

        @Override
        public JsonObject write(LootTableModifier iGlobalLootModifier) {
            return makeConditions(iGlobalLootModifier.conditions);
        }
    }
}

