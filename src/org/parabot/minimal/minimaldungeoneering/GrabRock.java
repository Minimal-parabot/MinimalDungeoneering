package org.parabot.minimal.minimaldungeoneering;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.SceneObjects;
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
    private final int rockId;

    public GrabRock(int rockId)
    {
        this.rockId = rockId;
    }

    private static final Tile[] SCULPTURE_TILES = { new Tile(2613, 9810),
             new Tile(2610, 9821),
             new Tile(2607, 9828),
             new Tile(2618, 9835) };

    private static final TilePath SCULPTURE_PATH = new TilePath(SCULPTURE_TILES);

    private static final int SCULPTURE_ID = 5808;

    @Override
    public boolean activate()
    {
        return !Inventory.contains(rockId);
    }

    @Override
    public void execute()
    {
        SceneObject sculpture = null;

        if (!Prayer.isActivated())
        {
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

        try
        {
            for (SceneObject so : SceneObjects.getAllSceneObjects())
            {
                if (so.getId() == SCULPTURE_ID)
                {
                    sculpture = so;
                }
            }
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();

            Logger.addMessage("NullPointerException when getting all scene objects", false);
        }

        if (sculpture == null || sculpture.distanceTo() > 10)
        {
            Logger.addMessage("Walking to rock", true);

            SCULPTURE_PATH.traverse();

            Time.sleep(1000);
        }

        if (sculpture != null && sculpture.distanceTo() <= 10)
        {
            Logger.addMessage("Grabbing rock", true);

            sculpture.interact(SceneObjects.Option.FIRST);

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Inventory.contains(rockId);
                }
            }, 3000);
        }
    }
}