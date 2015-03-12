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
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

@ScriptManifest(author = "Minimal",
        category = Category.DUNGEONEERING,
        description = "A dungeoneering script that completes Floor 2 on Ikov.",
        name = "Minimal Dungeoneering",
        servers = { "Ikov" },
        version = 1.0)

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
        boolean normalPrayers;
        
        MinimalDungeoneeringGUI gui = new MinimalDungeoneeringGUI();
        gui.setVisible(true);

        while(gui.isVisible())
        {
            sleep(500);
        }

        armor = gui.getArmor();
        normalPrayers = gui.getPrayer();

        strategies.add(new Relog());
        strategies.add(new EnterDungeon(THOK_ID));
        strategies.add(new ForceExit(BOSS_IDS, THOK_ID));
        strategies.add(new EquipGear(armor, normalPrayers));
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

    /**
     * Gets every loaded scene object in game
     *
     * @return every loaded scene object in game
     */
    public static final SceneObject[] getAllSceneObjects() {
        ArrayList<SceneObject> sceneObjects = new ArrayList<>();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                final Collection<SceneObject> sceneObjectsAtTile = getSceneObjectsAtTile(x, y, true);
                if (sceneObjectsAtTile != null && !sceneObjectsAtTile.isEmpty()) {
                    sceneObjects.addAll(sceneObjectsAtTile);
                }
            }
        }
        return sceneObjects.toArray(new SceneObject[sceneObjects.size()]);
    }

    /**
     * Gets all sceneobjects at a tile
     *
     * @param x
     * @param y
     * @param useCached
     *
     * @return array of sceneobjects, or null if there aren't any
     */
    public static final Collection<SceneObject> getSceneObjectsAtTile(int x, int y, boolean useCached)
    {
        Ground sceneTile = Loader.getClient().getScene().getGroundArray()[Game.getPlane()][x][y];
        ArrayList<SceneObject> sceneObjects = null;

        if (sceneTile != null)
        {
            final SceneObjectTile[] interactiveObjects = sceneTile.getInteractiveObjects();

            if (interactiveObjects != null)
            {
                for (final SceneObjectTile interactiveObject : interactiveObjects)
                {
                    if (interactiveObject != null)
                    {
                        if (sceneObjects == null)
                        {
                            sceneObjects = new ArrayList<>();
                        }
                        sceneObjects.add(new SceneObject(interactiveObject, SceneObject.TYPE_INTERACTIVE));
                    }
                }
            }
        }

        SceneObjectTile sceneObjectTile = sceneTile.getWallDecoration();

        if (sceneObjectTile != null)
        {
            if (sceneObjects == null)
            {
                sceneObjects = new ArrayList<>();
            }

            sceneObjects.add(new SceneObject(sceneObjectTile, SceneObject.TYPE_WALLDECORATION));
        }

        sceneObjectTile = sceneTile.getWallObject();

        if (sceneObjectTile != null)
        {
            if (sceneObjects == null)
            {
                sceneObjects = new ArrayList<>();
            }

            sceneObjects.add(new SceneObject(sceneObjectTile, SceneObject.TYPE_WALL));
        }

        sceneObjectTile = sceneTile.getGroundDecoration();

        if (sceneObjectTile != null)
        {
            if (sceneObjects == null)
            {
                sceneObjects = new ArrayList<>();
            }

            sceneObjects.add(new SceneObject(sceneObjectTile, SceneObject.TYPE_GROUNDDECORATION));
        }

        return sceneObjects;
    }
}