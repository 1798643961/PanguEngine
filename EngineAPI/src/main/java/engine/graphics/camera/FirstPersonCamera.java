package engine.graphics.camera;

import engine.entity.Entity;
import engine.player.Player;
import org.apache.commons.lang3.Validate;
import org.joml.*;

import javax.annotation.Nonnull;
import java.lang.Math;

public class FirstPersonCamera implements OldCamera {

    private final Player player;

    private final Vector3f position = new Vector3f();
    private final Vector3f lookAt = new Vector3f();
    private final Vector3f frontVector = new Vector3f();
    private final Matrix4f viewMatrix = new Matrix4f();

    public FirstPersonCamera(@Nonnull Player player) {
        this.player = Validate.notNull(player);
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void update(float partial) {
        Entity entity = player.getControlledEntity();
        Vector3d position = entity.getPosition();
        Vector3f motion = entity.getMotion();
        Vector3f rotation = entity.getRotation();
        this.position.set((float) position.x + motion.x * partial, (float) position.y + motion.y * partial, (float) position.z + motion.z * partial);
        this.frontVector.set((float) (Math.cos(Math.toRadians(rotation.y)) * Math.cos(Math.toRadians(-rotation.x))),
                (float) Math.sin(Math.toRadians(rotation.y)), (float) (Math.cos(Math.toRadians(rotation.y)) * Math.sin(Math.toRadians(-rotation.x)))).normalize();
        this.lookAt.set(this.position).add(this.frontVector);
        this.viewMatrix.identity().lookAt(this.position, this.lookAt, UP_VECTOR);
    }

    @Override
    public Vector3fc getPosition() {
        return position;
    }

    @Override
    public Vector3fc getLookAt() {
        return lookAt;
    }

    @Override
    public Vector3fc getFront() {
        return frontVector;
    }

    @Override
    public Matrix4fc getViewMatrix() {
        return viewMatrix;
    }

    @Override
    public ChangeListener getChangeListener() {
        return null;
    }

    @Override
    public void setChangeListener(ChangeListener listener) {

    }

}