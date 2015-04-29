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
    private static final int POTION_ID = 3025;
    private static final int HALF_HEALTH = Skill.HITPOINTS.getRealLevel() / 2;

    @Override
    public boolean activate()
    {
        return Skill.HITPOINTS.getLevel() < HALF_HEALTH
                && Inventory.contains(FOOD_ID)
                || Skill.PRAYER.getLevel() < 15
                && Inventory.contains(POTION_ID);
    }

    @Override
    public void execute()
    {
        if (Skill.HITPOINTS.getLevel() < HALF_HEALTH)
        {
            Logger.addMessage("Eating");

            Item food = Inventory.getItems(FOOD_ID)[0];

            if (food != null)
            {
                Menu.sendAction(74, food.getId() - 1, food.getSlot(), 3214);

                Time.sleep(500);
            }
        }
        else
        {
            Logger.addMessage("Drinking");

            Item potion = Inventory.getItem(POTION_ID);

            if (potion != null)
            {
                Menu.sendAction(74, potion.getId() - 1, potion.getSlot(), 3214);

                Time.sleep(500);
            }
        }
    }
}