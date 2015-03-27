package org.parabot.minimal.minimaldungeoneering;

import org.parabot.environment.api.utils.Time;
import org.parabot.environment.input.Mouse;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.Loader;
import org.rev317.min.api.methods.Npcs;

import java.awt.*;
import java.lang.reflect.Method;

/**
 * This class is in charge of knowing when to force a logout to exit the dungeon
 * The cause of these things is bosses turning invisible (good old Ikov) and
 * more recently bosses becoming unkillable but still having 0 HP
 * The reason for the first sleep condition is to ensure no mistakes were made
 * The variable monsterVisible is controlled using a message listener
 */
public class ForceExit implements Strategy
{
    private final int[] BOSS_IDS;
    private final int THOK_ID;

    public ForceExit(int[] BOSS_IDS, int THOK_ID)
    {
        this.BOSS_IDS = BOSS_IDS;
        this.THOK_ID = THOK_ID;
    }

    @Override
    public boolean activate()
    {
        return MinimalDungeoneering.monsterVisible
                && Npcs.getNearest(BOSS_IDS).length == 0;
    }

    @Override
    public void execute()
    {
        MinimalDungeoneering.status = "Nulled boss";

        Time.sleep(new SleepCondition()
        {
            @Override
            public boolean isValid()
            {
                return Npcs.getNearest(BOSS_IDS).length > 0
                        || Npcs.getNearest(THOK_ID).length > 0;
            }
        }, 5000);

        if (Npcs.getNearest(BOSS_IDS).length == 0
                || Npcs.getNearest(THOK_ID).length == 0)
        {
            MinimalDungeoneering.status = "Exiting";

            MinimalDungeoneering.monsterVisible = false;
            
            MinimalDungeoneering.forceLogout();

            Time.sleep(5000);
        }
    }
}