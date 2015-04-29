package org.parabot.minimal.minimaldungeoneering;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.utils.Time;
import org.parabot.environment.scripts.framework.SleepCondition;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.Loader;
import org.rev317.min.api.methods.Game;
import org.rev317.min.api.methods.Menu;
import org.rev317.min.api.methods.Npcs;
import org.rev317.min.api.wrappers.Npc;

/**
 * This class is in charge of entering a dungeon
 * It cycles through dialogs and joins the second dungeon
 * The final sleep is a mandatory timer Ikov puts on the user before they can do anything
 */
public class EnterDungeon implements Strategy
{
    private final int THOK_ID;

    public EnterDungeon(int THOK_ID)
    {
        this.THOK_ID = THOK_ID;
    }

    @Override
    public boolean activate()
    {
        return Npcs.getNearest(THOK_ID).length > 0
                && Npcs.getClosest(THOK_ID).distanceTo() < 20;
    }

    @Override
    public void execute()
    {
        if (Game.getOpenBackDialogId() == -1 || Game.getOpenBackDialogId() == 4900)
        {
            Logger.addMessage("Talking to Thok");

            Npc thok = Npcs.getClosest(THOK_ID);

            if (thok != null)
            {
                thok.interact(Npcs.Option.THIRD);

                Time.sleep(new SleepCondition()
                {
                    @Override
                    public boolean isValid()
                    {
                        return Game.getOpenBackDialogId() == 2469;
                    }
                }, 5000);
            }
        }

        if (Game.getOpenBackDialogId() == 2469)
        {
            Logger.addMessage("Entering " + MinimalDungeoneering.mode);

            if (MinimalDungeoneering.mode == Mode.SECOND_FLOOR)
            {
                Menu.sendAction(315, -1, -1, 2472);
            }
            else if (MinimalDungeoneering.mode == Mode.THIRD_FLOOR)
            {
                Menu.sendAction(315, -1, -1, 2473);
            }

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Npcs.getNearest(THOK_ID).length == 0;
                }
            }, 5000);
        }

        if (Npcs.getNearest(THOK_ID).length == 0)
        {
            Logger.addMessage("Waiting for 8 seconds");

            Time.sleep(8000);
        }
    }
}