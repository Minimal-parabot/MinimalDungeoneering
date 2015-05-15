package org.parabot.minimal.minimaldungeoneering;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.wrappers.Area;
import org.rev317.min.api.wrappers.Tile;
import org.rev317.min.api.wrappers.TilePath;

public class ThirdStage implements Strategy
{
    private static final Area THIRD_STAGE_AREA = new Area(new Tile(2952, 5030),
            new Tile(2952, 4994),
            new Tile(3023, 4994),
            new Tile(3023, 5030));

    private static final Tile[] TILE_ARRAY = { new Tile(3012, 5002),
            new Tile(3002, 5001),
            new Tile(2991, 4998),
            new Tile(2982, 5001),
            new Tile(2978, 5006),
            new Tile(2976, 5011),
            new Tile(2970, 5018),
            new Tile(2962, 5022),
            new Tile(2958, 5030) };

    private static final TilePath TILE_PATH = new TilePath(TILE_ARRAY);

    @Override
    public boolean activate()
    {
        return THIRD_STAGE_AREA.contains(Players.getMyPlayer().getLocation());
    }

    @Override
    public void execute()
    {
        if (!Players.getMyPlayer().getLocation().equals(TILE_ARRAY[TILE_ARRAY.length - 1]))
        {
            Logger.addMessage("Traversing tile path", true);

            TILE_PATH.traverse();

            Time.sleep(1000);
        }

        if (Players.getMyPlayer().getLocation().equals(TILE_ARRAY[TILE_ARRAY.length - 1]))
        {
            Logger.addMessage("Crossing ledge", true);

            // Interacting is hard-coded because getAllSceneObjects doesn't work on this floor
            // Sometimes the local x and y coordinates will change and ruin the script
            Menu.sendAction(502, 1192356766, 30, 87, 7239, 3);

            Time.sleep(new SleepCondition()
            {
                @Override public boolean isValid()
                {
                    return !THIRD_STAGE_AREA.contains(Players.getMyPlayer().getLocation());
                }
            }, 2500);
        }
    }
}