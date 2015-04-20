package org.parabot.minimal.minimaldungeoneering;

import org.rev317.min.api.methods.Game;
import org.rev317.min.api.methods.Menu;

/**
 * Created by Single Core
 * Transferred over to Ikov by Minimal
 */

public enum Prayer
{
    THICK_SKIN(5609, 83, 1, true),
    BURST_OF_STRENGTH(5610, 84, 4, true),
    CLARITY_OF_THOUGHT(5611, 85, 7, true),
    SHARP_EYE(25058, 601, 8, true),
    MYSTIC_WILL(25060, 602, 9, true),
    ROCK_SKIN(5612, 86, 10, true),
    SUPERHUMAN_STRENGTH(5613, 87, 13, true),
    IMPROVED_REFLEXES(5614, 88, 16, true),
    RAPID_RESTORE(5615, 89, 19, true),
    RAPID_HEAL(5616, 90, 22, true),
    PROTECT_ITEM(5617, 91, 25, true),
    HAWK_EYE(25022, 603, 26, true),
    MYSTIC_LORE(25024, 604, 27, true),
    STEEL_SKIN(5618, 92, 28, true),
    ULTIMATE_STRENGTH(5619, 93, 31, true),
    INCREDIBLE_REFLEXES(5620, 94, 34, true),
    PROTECT_FROM_SUMMONING(44650, 2843, 35, true),
    PROTECT_FROM_MAGIC(5621, 95, 37, true),
    PROTECT_FROM_MISSILES(5622, 96, 40, true),
    PROTECT_FROM_MELEE(5623, 97, 43, true),
    EAGLE_EYE(25038, 605, 44, true),
    MYSTIC_MIGHT(25040, 606, 45, true),
    RETRIBUTION(683, 98, 46, true),
    REDEMPTION(684, 99, 49, true),
    PROTECT_ITEM_ANCIENT(22503, 610, 50, false),
    SAP_WARRIOR(22505, 611, 50, false),
    SMITE(685, 100, 52, true),
    SAP_RANGER(22507, 612, 52, false),
    SAP_MAGE(22509, 613, 54, false),
    SAP_SPIRIT(22511, 614, 56, false),
    BERSERKER(22513, 615, 59, false),
    CHIVALRY(25048, 607, 60, true),
    DEFLECT_SUMMONING(22515, 616, 62, false),
    RAPID_RENEWAL(17014, 612, 65, true),
    DEFLECT_MAGIC(22517, 617, 65, false),
    DEFLECT_MISSILES(22519, 618, 68, false),
    PIETY(25050, 608, 70, true),
    RIGOUR(17018, 610, 70, true),
    AUGURY(17020, 611, 70, true),
    DEFLECT_MELEE(22521, 619, 71, false),
    LEECH_ATTACK(22523, 620, 74, false),
    LEECH_RANGED(22525, 621, 76, false),
    LEECH_MAGIC(22527, 622, 78, false),
    LEECH_DEFENCE(22529, 623, 80, false),
    LEECH_STRENGTH(22531, 624, 82, false),
    LEECH_ENERGY(22533, 625, 84, false),
    LEECH_SPECIAL_ATTACK(22535, 626, 86, false),
    WRATH(22537, 627, 89, false),
    SOUL_SPLIT(22539, 628, 92, false),
    TURMOIL(22541, 629, 95, false);

    @Override
    public String toString()
    {
        return name().charAt(0) + name().substring(1).toLowerCase().replace("_", " ");
    }
    
    private int action3;
    private int settingId;
    private int level;
    private boolean normal;
    
    Prayer(int action3, int settingId, int level, boolean normal)
    {
        this.action3 = action3;
        this.settingId = settingId;
        this.level = level;
        this.normal = normal;
    }
    
    public static void toggle()
    {
        Menu.sendAction(1500, -1, -1, 216);
    }
    
    public static boolean isActivated()
    {
        for (Prayer p : Prayer.values())
        {
            if (Game.getSetting(p.settingId) != 0)
            {
                return true;
            }
        }

        return false;
    }
}