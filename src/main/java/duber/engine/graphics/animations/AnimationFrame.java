package duber.engine.graphics.animations;

import java.util.Arrays;

import org.joml.Matrix4f;

public class AnimationFrame {

    private static final Matrix4f IDENTITY_MATRIX = new Matrix4f();

    public static final int MAX_JOINTS = 150;

    private final Matrix4f[] jointMatrices;

    public AnimationFrame() {
        jointMatrices = new Matrix4f[MAX_JOINTS];
        Arrays.fill(jointMatrices, IDENTITY_MATRIX);
    }

    public Matrix4f[] getJointMatrices() {
        return jointMatrices;
    }

    public void setMatrix(int pos, Matrix4f jointMatrix) {
        jointMatrices[pos] = jointMatrix;
    }
}
