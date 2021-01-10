package duber.game.networking;

public class ModelLoadPacket {
    public String modelFile;
    public String textureDirectory;

    public ModelLoadPacket(String modelFile, String textureDirectory) {
        this.modelFile = modelFile;
        this.textureDirectory = textureDirectory;
    }

    @SuppressWarnings("unused")
    private ModelLoadPacket(){}
}