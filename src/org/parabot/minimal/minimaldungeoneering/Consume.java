package org.parabot.minimal.minimaldungeoneering;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.Skill;
import org.rev317.min.api.wrappers.Item;

public class Consume implements Strategy
{
    private static final int FOOD_ID = 18156;
    private static final int SUPER_RESTORE_ID = 3025;

    private double healthThreshold = ((Skill.HITPOINTS.getRealLevel() > 99) ? 99 : Skill.HITPOINTS.getRealLevel()) * 0.6;


    @Override
    public boolean activate()
    {
        return Skill.HITPOINTS.getLevel() <= healthThreshold
                && Inventory.contains(FOOD_ID)
                || Skill.PRAYER.getLevel() < 15
                && Inventory.contains(SUPER_RESTORE_ID);
    }

    @Override
    public void execute()
    {
        if (Skill.HITPOINTS.getLevel() <= healthThreshold)
        {
            Logger.addMessage("Eating");

            Item food = Inventory.getItems(FOOD_ID)[0];

            if (food != null)
            {
                Menu.sendAction(74, food.getId() - 1, food.getSlot(), 3214);
            }
        }
        else if (Skill.PRAYER.getLevel() < 15)
        {
            Logger.addMessage("Drinking Super restore");

            Item potion = Inventory.getItem(SUPER_RESTORE_ID);

            if (potion != null)
            {
                Menu.sendAction(74, potion.getId() - 1, potion.getSlot(), 3214);
            }
        }

        Time.sleep(750);
    }
}