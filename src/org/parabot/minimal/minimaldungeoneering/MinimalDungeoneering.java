package org.parabot.minimal.minimaldungeoneering;

import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Timer;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.Loader;
import org.rev317.min.accessors.Ground;
import org.rev317.min.accessors.SceneObjectTile;
import org.rev317.min.api.events.MessageEvent;
import org.rev317.min.api.events.listeners.MessageListener;
import org.rev317.min.api.methods.*;
import org.rev317.min.api.wrappers.SceneObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

@ScriptManifest(author = "Minimal",
        category = Category.DUNGEONEERING,
        description = "A dungeoneering script that completes Floor 2 on Ikov.",
        name = "Minimal Dungeoneering",
        servers = { "Ikov" },
        version = 1.1)

public class MinimalDungeoneering extends Script implements Paintable, MessageListener
{
    private final ArrayList<Strategy> strategies = new ArrayList<>();

    private Timer timer;

    public static String status = "";

    public static boolean monsterVisible = false;

    /**
     * 9916 - Luminescent icefiend
     * 9989 - Asta Frost Web
     * 10044 - Icy Bones
     * 10064 - Hobgoblin Geomancer
     * 10110 - Bulkwark Beast
     * 10116 - Unholy Cursebearer
     */
    private final int[] BOSS_IDS = { 9916, 9989, 10044, 10064, 10110, 10116 };

    private final int THOK_ID = 9713;
    private final int STARTING_EXP = Skill.getCurrentExperience(23);
    private int floorsCompleted = 0;

    @Override
    public boolean onExecute()
    {
        timer = new Timer();
        int[] armor;

        MinimalDungeoneeringGUI gui = new MinimalDungeoneeringGUI();
        gui.setVisible(true);

        while(gui.isVisible())
        {
            sleep(500);
        }

        armor = gui.getArmor();

        strategies.add(new Relog());
        strategies.add(new EnterDungeon(THOK_ID));
        strategies.add(new ForceExit(BOSS_IDS, THOK_ID));
        strategies.add(new EquipGear(armor));
        strategies.add(new WaitBoss(BOSS_IDS));
        strategies.add(new KillBoss(BOSS_IDS));
        strategies.add(new GrabRock());
        strategies.add(new GrabOrb());
        strategies.add(new TouchShrine(BOSS_IDS));
        provide(strategies);
        return true;
    }

    @Override
    public void paint(Graphics g)
    {
        // g.drawImage(IMG, 549, 263, null);

        g.setFont(new Font("Helvetica", Font.PLAIN, 14));
        g.setColor(new Color(31, 34, 50));

        g.drawString("Time: " + timer.toString(), 560, 327);
        g.drawString("Status: " + status, 560, 380);
        g.drawString("Floors done: " + floorsCompleted, 560, 433);
        g.drawString("Exp: " + (Skill.getCurrentExperience(23) - STARTING_EXP), 15, 15);
    }

    @Override
    public void messageReceived(MessageEvent m)
    {
        if (m.getType() == 0)
        {
            String message = m.getMessage().toLowerCase();

            if (message.contains("object"))
            {
                status = "Nulled";
                forceLogout();
            }

            if (message.contains("completed a dungeon"))
            {
                floorsCompleted++;

                monsterVisible = false;
            }

            if (message.contains("your boss is"))
            {
                monsterVisible = true;
            }
        }
    }

    // Gets an image from a url
    public static Image getImage(String url)
    {
        try
        {
            return ImageIO.read(new URL(url));
        }
        catch(IOException e)
        {
            return null;
        }
    }

    // Forces the user to log out
    public static void forceLogout()
    {
        try
        {
            Class<?> c = Loader.getClient().getClass();
            Method m = c.getDeclaredMethod("am");
            m.setAccessible(true);
            m.invoke(Loader.getClient());
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }
}