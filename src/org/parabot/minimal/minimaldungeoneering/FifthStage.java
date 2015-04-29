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

public class FifthStage implements Strategy
{
    private static final Area FIFTH_STAGE_AREA = new Area(new Tile(2954, 5094),
            new Tile(2954, 5071),
            new Tile(2958, 5071),
            new Tile(2958, 5094));

    private static final Tile MIDDLE_TILE = new Tile(2956, 5083);

    private static final int PASSAGEWAY_ID = 7219;

    @Override
    public boolean activate()
    {
        return FIFTH_STAGE_AREA.contains(Players.getMyPlayer().getLocation());
    }

    @Override
    public void execute()
    {
        SceneObject passageWay = null;

        for (SceneObject so : SceneObjects.getNearest(PASSAGEWAY_ID))
        {
            if (so.getLocation().getX() == 2955)
            {
                passageWay = so;

                break;
            }
        }

        if (passageWay == null)
        {
            MIDDLE_TILE.walkTo();

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return SceneObjects.getNearest(PASSAGEWAY_ID).length > 0;
                }
            }, 3000);
        }

        if (passageWay != null)
        {
            Logger.addMessage("Entering passageway");

            passageWay.interact(SceneObjects.Option.OPEN);

            Time.sleep(3500);
        }
    }
}