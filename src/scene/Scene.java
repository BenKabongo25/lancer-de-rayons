package scene;

import geometry.ColorRGBA;
import scene.forms.Form;
import scene.forms.Intersection;
import scene.lights.PointLight;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    /**
     * Formes présentes sur la scène : box, plans, sphères
     */
    private List<Form> forms;

    public final List<Form> getForms() {
        return forms;
    }

    public final void setForms(List<Form> forms) {
        this.forms = forms;
    }

    public final void addForm(Form form) {
        forms.add(form);
    }
    public Form getLastFormAdded() {

        if(forms.size() == 0)
            return null;

        return forms.get(forms.size()-1);
    }


    /**
     * lumières présentes sur la scène
     */
    private List<PointLight> lights;

    public final List<PointLight> getLights() {
        return lights;
    }

    public final void setLights(List<PointLight> lights) {
        this.lights = lights;
    }

    public final void addLight(PointLight light) {
        lights.add(light);
    }

    /**
     * Camera sur la scène
     */
    private Camera camera;

    public final Camera getCamera() {
        return camera;
    }

    public final void setCamera(Camera camera) {
        this.camera = camera;
    }

    private ColorRGBA background;

    public final ColorRGBA getBackground() {
        return background;
    }

    public final void setBackground(ColorRGBA background) {
        this.background = background;
    }

    private String mLogErreur;

    /**
     * Scène
     * @param camera caméra
     * @param forms liste des formes
     * @param lights listes des lumières
     */
    public Scene(final Camera camera,
                 final List<Form> forms,
                 final List<PointLight> lights) {
        this.camera = camera;
        this.forms = forms;
        this.lights = lights;

        background = new ColorRGBA(0, 0, 0, 1);
        mLogErreur = "";
    }

    public Scene() {
        this(new Camera(), new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Trouve l'intersection la plus proche s'il y'en a avec toutes
     * les formes de la scène
     * @param ray rayon
     * @return une intersection s'il y'en a; null sinon
     */
    public Intersection intersect(Ray ray, boolean searchBackFace) {
        Intersection intersection = null;
        double distMin = Double.POSITIVE_INFINITY;

        for (Form form: forms) {
            Intersection intersectionForm = form.intersect(ray);
            if (intersectionForm != null && intersectionForm.isIntersect()) {

                if( searchBackFace == false || (searchBackFace == true && ray.getDirection().dot(intersectionForm.getNormal()) <= 0) )// permet de ne pas afficher les surfaces qui ne font pas faces à la caméra/rayon
                if (intersectionForm.getDistMin() <= distMin) {
                    distMin = intersectionForm.getDistMin();
                    intersection = intersectionForm;
                }
            }
        }

        return intersection;
    }

    public void setLogErreur(String log)
    {
        mLogErreur = log;
    }

    public String getLogErreur()
    {
        return mLogErreur;
    }

    public String logToString()
    {
        String log = "_______________\nScene log :\n";
        log += background.logToString("background");
        log += camera.logToString();
        for (Form form : forms) log += form.logToString();
        for (PointLight light : lights) log += light.logToString();
        return log;
    }
}
