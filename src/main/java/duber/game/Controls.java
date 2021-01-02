package duber.game;

import org.joml.Vector2f;
import org.joml.Vector3f;

import duber.engine.MouseInput;
import duber.engine.Window;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;


public class Controls {
    private float mouseSensitivity;
    
    private final Window window;
    private Player player;

    public Controls(Window window, Player player) {
        this.window = window;
        this.player = player;

        mouseSensitivity = 0.02f;
    }

    private void addControlVelocity(Vector3f playerVelocity, Vector3f playerRotation, Vector3f controlVelocity) {
        if(controlVelocity.z() != 0) {
            playerVelocity.x += (float)Math.sin(playerRotation.y()) * -1.0f * controlVelocity.z();
            playerVelocity.z += (float)Math.cos(playerRotation.y()) * controlVelocity.z();
        }
        
        if(controlVelocity.x() != 0) {
            playerVelocity.x += (float)Math.sin(playerRotation.y() - Math.toRadians(90)) * -1.0f * controlVelocity.x();
            playerVelocity.z += (float)Math.cos(playerRotation.y() - Math.toRadians(90)) * controlVelocity.x();
        }
        playerVelocity.y += controlVelocity.y();
    }

    public void input(MouseInput mouseInput) {
        Vector3f playerAngularVelocity = player.getPlayerBody().getAngularVelocity();

        Vector2f playerRotation = mouseInput.getDisplacementVec();
        playerAngularVelocity.add(playerRotation.y * mouseSensitivity, playerRotation.x * mouseSensitivity, 0.0f);
    }

    public void update() {
        Vector3f playerVelocity = player.getPlayerBody().getVelocity();

        Vector3f controlVelocity = new Vector3f();      
        
        if(window.isKeyPressed(GLFW_KEY_W)) {
            controlVelocity.add(0, 0, -player.getSpeed());
        } else if(window.isKeyPressed(GLFW_KEY_S)) {
            controlVelocity.add(0, 0, player.getSpeed());
        }

        if(window.isKeyPressed(GLFW_KEY_A)) {
            controlVelocity.add(-player.getSpeed(), 0, 0);
        } else if(window.isKeyPressed(GLFW_KEY_D)) {
            controlVelocity.add(player.getSpeed(), 0, 0);
        }

        if(window.isKeyPressed(GLFW_KEY_Z)) {
            controlVelocity.add(0, -player.getSpeed(), 0);
        } else if(window.isKeyPressed(GLFW_KEY_X)) {
            controlVelocity.add(0, player.getSpeed(), 0);
        }

        addControlVelocity(playerVelocity, player.getModel().getTransform().getRotation(), controlVelocity);
    }
}