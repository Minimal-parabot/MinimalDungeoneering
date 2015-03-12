package org.parabot.minimal.minimaldungeoneering;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.wrappers.SceneObject;
import org.rev317.min.api.wrappers.Tile;
import org.rev317.min.api.wrappers.TilePath;

/**
 * This class is in charge of grabbing the second item needed for Floor 2, Orb
 * Pretty much the same as GrabRock except we WANT our player to be on the 5th tile in
 * the ORB_TILES array because Ikov sometimes bugs out and doesn't let us click from
 * any other tile except this one.
 */
public class GrabOrb implements Strategy
{
    private SceneObject crate;

    private final Tile[] ORB_TILES = { new Tile(2609, 9835),
                                       new Tile(2597, 9836),
                                       new Tile(2586, 9838),
                                       new Tile(2576, 9843),
                                       new Tile(2570, 9846),
                                       new Tile(2565, 9849) };

    private final TilePath ORB_PATH = new TilePath(ORB_TILES);

    private final Tile CRATE_TILE = new Tile(2563, 9847);

    private final int CRATE_ID = 357;
    private final int ORB_ID = 6822;

    @Override
    public boolean activate()
    {
        try
        {
            for (SceneObject so : MinimalDungeoneering.getAllSceneObjects())
            {
                if (so.getId() == CRATE_ID
                        && so.getLocation().equals(CRATE_TILE)
                        && Inventory.getCount(ORB_ID) == 0)
                {
                    crate = so;

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
        if (!Players.getMyPlayer().getLocation().equals(ORB_TILES[5]))
        {
            MinimalDungeoneering.status = "Walking to orb";

            ORB_PATH.traverse();

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Players.getMyPlayer().getLocation() == ORB_TILES[5];
                }
            }, 500);
        }

        if (Players.getMyPlayer().getLocation().equals(ORB_TILES[5]))
        {
            MinimalDungeoneering.status = "Grabbing orb";

            crate.interact(0);

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Inventory.getCount(ORB_ID) > 0;
                }
            }, 5000);
        }
    }
}