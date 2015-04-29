package org.parabot.minimal.minimaldungeoneering;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Npcs;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.wrappers.SceneObject;

/**
 * This class touches the shrine after obtaining both necessary items to spawn the boss
 */
public class TouchShrine implements Strategy
{
    private static final int[] BOSS_IDS = { 9916, 9934, 9989, 10044, 10064, 10110, 10116 };

    private final int SHRINE_ID = 3634;
    private final int ORB_ID = 6822;
    private final int ROCK_ID = 1481;

    @Override
    public boolean activate()
    {
        return Inventory.contains(ROCK_ID, ORB_ID);
    }

    @Override
    public void execute()
    {
        SceneObject shrine = null;

        try
        {
            for (SceneObject so : SceneObjects.getAllSceneObjects())
            {
                if (so.getId() == SHRINE_ID)
                {
                    shrine = so;
                }
            }
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();

            Logger.addMessage("NullPointerException when getting all scene objects");
        }

        if (shrine != null)
        {
            Logger.addMessage("Touching shrine");

            shrine.interact(SceneObjects.Option.FIRST);

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Npcs.getNearest(BOSS_IDS).length > 0;
                }
            }, 5000);
        }
    }
}