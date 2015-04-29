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
import java.util.ArrayList;

@ScriptManifest(author = "Minimal",
        category = Category.DUNGEONEERING,
        description = "A dungeoneering script that completes Floor 2 on Ikov.",
        name = "Minimal Dungeoneering",
        servers = { "Ikov" },
        version = 0.3)

public class MinimalDungeoneering extends Script implements Paintable, MessageListener
{
    private final ArrayList<Strategy> strategies = new ArrayList<>();

    private static final Image IMG = getImage("http://i.imgur.com/08IiCkK.png");

    private Timer timer = new Timer();

    public static Mode mode;

    private static final int THOK_ID = 9713;
    private static final int STARTING_EXP = Skill.getCurrentExperience(23);
    private int floorsCompleted = 0;
    private int deathCount;

    public static boolean monsterVisible = false;

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
        strategies.add(new Boss());

        if (mode == Mode.SECOND_FLOOR)
        {
            strategies.add(new GrabRock());
            strategies.add(new GrabOrb());
            strategies.add(new TouchShrine());
        }
        else if (mode == Mode.THIRD_FLOOR)
        {
            strategies.add(new Consume());
            strategies.add(new FirstStage());
            strategies.add(new SecondStage());
            strategies.add(new ThirdStage());
            strategies.add(new FourthStage());
            strategies.add(new FifthStage());
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
        g.drawString("Time: " + timer.toString(), 555, 271);
        g.drawString("Floors done: " + floorsCompleted, 555, 330);
        g.drawString("Exp: " + (Skill.getCurrentExperience(23) - STARTING_EXP), 555, 389);
        g.drawString("Deaths: " + deathCount, 555, 448);
    }

    @Override
    public void messageReceived(MessageEvent m)
    {
        if (m.getType() == 0)
        {
            String message = m.getMessage().toLowerCase();

            if (message.contains("object"))
            {
                Logger.addMessage("Account was nulled");

                forceLogout();
            }
            else if (message.contains("completed a dungeon"))
            {
                floorsCompleted++;

                monsterVisible = false;
            }
            else if (message.contains("your boss is"))
            {
                monsterVisible = true;
            }
            else if (message.contains("oh dear,") || message.contains("lifepoints!"))
            {
                deathCount++;
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