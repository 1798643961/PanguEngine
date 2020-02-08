package engine.graphics.texture;

import engine.graphics.RenderEngine;
import engine.graphics.image.ReadOnlyImage;
import engine.util.Color;

public interface Texture2D extends Texture {

    static Builder builder() {
        return RenderEngine.getManager().getResourceFactory().createTexture2DBuilder();
    }

    int getWidth();

    int getHeight();

    interface Builder {
        Builder magFilter(FilterMode mode);

        Builder minFilter(FilterMode mode);

        Builder wrapS(WrapMode mode);

        Builder wrapT(WrapMode mode);

        Builder generateMipmap();

        Builder borderColor(Color color);

        Texture2D build(ReadOnlyImage image);
    }
}