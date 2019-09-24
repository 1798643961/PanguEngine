package nullengine.world;

import nullengine.entity.Entity;
import nullengine.world.raytrace.RayTraceEntityHit;
import org.joml.AABBd;
import org.joml.Vector3dc;
import org.joml.Vector3fc;

import java.util.List;
import java.util.function.Predicate;

public interface EntityManager {

    World getWorld();

    <T extends Entity> T spawnEntity(Class<T> entityType, double x, double y, double z);

    <T extends Entity> T spawnEntity(Class<T> entityType, Vector3dc position);

    Entity spawnEntity(String providerName, double x, double y, double z);

    Entity spawnEntity(String providerName, Vector3dc position);

    void destroyEntity(Entity entity);

    List<Entity> getEntities();

    List<Entity> getEntities(Predicate<Entity> predicate);

    <T extends Entity> List<T> getEntitiesWithType(Class<T> entityType);

    List<Entity> getEntitiesWithBoundingBox(AABBd boundingBox);

    List<Entity> getEntitiesWithBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ);

    List<Entity> getEntitiesWithSphere(double centerX, double centerY, double centerZ, double radius);

    RayTraceEntityHit raycastEntity(Vector3fc from, Vector3fc dir, float distance);
}