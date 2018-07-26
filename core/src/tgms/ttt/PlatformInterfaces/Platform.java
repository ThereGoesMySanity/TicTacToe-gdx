package tgms.ttt.PlatformInterfaces;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public abstract class Platform {

    public static int FONT_SIZE = 24;

    public enum Features {ONLINE, OS, THREAD};
    protected Online online;
    protected OSQuery os;
    protected Threading thread;
    protected BitmapFont font;

    public abstract BitmapFont getFont();

    public Online getOnline() {
        return online;
    }

    public OSQuery getOS() {
        return os;
    }
    
    public Threading getThread() {
    	return thread;
    }

    public boolean isSupported(Features f) {
        switch (f) {
            case OS:
                return os != null;
            case ONLINE:
                return online != null;
            case THREAD:
                return thread != null;
            default:
                return false;
        }
    }
}
