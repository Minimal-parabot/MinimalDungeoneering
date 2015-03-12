package org.parabot.minimal.minimaldungeoneering;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.api.methods.Npcs;
import org.rev317.min.api.wrappers.Npc;

/**
 * This class is in charge of waiting while in combat
 * It uses n.isInCombat because sometimes the player can be in combat with other Npcs
 * monsterVisible is set to false because once a boss's health hits 0, a bug can occur where it never dies
 */
public class WaitBoss implements Strategy
{
    private final int[] BOSS_IDS;

    public WaitBoss(int[] BOSS_IDS)
    {
        this.BOSS_IDS = BOSS_IDS;
    }

    private Npc boss;

    @Override
    public boolean activate()
    {
        for (Npc n : Npcs.getNearest(BOSS_IDS))
        {
            if (n != null
                    && n.isInCombat())
            {
                boss = n;

                return true;
            }
        }

        return false;
    }

    public void execute()
    {
        MinimalDungeoneering.status = "Waiting";

        Time.sleep(new SleepCondition()
        {
            @Override
            public boolean isValid()
            {
                return Npcs.getNearest(BOSS_IDS).length == 0
                        || boss.getHealth() == 0;
            }
        }, 1000);

        if (boss.getHealth() == 0)
            MinimalDungeoneering.monsterVisible = false;

        if (Npcs.getNearest(BOSS_IDS).length == 0)
            Time.sleep(1500);
    }
}