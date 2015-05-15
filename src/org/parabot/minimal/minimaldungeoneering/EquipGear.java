package org.parabot.minimal.minimaldungeoneering;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Game;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.wrappers.Item;

public class EquipGear implements Strategy
{
    private final int[] armorIds;
    
    public EquipGear(int[] armorIds)
    {
        this.armorIds = armorIds;
    }

    @Override
    public boolean activate()
    {
        return Inventory.contains(armorIds);
    }

    @Override
    public void execute()
    {
        Logger.addMessage("Equipping armor", true);

        for (Item item : Inventory.getItems(armorIds))
        {
            Menu.sendAction(454, item.getId() - 1, item.getSlot(), 3214);

            Time.sleep(500);
        }

        // Toggles auto-retaliate

        if (MinimalDungeoneering.mode == Mode.SECOND_FLOOR)
        {
            if (Game.getSetting(172) != 0)
            {
                Logger.addMessage("Disabling auto-retaliate", true);

                Menu.sendAction(169, -1, -1, 150);
            }
        }
        else if (MinimalDungeoneering.mode == Mode.THIRD_FLOOR)
        {
            if (Game.getSetting(172) == 0)
            {
                Logger.addMessage("Enabling auto-retaliate", true);

                Menu.sendAction(169, -1, -1, 150);
            }
        }
    }
}