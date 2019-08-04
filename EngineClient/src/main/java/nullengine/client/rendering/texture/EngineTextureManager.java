package nullengine.client.rendering.texture;

import nullengine.Platform;
import nullengine.client.asset.*;
import nullengine.client.asset.exception.AssetLoadException;
import nullengine.client.asset.reloading.AssetReloadScheduler;
import nullengine.client.asset.source.AssetSourceManager;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class EngineTextureManager implements TextureManager, AssetProvider<GLTexture> {

    private final List<Asset<GLTexture>> assets = new ArrayList<>();
    private final Map<TextureAtlasName, TextureAtlasImpl> texturesAtlases = new HashMap<>();

    private final GLTexture whiteTexture;

    private AssetSourceManager sourceManager;

    public EngineTextureManager() {
        this.whiteTexture = getTextureDirect(new TextureBuffer(2, 2, 0xffffffff));
    }

    @Override
    public GLTexture getTextureDirect(TextureBuffer buffer) {
        return GLTexture.of(buffer.getWidth(), buffer.getHeight(), buffer.getBuffer());
    }

    @Override
    public TextureAtlasPart addTextureToAtlas(AssetPath path, TextureAtlasName type) {
        return texturesAtlases.computeIfAbsent(type, key -> new TextureAtlasImpl()).addTexture(path);
    }

    @Override
    public TextureAtlas getTextureAtlas(TextureAtlasName type) {
        return texturesAtlases.computeIfAbsent(type, key -> new TextureAtlasImpl());
    }

    @Override
    public void reloadTextureAtlas(TextureAtlasName type) {
        try {
            if (texturesAtlases.containsKey(type)) {
                texturesAtlases.get(type).reload();
            }
        } catch (IOException e) {
            Platform.getLogger().warn(String.format("Cannot reload texture atlas %s.", type), e);
        }
    }

    @Override
    public GLTexture getWhiteTexture() {
        return whiteTexture;
    }

    @Override
    public void init(AssetManager manager, AssetType<GLTexture> type) {
        sourceManager = manager.getSourceManager();
        manager.getReloadManager().addFirst("Texture", this::reload);
    }

    @Override
    public void register(Asset<GLTexture> asset) {
        assets.add(asset);
    }

    @Override
    public void unregister(Asset<GLTexture> asset) {
        var glTexture = asset.get();
        if (glTexture != null) {
            glTexture.dispose();
        }
        assets.remove(asset);
    }

    private void reload(AssetReloadScheduler scheduler) {
        assets.forEach(asset -> {
            var glTexture = asset.get();
            if (glTexture != null) {
                glTexture.dispose();
            }
            asset.reload();
        });
        texturesAtlases.keySet().forEach(this::reloadTextureAtlas);
    }

    @Nonnull
    @Override
    public GLTexture loadDirect(AssetPath path) {
        Optional<Path> localPath = sourceManager.getPath(path);
        if (localPath.isEmpty()) {
            throw new AssetLoadException("Cannot loadDirect texture because missing asset. Path: " + path);
        }

        try (var channel = Files.newByteChannel(localPath.get())) {
            var buffer = ByteBuffer.allocateDirect(Math.toIntExact(channel.size()));
            channel.read(buffer);
            buffer.flip();
            return getTextureDirect(TextureBuffer.create(buffer));
        } catch (IOException e) {
            throw new AssetLoadException("Cannot loadDirect texture because catch exception. Path: " + path, e);
        }
    }

    @Override
    public void dispose() {
        assets.forEach(asset -> {
            var glTexture = asset.get();
            if (glTexture != null) {
                glTexture.dispose();
            }
        });
    }
}
