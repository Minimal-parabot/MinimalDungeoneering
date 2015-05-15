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
    private final int passageWayId;

    public FifthStage(int passageWayId)
    {
        this.passageWayId = passageWayId;
    }
    private static final Area FIFTH_STAGE_AREA = new Area(new Tile(2954, 5094),
            new Tile(2954, 5071),
            new Tile(2958, 5071),
            new Tile(2958, 5094));

    private static final Tile MIDDLE_TILE = new Tile(2956, 5083);

    @Override
    public boolean activate()
    {
        return FIFTH_STAGE_AREA.contains(Players.getMyPlayer().getLocation());
    }

    @Override
    public void execute()
    {
        SceneObject passageWay = null;

        for (SceneObject so : SceneObjects.getNearest(passageWayId))
        {
            if (so.getLocation().getX() == 2955)
            {
                passageWay = so;

                break;
            }
        }

        if (passageWay == null)
        {
            Logger.addMessage("Traversing to middle tile", true);

            MIDDLE_TILE.walkTo();

            Time.sleep(1000);
        }

        if (passageWay != null)
        {
            Logger.addMessage("Entering passageway", true);

            passageWay.interact(SceneObjects.Option.OPEN);

            Time.sleep(3500);
        }
    }
}