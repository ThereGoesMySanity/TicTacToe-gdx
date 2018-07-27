package tgms.ttt.PlatformInterfaces;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import tgms.ttt.PlatformInterfaces.Feature.*;

public abstract class Platform {

    public static int FONT_SIZE = 24;

    public Online online;
    public OSQuery os;
    public Threading thread;
    
    protected BitmapFont font;

    public abstract BitmapFont getFont();
    
}
