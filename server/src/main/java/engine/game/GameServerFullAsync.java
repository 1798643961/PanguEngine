package engine.game;

import com.google.common.base.Strings;
import engine.Engine;
import engine.entity.Entity;
import engine.event.world.WorldCreateEvent;
import engine.event.world.WorldLoadEvent;
import engine.event.world.WorldUnloadEvent;
import engine.player.Player;
import engine.player.PlayerImpl;
import engine.player.Profile;
import engine.registry.Registries;
import engine.server.network.NetworkServer;
import engine.world.World;
import engine.world.WorldCommon;
import engine.world.WorldCreationSetting;
import engine.world.exception.WorldAlreadyLoadedException;
import engine.world.exception.WorldLoadException;
import engine.world.exception.WorldNotExistsException;
import engine.world.exception.WorldProviderNotFoundException;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.*;

/**
 * Each world host in an independent thread.
 */
public class GameServerFullAsync extends GameBase {

    protected final Set<Player> players = new HashSet<>();
    protected final Map<String, World> worlds = new HashMap<>();
    private final NetworkServer networkServer;
//    protected List<Thread> worldThreads;

    public GameServerFullAsync(Engine engine, Path storagePath, GameData data, NetworkServer networkServer) {
        super(engine, storagePath, data);
        this.networkServer = networkServer;
    }

    @Nonnull
    @Override
    public Player joinPlayer(Profile profile, Entity controlledEntity) {
        Player player = new PlayerImpl(profile, controlledEntity);
        players.add(player);
        return player;
    }

    @Nonnull
    @Override
    public Collection<Player> getPlayers() {
        return List.copyOf(players);
    }

    @Nonnull
    @Override
    public World createWorld(@Nonnull String providerName, @Nonnull String name, @Nonnull WorldCreationSetting creationConfig) {
        Validate.notEmpty(providerName);
        Validate.notEmpty(name);
        Validate.notNull(creationConfig);
        if (worlds.containsKey(name)) {
            throw new WorldAlreadyLoadedException(name);
        }

        var provider = Registries.getWorldProviderRegistry().getValue(providerName);
        if (provider == null) {
            throw new WorldProviderNotFoundException(providerName);
        }

        var world = provider.create(this, storagePath.resolve("world").resolve(name), name, creationConfig);
        getEventBus().post(new WorldCreateEvent(world));

        this.worlds.put(name, world);
        this.data.getWorlds().put(name, providerName);
        this.data.save();
        getEventBus().post(new WorldLoadEvent(world));

//        Thread thread = new Thread((Runnable) world);
//        thread.setName("World Thread - " + name);
//        this.worldThreads.add(thread);

        return world;
    }

    @Nonnull
    @Override
    public World loadWorld(@Nonnull String name) throws WorldLoadException, WorldNotExistsException {
        if (worlds.containsKey(name)) {
            throw new WorldAlreadyLoadedException(name);
        }

        String providerName = data.getWorlds().get(name);
        if (Strings.isNullOrEmpty(providerName)) {
            throw new WorldNotExistsException(name);
        }

        return loadWorld(name, providerName);
    }

    @Override
    public void unloadWorld(@Nonnull String name) {
        getWorld(name).ifPresent(World::unload);
    }

    private World loadWorld(@Nonnull String name, @Nonnull String providerName) {
        Validate.notEmpty(name);
        if (worlds.containsKey(name)) {
            throw new WorldAlreadyLoadedException(name);
        }

        var provider = Registries.getWorldProviderRegistry().getValue(providerName);
        if (provider == null) {
            throw new WorldProviderNotFoundException(providerName);
        }

        World world = provider.load(this, storagePath.resolve("world").resolve(name));
        this.worlds.put(name, world);
        getEventBus().post(new WorldLoadEvent(world));
        return world;
    }

    // @Override
    public Collection<World> getWorlds() {
        return worlds.values();
    }

    @Override
    public Optional<World> getWorld(@Nonnull String name) {
        return Optional.ofNullable(worlds.get(name));
    }

    @Override
    protected void constructStage() {
        super.constructStage();
//        this.worldThreads = Lists.newArrayList();
    }

    @Override
    protected void finishStage() {
        super.finishStage();
        data.getWorlds().forEach(this::loadWorld);
        markReady();
    }

    @Override
    public void init() {
        super.init();
//        for (Thread thread : this.worldThreads) {
//            thread.start();
//        }
    }

    @Override
    public void terminate() {
        super.terminate();
    }

    @Override
    public void doUnloadWorld(World world) {
        if (!world.isUnloaded()) {
            throw new IllegalStateException("World is not unloaded");
        }

        worlds.remove(world.getName());
        getEventBus().post(new WorldUnloadEvent(world));
    }

    @Override
    protected void tryTerminate() {
//        for (World worldCommon : worlds.values()) {
//            ((WorldCommon) worldCommon).stop();
//        }
        List.copyOf(worlds.values()).forEach(World::unload);
        // TODO: unload mod/resource here
        super.tryTerminate();
    }

    //TODO: move to api
    public void tick() {
        if (isMarkedTermination()) {
            tryTerminate();
        }

        networkServer.tick();
        getWorlds().forEach(world -> ((WorldCommon) world).tick());
    }
}