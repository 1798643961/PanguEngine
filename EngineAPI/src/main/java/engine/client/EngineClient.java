package engine.client;

import engine.Engine;
import engine.client.asset.AssetManager;
import engine.client.asset.source.AssetSource;
import engine.client.game.GameClient;
import engine.graphics.RenderManager;
import engine.client.settings.EngineSettings;
import engine.client.sound.ALSoundManager;

public interface EngineClient extends Engine {

    Thread getClientThread();

    boolean isClientThread();

    AssetManager getAssetManager();

    AssetSource getEngineAssetSource();

    RenderManager getRenderManager();

    ALSoundManager getSoundManager();

    @Override
    GameClient getCurrentGame();

    EngineSettings getSettings();
}