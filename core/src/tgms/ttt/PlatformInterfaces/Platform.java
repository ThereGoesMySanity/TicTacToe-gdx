package tgms.ttt.PlatformInterfaces;

public abstract class Platform {
    public enum Features {ONLINE, OS, THREAD};
    protected Online online;
    protected OSQuery os;
    protected boolean threading = true;

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
