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
import org.rev317.min.api.wrappers.TilePath;

public class FourthStage implements Strategy
{
    private final int passageWayId;

    public FourthStage(int passageWayId)
    {
        this.passageWayId = passageWayId;
    }

    private static final Area FOURTH_STAGE_AREA = new Area(new Tile(2956, 5068),
            new Tile(2956, 5034),
            new Tile(2966, 5034),
            new Tile(2966, 5068));

    private static final Tile[] TILE_ARRAY = { new Tile(2963, 5050),
            new Tile(2958, 5062) };

    private static final TilePath TILE_PATH = new TilePath(TILE_ARRAY);

    @Override
    public boolean activate()
    {
        return FOURTH_STAGE_AREA.contains(Players.getMyPlayer().getLocation());
    }

    @Override
    public void execute()
    {
        if (SceneObjects.getClosest(passageWayId) == null
                || SceneObjects.getClosest(passageWayId).distanceTo() >= 15)
        {
            Logger.addMessage("Traversing to passageway", true);

            TILE_PATH.traverse();

            Time.sleep(1000);
        }

        if (SceneObjects.getClosest(passageWayId).distanceTo() < 15)
        {
            SceneObject passageWay = SceneObjects.getClosest(passageWayId);

            if (passageWay != null)
            {
                Logger.addMessage("Entering passageway", true);

                passageWay.interact(SceneObjects.Option.OPEN);

                Time.sleep(new SleepCondition()
                {
                    @Override
                    public boolean isValid()
                    {
                        return !FOURTH_STAGE_AREA.contains(Players.getMyPlayer().getLocation());
                    }
                }, 5000);
            }
        }
    }
}