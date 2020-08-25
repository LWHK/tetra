package se.mickelus.tetra.module.data;

import com.google.common.collect.Multimap;
import com.google.gson.*;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import se.mickelus.tetra.module.Priority;

import java.lang.reflect.Type;

/**
 * ModuleVariantData contain stats and information for a variant of an item module.
 * Example json:
 * {
 *     "key": "heavy_blade/oak_log",
 *     "durability": 100,
 *     "integrity": -2,
 *     "damage": 3,
 *     "attackSpeed": -0.4,
 *     "glyph": {
 *         "tint": "9d804e",
 *         "textureX": 48
 *     },
 *     "effects": {
 *         "sweeping": 3
 *     },
 *     "tools": {
 *         "cut": [1, 3.443],
 *         "hammer": 2
 *     }
 * }
 */
public class VariantData extends ItemProperties {
    /**
     * The key for the module variant. This is used for referencing the module and should be unique. In schematic
     * outcome definitions the moduleVariant field should match this value. Also used as part of localized strings.
     */
    public String key;

    public String category = "misc";

    public Multimap<Attribute, AttributeModifier> attributes;

    public AbilityData[] abilities;

    /**
     * The tools field is an object describing which tools this item should be usable as, and on which level and with what efficiency it provides that
     * use. The keys in the object should be names of provided tools and the value is an array containing the level and efficiency. The level is an
     * integer and the efficiency is a decimal number. The efficiency is optional and when not provided the level should not be placed within an array.
     * Optional, can be omitted if the module variant provides no tool usages.
     *
     * Json format:
     * {
     *     "toolA": [level, efficiency],
     *     "toolB": level
     * }
     */
    public ToolData tools;

    /**
     * The effects field is an object describing which effects a module provides, and which level and efficiency that
     * effect has. The keys in the object should be names of provided effects and the value is an array containing the
     * level and efficiency. The level is an integer and the efficiency is a decimal number. The efficiency is optional
     * and when not provided the level should not be placed within an array.
     * Optional, can be omitted if the module variant provides no effects.
     *
     * Json format:
     * {
     *     "effectA": [level, efficiency],
     *     "effectB": level
     * }
     */
     public EffectData effects;

    /**
     * The priority for setting the name of the item. Multiple modules may want to provide an item name e.g. hammer,
     * or pickaxe, and this field is used to prioritize that.
     * Possible values in ascending priority order: LOWEST, LOWER, LOW, BASE, HIGH, HIGHER, HIGHEST
     */
    public Priority namePriority = Priority.BASE;

    /**
     * The priority for setting the prefixes of the item. Multiple modules may want to provide a name prefix e.g.
     * Iron Pickaxe, Tempered steel axe, or Serrated copper shortblade. Only two prefixes are displayed and when more
     * than two are available, this field is used to prioritize which prefixes are used.
     * Possible values in ascending priority order: LOWEST, LOWER, LOW, BASE, HIGH, HIGHER, HIGHEST
     */
    public Priority prefixPriority = Priority.BASE;

    /**
     * The glyph displayed for this module variation, preferrably the same glyph texture is used for all variations of
     * one module while the color differs based on material.
     */
    public GlyphData glyph = new GlyphData();

    public ModuleModel[] models = new ModuleModel[0];

    /**
     * The damage this module provides, only useful for modules used in handheld items. The damage values from all
     * modules of the item are summed up. Higher damage values makes you better at hurting others, ouch!
     * Although it's uncommon, the damage value can be a decimal number.
     */
    @Deprecated
    public float damage = 0;

    /**
     * Multiplies the flat damage value of the item. A value of 1.1 yields a 10% increase and a value of 0.85 yields a
     * 15% decrease. This is cumulative, e.g. if two modules increase the damage by 10% the final multiplier will
     * be 1.1 * 1.1 = 1.21.
     */
    @Deprecated
    public float damageMultiplier = 1;

    /**
     * The attack speed this module provides, only useful for modules used in handheld items. The attack speed from all
     * modules of the item are summed up. Negative values makes the attack speed slower and positive values makes it
     * faster.
     */
    @Deprecated
    public float attackSpeed = 0;

    /**
     * Multiplies the flat attack speed of the item. A value of 1.1 yields a 10% faster swing speed and a value of
     * 0.85 yields a 15% slower attack speed. This is cumulative, e.g. if two modules increase the speed by 10% the
     * final multiplier will be 1.1 * 1.1 = 1.21.
     */
    @Deprecated
    public float attackSpeedMultiplier = 1;

    /**
     * For handheld items this represent the item's reach. The base reach for items is 5 blocks and this value added to that.
     */
    @Deprecated
    public float range = 0;

    public int magicCapacity = 0;

    public String getKey() {
        return key;
    }

    public static class Deserializer implements JsonDeserializer<VariantData> {
        @Override
        public VariantData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            if (jsonObject.has("materials")) {
                return context.deserialize(json, MaterialVariantData.class);
            } else {
                return context.deserialize(json, UniqueVariantData.class);
            }
        }
    }
}
