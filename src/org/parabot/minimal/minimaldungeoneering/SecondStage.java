package org.parabot.minimal.minimaldungeoneering;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.SceneObjects;
import org.rev317.min.api.wrappers.Area;
import org.rev317.min.api.wrappers.SceneObject;
import org.rev317.min.api.wrappers.Tile;

public class SecondStage implements Strategy
{
    private static final Area SECOND_STAGE_AREA = new Area(new Tile(3049, 5003),
            new Tile(3049, 4994),
            new Tile(3023, 4994),
            new Tile(3023, 5003));

    private static final Tile MIDDLE_TILE = new Tile(3037, 4998);

    private static final int GRILL_ID = 7255;

    @Override
    public boolean activate()
    {
        return SECOND_STAGE_AREA.contains(Players.getMyPlayer().getLocation());
    }

    @Override
    public void execute()
    {
        if (SceneObjects.getNearest(GRILL_ID).length == 0)
        {
            Logger.addMessage("Walking to middle tile");

            MIDDLE_TILE.walkTo();

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return SceneObjects.getNearest(GRILL_ID).length > 0;
                }
            }, 5000);
        }

        if (SceneObjects.getNearest(GRILL_ID).length > 0)
        {
            Logger.addMessage("Opening grill");

            SceneObject grill = SceneObjects.getClosest(GRILL_ID);

            if (grill != null)
            {
                grill.interact(SceneObjects.Option.OPEN);

                Time.sleep(new SleepCondition()
                {
                    @Override
                    public boolean isValid()
                    {
                        return !SECOND_STAGE_AREA.contains(Players.getMyPlayer().getLocation());
                    }
                }, 5000);
            }
        }
    }
}