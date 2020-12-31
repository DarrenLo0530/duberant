package duber.engine;

import duber.engine.entities.Entity;
import duber.engine.graphics.OrthoCoord;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;


public class Transformation {
    //Maps an object to its coordinates in the world
    private final Matrix4f modelMatrix;

    //Maps an object relative to the camera and the world
    private final Matrix4f modelViewMatrix;

    //Maps an object relative to a light
    private final Matrix4f lightViewMatrix;

    //Maps an object relative to a light in the world
    private final Matrix4f modelLightViewMatrix;

    private final Matrix4f orthoProjectionMatrix;
    private final Matrix4f orthoProjectionModelMatrix;
    
    public Transformation() {
        modelMatrix = new Matrix4f();
        
        lightViewMatrix = new Matrix4f();
        
        modelViewMatrix = new Matrix4f();

        modelLightViewMatrix = new Matrix4f();
        
        
        orthoProjectionMatrix = new Matrix4f();
        orthoProjectionModelMatrix = new Matrix4f();
    }
    
    public final Matrix4f getOrthoProjectionMatrix() {
        return orthoProjectionMatrix;
    }

    public final Matrix4f updateOrthoProjectionMatrix(float left, float right, float bottom, float top, float zNear, float zFar) {
        return orthoProjectionMatrix.identity().setOrtho(left, right, bottom, top, zNear, zFar);
    }

    public final Matrix4f updateOrthoProjectionMatrix(OrthoCoord orthoCoord) {
        return updateOrthoProjectionMatrix(orthoCoord.getLeft(), orthoCoord.getRight(), orthoCoord.getBottom(),
            orthoCoord.getTop(), orthoCoord.getNear(), orthoCoord.getFar());
    }
    
    public static final Matrix4f updateGeneralViewMatrix(Vector3f position, Vector3f rotation, Matrix4f viewMatrix) {
        //Rotate and translate the object relative to the camera
        return viewMatrix.rotationX((float)Math.toRadians(rotation.x))
                .rotateY((float)Math.toRadians(rotation.y))
                .translate(-position.x, -position.y, -position.z);
    }

    public final Matrix4f getLightViewMatrix() {
        return lightViewMatrix;
    }

    public final Matrix4f updateLightViewMatrix(Vector3f lightPosition, Vector3f lightRotation) {
        return updateGeneralViewMatrix(lightPosition, lightRotation, lightViewMatrix);
    }

    public final Matrix4f buildModelMatrix(Entity entity) {
        Transform entityTransform = entity.getTransform();
        Vector3f position = entityTransform.getPosition();
        Quaternionf rotation = entityTransform.getRotationQuat();
        return modelMatrix.translationRotateScale(
            position.x, position.y, position.z,
            rotation.x, rotation.y, rotation.z, rotation.w,
            entityTransform.getScale(), entityTransform.getScale(), entityTransform.getScale()
        );
    }

    public final Matrix4f buildModelViewMatrix(Matrix4f modelMatrix, Matrix4f viewMatrix) {        
        return viewMatrix.mulAffine(modelMatrix, modelViewMatrix);
    }

    public final Matrix4f buildModelViewMatrix(Entity entity, Matrix4f viewMatrix) {
        return buildModelViewMatrix(buildModelMatrix(entity), viewMatrix);
    }

    public final Matrix4f buildModelLightViewMatrix(Matrix4f modelMatrix, Matrix4f lightViewMatrix) {
        return lightViewMatrix.mulAffine(modelMatrix, modelLightViewMatrix);
    }

    public final Matrix4f buildModelLightViewMatrix(Entity entity, Matrix4f lightViewMatrix) {
        return buildModelLightViewMatrix(buildModelMatrix(entity), lightViewMatrix);
    }

    public Matrix4f buildOrthoProjectionModelMatrix(Entity entity, Matrix4f orthoMatrix) {
        return orthoMatrix.mulOrthoAffine(buildModelMatrix(entity), orthoProjectionModelMatrix);
    }
}
