package org.parabot.minimal.minimaldungeoneering;

public enum Mode
{
    SECOND_FLOOR(2),
    THIRD_FLOOR(3);

    @Override
    public String toString()
    {
        return name().charAt(0) + name().substring(1).toLowerCase().replace("_", " ");
    }

    private int floorNumber;

    Mode(int floorNumber)
    {
        this.floorNumber = floorNumber;
    }

    public int getFloorNumber()
    {
        return floorNumber;
    }
}
