package duber.game.client;

import duber.engine.GameEngine;
import duber.engine.GameLogic;
import duber.engine.Window;
import duber.engine.audio.SoundListener;
import duber.engine.audio.SoundManager;
import duber.engine.exceptions.LWJGLException;
import duber.game.SoundData;
import duber.game.User;
import duber.game.client.GameStateManager.GameStateOption;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import java.io.IOException;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWKeyCallbackI;


public class Duberant extends GameLogic {
    private Window window;
    private User user;
    private ClientNetwork clientNetwork;
    private GameStateManager gameStateManager;
    private SoundManager soundManager;
    
    public Duberant() {
        clientNetwork = new ClientNetwork();
    }
    
    @Override
    public void init(Window window) throws LWJGLException {
        this.window = window;
        window.applyOptions();

        soundManager = new SoundManager();
        soundManager.setListener(new SoundListener(new Vector3f()));
        
        try {
            SoundData.loadSounds(soundManager);
        } catch (IOException ioe) {
            throw new LWJGLException("Could not load sound files");
        }

        gameStateManager = new GameStateManager(this);
        gameStateManager.pushState(GameStateOption.MAIN_MENU);
        
        configureOptionsMenuCallback();
    }

    public Window getWindow() {
        return window;
    }
    
    public User getUser() {
        return user;
    }

    public ClientNetwork getClientNetwork() {
        return clientNetwork;
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isConnected() {
        return clientNetwork.isConnected();
    }

    public boolean isLoggedIn() {
        return user != null && user.isLoggedIn();
    }

    @Override
    public void update() {
        gameStateManager.update();
    }

    @Override
    public void render() {
        gameStateManager.render();
    }

    @Override
    public void cleanup() {
        clientNetwork.close();
        gameStateManager.cleanup();
        soundManager.cleanup();
    }

    private void configureOptionsMenuCallback() {
        GLFWKeyCallbackI optionsCallback = (windowHandle, keyCode, scanCode, action, mods) -> {
            GameState optionMenu = GameStateOption.OPTIONS_MENU.getGameState();
            if (keyCode == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                if (optionMenu.isOpened()) {
                    optionMenu.setShouldClose(true);
                } else {
                    optionMenu.pushSelf();
                }
            }
        };

        window.addCallback(optionsCallback);
    }

    public static void main(String[] args) {
        try {
            GameLogic gameLogic = new Duberant();
            GameEngine gameEngine = new GameEngine("Duberant", 1920, 1080, gameLogic);
            gameEngine.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}

