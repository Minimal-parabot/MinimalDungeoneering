package org.parabot.minimal.minimaldungeoneering;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.wrappers.SceneObject;
import org.rev317.min.api.wrappers.Tile;
import org.rev317.min.api.wrappers.TilePath;

/**
 * This class is in charge of getting the first required item for Floor 2, Rock
 * Try/catch is used because our small sleep times means we will inevitably try to activate the method
 * with the map still loading, throwing a NullPointerException
 */
public class GrabRock implements Strategy
{
    private SceneObject sculpture;

    private final Tile[] SCULPTURE_TILES = { new Tile(2613, 9809),
                                             new Tile(2611, 9820),
                                             new Tile(2606, 9831),
                                             new Tile(2616, 9835) };

    private final TilePath SCULPTURE_PATH = new TilePath(SCULPTURE_TILES);

    private final int SCULPTURE_ID = 5808;
    private final int ROCK_ID = 1481;

    @Override
    public boolean activate()
    {
        try
        {
            for (SceneObject so : MinimalDungeoneering.getAllSceneObjects())
            {
                if (so.getId() == SCULPTURE_ID
                        && Inventory.getCount(ROCK_ID) == 0)
                {
                    sculpture = so;

                    return true;
                }
            }
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();

            Time.sleep(500);
        }

        return false;
    }

    @Override
    public void execute()
    {
        if (sculpture.distanceTo() > 10)
        {
            MinimalDungeoneering.status = "Walking to rock";

            SCULPTURE_PATH.traverse();

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return SCULPTURE_PATH.hasReached();
                }
            }, 500);
        }
        else
        {
            MinimalDungeoneering.status = "Grabbing rock";

            sculpture.interact(0);

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Inventory.getCount(ROCK_ID) > 0;
                }
            }, 3000);
        }
    }
}