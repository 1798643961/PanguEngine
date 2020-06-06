package engine.client.game;

import engine.client.ClientEngine;
import engine.client.player.ClientPlayer;
import engine.client.player.ClientPlayerImpl;
import engine.entity.Entity;
import engine.event.game.GameTerminationEvent;
import engine.game.GameData;
import engine.game.ServerGame;
import engine.player.Player;
import engine.player.Profile;
import engine.world.World;
import engine.world.WorldCommon;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class StandaloneClientGame extends ServerGame implements ClientGame {

    private final ClientEngine clientEngine;

    private ClientPlayer clientPlayer;
    protected final Set<Player> players = new HashSet<>();

    public StandaloneClientGame(ClientEngine clientEngine, Path storagePath, GameData data) {
        super(clientEngine, storagePath, data, null);
        this.clientEngine = clientEngine;
    }

    @Nonnull
    @Override
    public ClientEngine getEngine() {
        return clientEngine;
    }

    @Nonnull
    @Override
    public ClientPlayer joinPlayer(Profile profile, Entity controlledEntity) {
        if (clientPlayer != null) {
            throw new IllegalStateException("Cannot join player twice on client game");
        }
        clientPlayer = new ClientPlayerImpl(profile, controlledEntity);
        players.add(clientPlayer);
        return clientPlayer;
    }

    /**
     * Get player client
     */
    @Nonnull
    @Override
    public ClientPlayer getClientPlayer() {
        if (clientPlayer != null) {
            return clientPlayer;
        }
        throw new IllegalStateException("The player hasn't initialize");
    }

    /**
     * @return the client world
     */
    @Nonnull
    @Override
    public World getClientWorld() {
        if (clientPlayer != null) {
            return clientPlayer.getWorld();
        }
        throw new IllegalStateException("The world hasn't initialize");
    }

    @Override
    protected void constructStage() {
        super.constructStage();
    }

    @Override
    protected void resourceStage() {
        super.resourceStage();
    }

    @Override
    protected void finishStage() {
        logger.info("Finishing Game Initialization!");

        super.finishStage();
        logger.info("Game Ready!");
    }

    public void clientTick() {
        if (isMarkedTermination()) {
            tryTerminate();
        }

        getWorlds().forEach(world -> ((WorldCommon) world).tick());
        // TODO upload particle physics here
    }

    @Override
    protected void tryTerminate() {
        logger.info("Game terminating!");
        engine.getEventBus().post(new GameTerminationEvent.Pre(this));
        super.tryTerminate();
        engine.getEventBus().post(new GameTerminationEvent.Post(this));
        logger.info("Game terminated.");
    }
}