package org.parabot.minimal.minimaldungeoneering;

import java.awt.*;

public enum Equipment
{
    PRIMAL_FULL_HELM(16712, MinimalDungeoneering.getImage("http://i.imgur.com/KPvgUQm.png")),
    PRIMAL_PLATEBODY(17260, MinimalDungeoneering.getImage("http://i.imgur.com/m9YoHpy.png")),
    PRIMAL_PLATELEGS(16910, MinimalDungeoneering.getImage("http://i.imgur.com/tTTKQzs.png")),
    PRIMAL_2H_SWORD(16690, MinimalDungeoneering.getImage("http://i.imgur.com/NC29pAp.png")),
    ZEPHYRIUM_FULL_HELM(16702, MinimalDungeoneering.getImage("http://i.imgur.com/wkKnpUX.png")),
    ZEPHYRIUM_PLATEBODY(17250, MinimalDungeoneering.getImage("http://i.imgur.com/uI1DSKs.png")),
    ZEPHYRIUM_PLATELEGS(16680, MinimalDungeoneering.getImage("http://i.imgur.com/6WpzQRY.png")),
    ZEPHYRIUM_RAPIER(16946, MinimalDungeoneering.getImage("http://i.imgur.com/grQ2PV1.png")),
    ZEPHYRIUM_LONGSWORD(16394, MinimalDungeoneering.getImage("http://i.imgur.com/K130ZFA.png")),
    ZEPHYRIUM_MAUL(16416, MinimalDungeoneering.getImage("http://i.imgur.com/paOEo5s.png")),
    ZEPHYRIUM_2H_SWORD(16900, MinimalDungeoneering.getImage("http://i.imgur.com/61qxP7a.png"));

    @Override
    public String toString()
    {
        return name().charAt(0) + name().substring(1).toLowerCase().replace("_", " ");
    }

    private int id;
    private Image image;

    Equipment(int id, Image image)
    {
        this.id = id;
        this.image = image;
    }

    public int getId()
    {
        return id;
    }

    public Image getImage()
    {
        return image;
    }
}