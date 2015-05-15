package org.parabot.minimal.minimaldungeoneering;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Inventory;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.methods.SceneObjects;
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
    private final int rockId;
    private final int orbId;

    public GrabOrb(int rockId, int orbId)
    {
        this.rockId = rockId;
        this.orbId = orbId;
    }
    private static final Tile[] ORB_TILES = { new Tile(2611, 9835),
                                       new Tile(2605, 9837),
                                       new Tile(2598, 9837),
                                       new Tile(2588, 9837),
                                       new Tile(2581, 9842),
                                       new Tile(2576, 9842),
                                       new Tile(2570, 9846),
                                       new Tile(2565, 9849) };

    private static final TilePath ORB_PATH = new TilePath(ORB_TILES);

    private static final Tile CRATE_TILE = new Tile(2563, 9847);

    private static final int CRATE_ID = 357;


    @Override
    public boolean activate()
    {
        return Inventory.contains(rockId)
                && !Inventory.contains(orbId);
    }

    @Override
    public void execute()
    {
        SceneObject crate = null;

        try
        {
            for (SceneObject so : SceneObjects.getAllSceneObjects())
            {
                if (so.getId() == CRATE_ID && so.getLocation().equals(CRATE_TILE))
                {
                    crate = so;
                }
            }
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();

            Logger.addMessage("NullPointerException when getting all scene objects", false);
        }

        if (crate == null || !Players.getMyPlayer().getLocation().equals(ORB_TILES[ORB_TILES.length - 1]))
        {
            Logger.addMessage("Walking to orb", true);

            ORB_PATH.traverse();

            Time.sleep(1000);
        }

        if (crate != null && Players.getMyPlayer().getLocation().equals(ORB_TILES[ORB_TILES.length - 1]))
        {
            Logger.addMessage("Grabbing orb", true);

            crate.interact(SceneObjects.Option.FIRST);

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Inventory.contains(orbId);
                }
            }, 3000);
        }
    }
}