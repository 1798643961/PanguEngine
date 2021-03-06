package engine.event.player;

import engine.event.Event;
import engine.player.Player;

public class PlayerEvent implements Event {

    private final Player player;

    public PlayerEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
