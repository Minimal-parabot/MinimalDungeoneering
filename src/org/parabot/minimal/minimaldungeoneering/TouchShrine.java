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
    private final int[] bossIds;

    private final int rockId;
    private final int orbId;

    public TouchShrine(int[] bossIds, int rockId, int orbId)
    {
        this.bossIds = bossIds;

        this.rockId = rockId;
        this.orbId = orbId;
    }

    private static final int SHRINE_ID = 3634;

    @Override
    public boolean activate()
    {
        return Inventory.contains(rockId, orbId);
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
                    return Npcs.getNearest(bossIds).length > 0;
                }
            }, 5000);
        }
    }
}