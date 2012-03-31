package skia.javacpp.gm;

public class GMRegistry {
    public static interface Factory {
        GM apply();
    }
}
