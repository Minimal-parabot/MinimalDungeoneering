package org.parabot.minimal.minimaldungeoneering;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.wrappers.Item;

/**
 * This class is in charge of making sure our equipment is worn
 * Additionally, it activates prayers if it recognizes that none are enabled
 */
public class EquipGear implements Strategy
{
    private final int[] ARMOR_IDS;
    private final boolean NORMAL_PRAYER;
    
    public EquipGear(int[] ARMOR_IDS, boolean NORMAL_PRAYER)
    {
        this.ARMOR_IDS = ARMOR_IDS;
        this.NORMAL_PRAYER = NORMAL_PRAYER;
    }

    @Override
    public boolean activate()
    {
        return Inventory.getCount(ARMOR_IDS) > 0;
    }

    @Override
    public void execute()
    {
        MinimalDungeoneering.status = "Equipping";

        for (final Item item : Inventory.getItems(ARMOR_IDS))
        {
            Menu.sendAction(454, item.getId() - 1, item.getSlot(), 3214);

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Inventory.getCount(item.getId()) == 0;
                }
            }, 1000);
        }
        
        if (!Prayer.areActivated())
        {
            MinimalDungeoneering.status = "Praying";

            if (NORMAL_PRAYER)
                Prayer.activateNormalPrayers();
            else
                Prayer.activateAncientPrayers();
        }
    }
}