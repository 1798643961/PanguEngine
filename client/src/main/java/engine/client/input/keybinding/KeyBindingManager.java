package engine.client.input.keybinding;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import engine.client.EngineClient;
import engine.event.Listener;
import engine.event.game.GameStartEvent;
import engine.event.game.GameTerminationEvent;
import engine.graphics.display.Window;
import engine.input.Action;
import engine.input.KeyCode;
import engine.input.Modifiers;
import engine.input.MouseButton;
import engine.logic.Tickable;
import engine.registry.Registries;
import engine.registry.Registry;

import java.util.Collection;
import java.util.List;

/**
 * Handles the registration of KeyBinding and also handles key inputs (and mouse
 * inputs)
 */
public class KeyBindingManager implements Tickable, KeyBindingConfig {

    private final EngineClient engine;
    /**
     * Mappes the key binding index to the KeyBinding objects.
     */
    private final Multimap<Integer, KeyBinding> indexToBinding = HashMultimap.create();
    /**
     * KeyBinding Registry
     */
    private Registry<KeyBinding> registry;

    public KeyBindingManager(EngineClient engine) {
        this.engine = engine;
        engine.getEventBus().register(this);
        Window window = engine.getGraphicsManager().getWindow();
        window.addKeyCallback(this::handleKey);
        window.addMouseCallback(this::handleMouse);
    }

    /**
     * Register a KeyBinding
     *
     * @param keybinding key binding to register
     * @Deprecated This should happen when listening to
     * EngineEvent.RegistrationStart
     */
    @Deprecated
    public void register(KeyBinding keybinding) {
        registry.register(keybinding);
    }

    @Listener
    public void onGameReady(GameStartEvent.Post event) {
        Registries.getRegistryManager().getRegistry(KeyBinding.class).ifPresent(registry -> {
            this.registry = registry;
            reload();
        });
    }

    @Listener
    public void onGameMarkedStop(GameTerminationEvent.Marked event) {
        this.registry = null;
        reload();
    }

    /**
     * Reload the bindings. Use this after keybinding settings have changed
     */
    public void reload() {
        indexToBinding.clear();
        if (registry == null) return;
        for (KeyBinding keybinding : registry.getValues()) {
            int code = keybinding.getKey().code;
            int mods = keybinding.getModifier().getInternalCode();
            indexToBinding.put(getIndex(code, mods), keybinding);
        }
    }

    protected void handlePress(int code, int modifiers) {
        Collection<KeyBinding> keyBindings = this.indexToBinding.get(getIndex(code, modifiers));
        for (KeyBinding binding : keyBindings) {
            if (engine.getGraphicsManager().getGUIManager().isShowing() && !binding.isAllowInScreen())
                continue;
            binding.setPressed(true);
            binding.setDirty(true);
        }
        // Trigger single key
        if (modifiers != 0) {
            handlePress(code, 0);
        }
    }

    protected void handleRelease(int code, int modifiers) {
        Collection<KeyBinding> keyBindings = this.indexToBinding.get(getIndex(code, modifiers));
        for (KeyBinding binding : keyBindings) {
            if (engine.getGraphicsManager().getGUIManager().isShowing() && !binding.isAllowInScreen())
                continue;
            binding.setPressed(false);
            if (binding.getActionMode() == ActionMode.PRESS) {
                binding.setDirty(true);
            }
        }
        // Trigger single key
        if (modifiers != 0) {
            handleRelease(code, 0);
        }
    }

    /**
     * Get the index of the modified key
     *
     * @param code      Key code
     * @param modifiers Modifiers
     * @return An index value with last 4-bits presenting the modifiers
     */
    protected int getIndex(int code, int modifiers) {
        return modifiers + (code << 4);
    }

    public void handleMouse(Window window, MouseButton button, Action action, Modifiers modifiers) {
        switch (action) {
            case PRESS:
                handlePress(button.ordinal() + 400, modifiers.getInternalCode());
                break;
            case RELEASE:
                handleRelease(button.ordinal() + 400, modifiers.getInternalCode());
                break;
            default:
                break;
        }
    }

    public void handleKey(Window window, KeyCode key, int scancode, Action action, Modifiers modifiers) {
        switch (action) {
            case PRESS:
                handlePress(key.getCode(), modifiers.getInternalCode());
                break;
            case RELEASE:
                handleRelease(key.getCode(), modifiers.getInternalCode());
                break;
            default:
                break;
        }
    }

    /**
     * Handles Mouse and Key inputs
     */
    @Override
    public void tick() {
        boolean displayingScreen = engine.getGraphicsManager().getGUIManager().isShowing();
        if (displayingScreen) {
            releaseAllPressedKeys(false);
        }

        for (KeyBinding keyBinding : indexToBinding.values()) {
            if (displayingScreen && !keyBinding.isAllowInScreen()) continue;
            if (keyBinding.isDirty()) {
                // state change
                keyBinding.setDirty(false);
                if (keyBinding.getActionMode() == ActionMode.PRESS) {
                    keyBinding.setActive(keyBinding.isPressed());
                } else {
                    if (keyBinding.isPressed()) {
                        keyBinding.setActive(!keyBinding.isActive());
                    }
                }
                if (keyBinding.isActive()) {
                    keyBinding.onKeyStart(engine);
                } else {
                    keyBinding.onKeyEnd(engine);
                }
            } else if (keyBinding.isActive()) {
                // keep key
                keyBinding.onKeyKeep(engine);
            }
        }
    }

    private void releaseAllPressedKeys(boolean releaseAll) {
        for (KeyBinding keyBinding : indexToBinding.values()) {
            if (!releaseAll && keyBinding.isAllowInScreen()) continue;
            if (keyBinding.isDirty()) {
                // state change
                keyBinding.setDirty(false);
                if (keyBinding.getActionMode() == ActionMode.PRESS) {
                    keyBinding.setActive(keyBinding.isPressed());
                } else {
                    if (keyBinding.isPressed()) {
                        keyBinding.setActive(!keyBinding.isActive());
                    }
                }
                if (keyBinding.isActive()) {
                    keyBinding.setPressed(false);
                    keyBinding.setActive(false);
                } else {
                    keyBinding.onKeyEnd(engine);
                }
            } else if (keyBinding.isActive()) {
                keyBinding.setPressed(false);
                keyBinding.setActive(false);
                keyBinding.onKeyEnd(engine);
            }
        }
    }

    @Override
    public List<String> getRegisteredKeyBindings() {
        return ImmutableList.copyOf(registry.getKeys());
    }

    @Override
    public Key getBoundKeyFor(String target) {
        return registry.getValue(target).getKey();
    }

    @Override
    public void setBoundKeyFor(String target, Key key) {
        registry.getValue(target).setKey(key);
    }

    @Override
    public void setBoundKeyToDefault(String target) {
        KeyBinding binding = registry.getValue(target);
        binding.setKey(binding.getDefaultKey());
    }

    @Override
    public void saveConfig() {
        reload();
        // TODO: save config file to disk
    }

    public void loadConfig() {
        // TODO: load config file from disk
    }

}
