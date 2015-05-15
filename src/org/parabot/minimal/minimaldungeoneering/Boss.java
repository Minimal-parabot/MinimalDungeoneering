package org.parabot.minimal.minimaldungeoneering;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Npcs;
import org.rev317.min.api.methods.Players;
import org.rev317.min.api.wrappers.Area;
import org.rev317.min.api.wrappers.Npc;
import org.rev317.min.api.wrappers.Tile;

public class Boss implements Strategy
{
    private final int[] bossIds;

    public Boss(int[] bossIds)
    {
        this.bossIds = bossIds;
    }

    private static final Area SIXTH_STAGE_AREA = new Area(new Tile(2949, 5111),
            new Tile(2949, 5097),
            new Tile(2976, 5097),
            new Tile(2976, 5111));

    @Override
    public boolean activate()
    {
        return MinimalDungeoneering.mode == Mode.SECOND_FLOOR && Npcs.getNearest(bossIds).length > 0
                || MinimalDungeoneering.mode == Mode.THIRD_FLOOR && SIXTH_STAGE_AREA.contains(Players.getMyPlayer().getLocation());
    }

    public void execute()
    {
        final Npc boss = Npcs.getClosest(bossIds);

        if (boss != null)
        {
            if (!boss.isInCombat())
            {
                Logger.addMessage("Attacking boss", true);

                boss.interact(Npcs.Option.ATTACK);

                Time.sleep(new SleepCondition()
                {
                    @Override
                    public boolean isValid()
                    {
                        return boss.isInCombat();
                    }
                }, 2000);
            }

            if (boss.isInCombat())
            {
                Logger.addMessage("Waiting for boss to die", true);

                Time.sleep(new SleepCondition()
                {
                    @Override
                    public boolean isValid()
                    {
                        return Npcs.getNearest(bossIds).length == 0;
                    }
                }, 1000);

                if (Npcs.getNearest(bossIds).length == 0)
                {
                    Time.sleep(1000);
                }
            }
        }
    }
}