package tgms.ttt.PlatformInterfaces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public abstract class Platform {

    public static int FONT_SIZE = 24;

    public enum Features {ONLINE, OS, THREAD};
    protected Online online;
    protected OSQuery os;
    protected boolean threading = true;
    protected BitmapFont font;

    public BitmapFont getFont() {
        if (font == null) {
            Gdx.app.debug("test", Gdx.files.internal("fonts/default.ttf").exists() + " font");
            FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/default.ttf"));
            FreeTypeFontParameter param = new FreeTypeFontParameter();
            param.size = FONT_SIZE;
            param.flip = true;
            font = gen.generateFont(param);
            gen.dispose();
        }
        return font;
    }

    public Online getOnline() {
        return online;
    }

    public OSQuery getOS() {
        return os;
    }

    public boolean isSupported(Features f) {
        switch (f) {
            case OS:
                return os != null;
            case ONLINE:
                return online != null;
            case THREAD:
                return threading;
            default:
                return false;
        }
    }
}
