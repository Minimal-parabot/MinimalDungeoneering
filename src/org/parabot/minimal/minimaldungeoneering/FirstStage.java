package org.parabot.minimal.minimaldungeoneering;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.wrappers.Area;
import org.rev317.min.api.wrappers.Item;
import org.rev317.min.api.wrappers.SceneObject;
import org.rev317.min.api.wrappers.Tile;

public class FirstStage implements Strategy
{
    // North-west, south-west, south-east, north-east
    private static final Area FIRST_STAGE_AREA = new Area(new Tile(3049, 5008),
            new Tile(3049, 4992),
            new Tile(3061, 4992),
            new Tile(3061, 5008));

    private static final int CHEST_ID = 2079;
    private static final int CONTORTION_BARS_ID = 7251;

    private static final int[] OVERLOAD_IDS = new int[] { 15333, 15334, 15335, 15336 };

    @Override
    public boolean activate()
    {
        return FIRST_STAGE_AREA.contains(Players.getMyPlayer().getLocation());
    }

    @Override
    public void execute()
    {
        if (Inventory.getCount() < 20)
        {
            Logger.addMessage("Opening chest for supplies");

            SceneObject chest = SceneObjects.getClosest(CHEST_ID);

            if (chest != null)
            {
                chest.interact(SceneObjects.Option.OPEN);

                Time.sleep(new SleepCondition()
                {
                    @Override
                    public boolean isValid()
                    {
                        return Inventory.getCount() >= 20;
                    }
                }, 2500);
            }
        }

        if (!Prayer.isActivated())
        {
            Logger.addMessage("Toggling quick prayer");

            Prayer.toggle();

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Prayer.isActivated();
                }
            }, 1000);
        }

        if (Inventory.getCount() >= 20
                && Prayer.isActivated())
        {
            if (Inventory.contains(OVERLOAD_IDS))
            {
                Item overload = Inventory.getItems(OVERLOAD_IDS)[0];

                if (overload != null)
                {
                    Logger.addMessage("Drinking overload");

                    Menu.sendAction(74, overload.getId() - 1, overload.getSlot(), 3214);

                    Time.sleep(500);
                }
            }

            Logger.addMessage("Entering contortion bars");

            SceneObject contortionBars = SceneObjects.getClosest(CONTORTION_BARS_ID);

            if (contortionBars != null)
            {
                contortionBars.interact(SceneObjects.Option.OPEN);

                Time.sleep(new SleepCondition()
                {
                    @Override
                    public boolean isValid()
                    {
                        return !FIRST_STAGE_AREA.contains(Players.getMyPlayer().getLocation());
                    }
                }, 2500);
            }
        }
    }
}