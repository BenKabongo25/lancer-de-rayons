package scene.lights;

import geometry.ColorRGBA;
import geometry.Coords3D;

// Source de lumière ponctuelle
// Les projecteurs sont exactement ce que le nom indique
// Une lumière ponctuelle n'a pas de taille, est invisible et illumine tout
// dans la scène de la même manière, quelle que soit la distance de la source l
// umineuse (ce comportement peut être modifié)
// Il s'agit de la source lumineuse la plus simple et la plus élémentaire.
// Il n'y a que deux paramètres importants, l'emplacement et la couleur

/**
 * Lumière
 * Source de lumière ponctuelle
 */
public class PointLight extends Light {

    public PointLight(final Coords3D position, final ColorRGBA color, final double fade_distance, final double fade_power) {
        super(color);
        setPosition(position);
        setFade_distance(fade_distance);
        setFade_power(fade_power);
    }

    public PointLight() {
        this(new Coords3D(), new ColorRGBA(1, 1, 1, 0), 1000000, 1);
    }

    public String logToString()
    {
        String log = "Light :\n";
        log += getPosition().logToString("\tposition");
        log += color.logToString("\tcolor");

        String logFadeDist = (fade_distance == -1) ? "infinie" :  ""+fade_distance;
        log += "\tfade_distance = " + logFadeDist + "\n";
        log += "\tfade_power = " + fade_power + "\n";
        return log;
    }


    public double getFade_distance()
    {
        return fade_distance;
    }
    public void setFade_distance(double fade_distance)
    {
        this.fade_distance = fade_distance;
    }

    public double getFade_power()
    {
        return fade_power;
    }
    public void setFade_power(double fade_power)
    {
        this.fade_power = fade_power;
    }

    //le rayon d'influence de la lumière. Si le rayon est égal à -1 on considére que le rayon est infini
    private double fade_distance;

    //le type de dégradé, 1 linéaire, 2 quadratique
    private double fade_power;
    //les attributs de la point source light est défini ici : http://www.f-lohmueller.de/pov_tut/camera_light/light_e2.htm
}
