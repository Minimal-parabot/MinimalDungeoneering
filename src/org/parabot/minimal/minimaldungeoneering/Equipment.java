package org.parabot.minimal.minimaldungeoneering;

import java.awt.*;

public enum Equipment
{
    PRIMAL_FULL_HELM(16712, MinimalDungeoneering.getImage("http://i.imgur.com/KPvgUQm.png")),
    PRIMAL_PLATEBODY(17260, MinimalDungeoneering.getImage("http://i.imgur.com/m9YoHpy.png")),
    PRIMAL_PLATELEGS(16910, MinimalDungeoneering.getImage("http://i.imgur.com/tTTKQzs.png")),
    PRIMAL_2H_SWORD(16690, MinimalDungeoneering.getImage("http://i.imgur.com/NC29pAp.png"));

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