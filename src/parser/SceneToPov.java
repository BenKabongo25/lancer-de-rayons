package parser;

import geometry.ColorRGBA;
import geometry.Coords3D;
import scene.Camera;
import scene.Scene;
import scene.Transformation;
import scene.forms.*;
import scene.lights.*;
import scene.forms.Texture;

/**
 * Générateur de fichier .pov à partir d'une scène
 */
public class SceneToPov {

    public static String write(Camera camera) {
        String pov = "";
        return pov;
    }

    public static String write(Box box) {
        String pov = "";
        return pov;
    }

    public static String write(Cone cone) {
        String pov = "";
        return pov;
    }

    public static String write(Cylinder cylinder) {
        String pov = "";
        return pov;
    }

    public static String write(Plane plane) {
        String pov = "";
        return pov;
    }

    public static String write(Sphere sphere) {
        String pov = "";
        return pov;
    }

    public static String write(Triangle triangle) {
        String pov = "";
        return pov;
    }

    public static String write(PointLight pointLight) {
        String pov = "";
        return pov;
    }

    public static String write(Texture texture) {
        String pov = "";
        return pov;
    }

    public static String write(Transformation transformation) {
        String pov = "";
        return pov;
    }

    public static String write(ColorRGBA colorRGBA) {
        String pov = "";
        return pov;
    }

    public static String write(Coords3D coords3D) {
        String pov = "";
        return pov;
    }

    public static String write(Scene scene) {
        StringBuilder pov = new StringBuilder();

        // transcription du background
        pov.append("background { color rgb " + write(scene.getBackground()) + "}\n");

        // transcription des informations sur la caméra
        pov.append(write(scene.getCamera()));

        // transcription des informations sur les lumières
        for (Light light : scene.getLights()) {
            // l'ordre des 'if' est important
            // car les classes héritent les unes des autres
            String povLight;
            if (light instanceof PointLight)
                povLight = write((PointLight) light);
            else
                povLight = "";
            pov.append(povLight);
        }

        // transcription des informations sur les formes
        for (Form form : scene.getForms()) {
            String povForm;
            if (form instanceof Box)
                povForm = write((Box) form);
            else if (form instanceof Cone)
                povForm = write((Cone) form);
            else if (form instanceof Cylinder)
                povForm = write((Cylinder) form);
            else if (form instanceof Plane)
                povForm = write((Plane) form);
            else if (form instanceof Sphere)
                povForm = write((Sphere) form);
            else if (form instanceof Triangle)
                povForm = write((Triangle) form);
            else
                povForm = "";
            pov.append(povForm);
        }

        return pov.toString();
    }

}
