package org.parabot.minimal.minimaldungeoneering;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Npcs;
import org.rev317.min.api.wrappers.SceneObject;
import org.rev317.min.api.wrappers.Tile;

/**
 * This class touches the shrine after obtaining both necessary items to spawn the boss
 */
public class TouchShrine implements Strategy
{
    private final int[] BOSS_IDS;

    public TouchShrine(int[] BOSS_IDS)
    {
        this.BOSS_IDS = BOSS_IDS;
    }

    private SceneObject shrine;

    private final int SHRINE_ID = 3634;

    @Override
    public boolean activate()
    {
        try
        {
            for (SceneObject so : MinimalDungeoneering.getAllSceneObjects())
            {
                if (so.getId() == SHRINE_ID
                        && so.distanceTo() < 10
                        && Npcs.getNearest(BOSS_IDS).length == 0)
                {
                    shrine = so;

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
        MinimalDungeoneering.status = "Touching shrine";

        shrine.interact(0);

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