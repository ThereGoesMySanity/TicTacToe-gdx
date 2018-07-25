package tgms.ttt.PlatformInterfaces;

public abstract class Platform {
    public enum Features {ONLINE, OS};
    protected Online online;
    protected OSQuery os;

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
            default:
                return false;
        }
    }
}
