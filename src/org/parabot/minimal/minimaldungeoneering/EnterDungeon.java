package org.parabot.minimal.minimaldungeoneering;

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

    private Npc thok;

    @Override
    public boolean activate()
    {
        for (Npc n : Npcs.getNearest(THOK_ID))
        {
            if (n != null)
            {
                thok = n;

                return true;
            }
        }

        return false;
    }

    @Override
    public void execute()
    {
        if (Game.getOpenBackDialogId() == -1)
        {
            MinimalDungeoneering.status = "Talking to Thok";

            thok.interact(0);

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Game.getOpenBackDialogId() == 4887;
                }
            }, 5000);
        }

        if (Game.getOpenBackDialogId() == 4887)
        {
            MinimalDungeoneering.status = "Continuing";

            Menu.sendAction(679, -1, -1, 4982);

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Game.getOpenBackDialogId() == 2469;
                }
            }, 1000);

            Time.sleep(250);
        }

        if (Game.getOpenBackDialogId() == 2469
                && Loader.getClient().getInterfaceCache()[2471].getMessage().equals("Start Dungeoneering"))
        {
            MinimalDungeoneering.status = "Starting";

            Menu.sendAction(315, -1, -1, 2471);

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Game.getOpenBackDialogId() == 2459;
                }
            }, 1000);

            Time.sleep(250);
        }

        if (Game.getOpenBackDialogId() == 2459)
        {
            MinimalDungeoneering.status = "Single";

            Menu.sendAction(315, -1, -1, 2461);

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Game.getOpenBackDialogId() == 2469;
                }
            }, 1000);

            Time.sleep(250);
        }

        if (Game.getOpenBackDialogId() == 2469)
        {
            MinimalDungeoneering.status = "Floor 2";

            Menu.sendAction(315, -1, -1, 2472);

            Time.sleep(new SleepCondition()
            {
                @Override
                public boolean isValid()
                {
                    return Npcs.getNearest(THOK_ID).length == 0;
                }
            }, 2500);

            Time.sleep(250);
        }

        if (Npcs.getNearest(THOK_ID).length == 0)
        {
            MinimalDungeoneering.status = "Waiting";

            Time.sleep(8500);
        }
    }
}