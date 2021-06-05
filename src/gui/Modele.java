package gui;

import javafx.scene.image.Image;
import parser.ParsePOV;
import raytracer.Raytracer;
import scene.Scene;

import java.util.Observable;

public class Modele extends Observable {

    private Raytracer mRayTracer = null;

    private String filePathSave;
    private int imageW = 600;
    private int imageH = 600;
    public String defaultPOV;

    public String getDefaultPOV() {
        return defaultPOV;
    }

    public void setDefaultPOV(String defaultPOV) {
        this.defaultPOV = defaultPOV;
    }

    public Scene getScene()
    {
        if(mRayTracer != null)
            return mRayTracer.getScene();
        return null;
    }

    public String getFilePathSave() {
        return filePathSave;
    }

    public void setFilePathSave(String filePathSave) {
        this.filePathSave = filePathSave;
    }

    public int getImageW() {
        return imageW;
    }

    public int getImageH() {
        return imageH;
    }

    public Modele()
    {
        filePathSave = null;
        defaultPOV = ModeleDefault.examplePOV;

        setChanged();
        notifyObservers();
    }

    public Image renderScene()
    {
        if(mRayTracer != null)
            return mRayTracer.render(getImageW(), getImageH());
        return null;
    }

    public void updateScene(String input)
    {
        mRayTracer = new Raytracer(new ParsePOV().importPov(input));

        mRayTracer.getScene().getCamera().setImageWidth(getImageW());
        mRayTracer.getScene().getCamera().setImageHeight(getImageH());

        setChanged();
        notifyObservers();
    }
}
