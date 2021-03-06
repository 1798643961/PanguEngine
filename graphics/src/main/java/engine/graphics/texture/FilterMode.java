package engine.graphics.texture;

public enum FilterMode {
    LINEAR(false),
    NEAREST(false),
    NEAREST_MIPMAP_NEAREST(true),
    LINEAR_MIPMAP_NEAREST(true),
    NEAREST_MIPMAP_LINEAR(true),
    LINEAR_MIPMAP_LINEAR(true);

    private final boolean mipmap;

    FilterMode(boolean mipmap) {
        this.mipmap = mipmap;
    }

    public boolean isMipmap() {
        return mipmap;
    }
}
