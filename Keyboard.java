
public class Keyboard
{
    private static boolean wDown = false;
    private static boolean aDown = false;
    private static boolean sDown = false;
    private static boolean dDown = false;
    private static boolean spaceDown = false;

    public static boolean getW()
    {
        return wDown;
    }

    public static boolean getA()
    {
        return aDown;
    }

    public static boolean getS()
    {
        return sDown;
    }

    public static boolean getD()
    {
        return dDown;
    }

    public static boolean getSpace()
    {
        return spaceDown;
    }

    public static void setW(boolean state)
    {
        wDown = state;
    }

    public static void setA(boolean state)
    {
        aDown = state;
    }

    public static void setS(boolean state)
    {
        sDown = state;
    }

    public static void setD(boolean state)
    {
        dDown = state;
    }

    public static void setSpace(boolean state)
    {
        spaceDown = state;
    }

}
