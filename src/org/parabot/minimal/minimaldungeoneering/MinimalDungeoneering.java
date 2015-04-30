package org.parabot.minimal.minimaldungeoneering;

import org.parabot.core.ui.Logger;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.api.utils.Timer;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.parabot.environment.scripts.framework.Strategy;
import org.rev317.min.Loader;
import org.rev317.min.api.events.MessageEvent;
import org.rev317.min.api.events.listeners.MessageListener;
import org.rev317.min.api.methods.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

@ScriptManifest(author = "Minimal",
        category = Category.DUNGEONEERING,
        description = "A dungeoneering script that completes Floor 2 on Ikov.",
        name = "Minimal Dungeoneering",
        servers = { "Ikov" },
        version = 0.4)

public class MinimalDungeoneering extends Script implements Paintable, MessageListener
{
    private final ArrayList<Strategy> strategies = new ArrayList<>();

    private static final Image IMG = getImage("http://i.imgur.com/08IiCkK.png");

    private Timer timer = new Timer();
    private Timer secondaryTimer = new Timer(240000);

    public static Mode mode;

    /**
     * 9916 - Luminescent icefiend
     * 9934 - Plane-freezer Lakhrahnaz
     * 9989 - Asta Frost Web
     * 10044 - Icy Bones
     * 10064 - Hobgoblin Geomancer
     * 10110 - Bulkwark Beast
     * 10116 - Unholy Cursebearer
     */
    private static final int[] BOSS_IDS = { 9916, 9934, 9989, 10044, 10064, 10110, 10116 };

    private static final int STARTING_EXP = Skill.getCurrentExperience(23);
    private static final int THOK_ID = 9713;
    private static final int ROCK_ID = 1481;
    private static final int ORB_ID = 6822;
    private static final int PASSAGEWAY_ID = 7219;

    private int floorsCompleted = 0;

    @Override
    public boolean onExecute()
    {
        JOptionPane.showMessageDialog(null, "Post proggies on the thread!");

        MinimalDungeoneeringGUI gui = new MinimalDungeoneeringGUI();
        gui.setVisible(true);

        int[] armor;

        while(gui.isVisible())
        {
            sleep(500);
        }

        armor = gui.getArmor();

        strategies.add(new Relog());
        strategies.add(new EnterDungeon(THOK_ID));
        strategies.add(new EquipGear(armor));
        strategies.add(new Consume());
        strategies.add(new Boss(BOSS_IDS));

        if (mode == Mode.SECOND_FLOOR)
        {
            strategies.add(new GrabRock(ROCK_ID));
            strategies.add(new GrabOrb(ROCK_ID, ORB_ID));
            strategies.add(new TouchShrine(BOSS_IDS, ROCK_ID, ORB_ID));
        }
        else if (mode == Mode.THIRD_FLOOR)
        {
            strategies.add(new FirstStage());
            strategies.add(new SecondStage());
            strategies.add(new ThirdStage());
            strategies.add(new FourthStage(PASSAGEWAY_ID));
            strategies.add(new FifthStage(PASSAGEWAY_ID));
        }

        provide(strategies);
        return true;
    }

    @Override
    public void paint(Graphics g)
    {
        g.setFont(new Font("Helvetica", Font.PLAIN, 14));
        g.setColor(new Color(31, 34, 50));

        g.drawImage(IMG, 546, 209, null);
        g.drawString("Secondary timer: " + secondaryTimer.toString(), 15, 15);
        g.drawString("Time: " + timer.toString(), 555, 271);
        g.drawString("Floors: " + floorsCompleted, 555, 330);
        g.drawString("Exp: " + getPerHour(Skill.getCurrentExperience(23) - STARTING_EXP), 555, 389);
        g.drawString("Tokens: " + getPerHour((Skill.getCurrentExperience(23) - STARTING_EXP) / 10), 555, 448);
    }

    @Override
    public void messageReceived(MessageEvent m)
    {
        if (secondaryTimer.getRemaining() <= 0)
        {
            Logger.addMessage("Secondary timer has ran out - the dungeon may have been bugged.");

            secondaryTimer.restart();

            forceLogout();
        }

        if (m.getType() == 0)
        {
            String message = m.getMessage().toLowerCase();

            if (message.contains("object"))
            {
                Logger.addMessage("Account was nulled");

                secondaryTimer.restart();

                forceLogout();
            }
            else if (message.contains("completed a dungeon"))
            {
                floorsCompleted++;

                Logger.addMessage("Secondary timer reset");

                secondaryTimer.restart();
            }
        }
    }

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

    private String formatNumber(double number)
    {
        DecimalFormat normal = new DecimalFormat("#,###.0");
        DecimalFormat goldFarmer = new DecimalFormat("#,###.00");

        if (number >= 1000 && number < 1000000)
        {
            return normal.format(number / 1000) + "K";
        }
        else if (number >= 1000000 && number < 1000000000)
        {
            return normal.format(number / 1000000) + "M";
        }
        else if (number >= 1000000000)
        {
            return goldFarmer.format(number / 1000000000) + "B";
        }

        return "" + number;
    }

    private String getPerHour(int number)
    {
        return formatNumber(number) + "(" + formatNumber(timer.getPerHour(number)) + ")";
    }
}