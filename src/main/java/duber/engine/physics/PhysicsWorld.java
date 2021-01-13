package duber.engine.physics;

import org.joml.Vector3f;

import duber.engine.entities.Entity;
import duber.engine.entities.components.Transform;
import duber.engine.entities.components.Collider;
import duber.engine.entities.components.Follow;
import duber.engine.entities.components.RigidBody;
import duber.engine.physics.collisions.ICollisionHandler;

public abstract class PhysicsWorld {
    private static final float GRAVITY = 0.25f;
    private static final float MAX_Y_SPEED = 3.0f;
    private ICollisionHandler collisionHandler;

    public abstract void update();

    public void setCollisionHandler(ICollisionHandler collisionHandler) {
        this.collisionHandler = collisionHandler;
    }
    
    public void updateEntityPhysics(Entity entity) {
        if(entity.hasComponent(RigidBody.class)) {
            updateRigidBody(entity.getComponent(RigidBody.class), entity);
        }
        
        if(entity.hasComponent(Collider.class)) {
            updateCollider(entity.getComponent(Collider.class), entity);            
        }

        if(entity.hasComponent(Follow.class)) {
            updateFollow(entity.getComponent(Follow.class), entity);
        }
    }

    private void updateCollider(Collider collider, Entity entity) {
        collisionHandler.handleCollisions(collider, entity);
    }

    private void updateRigidBody(RigidBody rigidBody, Entity entity) {
        Transform transform = entity.getComponent(Transform.class);
        
        Vector3f velocity = rigidBody.getVelocity();
        Vector3f angularVelocity = rigidBody.getAngularVelocity();
        
        transform.getPosition().add(velocity);

        transform.rotate(angularVelocity.x(), angularVelocity.y(), angularVelocity.z());
        velocity.y -= GRAVITY;
        velocity.set(0, velocity.y(), 0);
        if(velocity.y > MAX_Y_SPEED) {
            velocity.y = MAX_Y_SPEED;
        } else if (velocity.y < -MAX_Y_SPEED) {
            velocity.y = -MAX_Y_SPEED;
        }
        angularVelocity.set(0, 0, 0);
    }

    private void updateFollow(Follow entityFollow, Entity entity) {
        Transform entityTransform = entity.getComponent(Transform.class);
        Transform cameraTransform = entityFollow.getCamera().getComponent(Transform.class);

        cameraTransform.getPosition().set(entityTransform.getPosition());
        cameraTransform.getPosition().add(entityFollow.getCameraOffset());
        cameraTransform.getRotation().set(entityTransform.getRotation());
    }
}