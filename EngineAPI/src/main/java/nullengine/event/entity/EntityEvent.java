package nullengine.event.entity;

import nullengine.entity.Entity;
import nullengine.event.Event;

public abstract class EntityEvent implements Event {
    private Entity entity;

    public EntityEvent(Entity entity) {
        this.entity = entity;
    }
}
