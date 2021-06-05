package scene.lights;


import geometry.ColorRGBA;
import scene.Identifiable;
import scene.Transformation;

/**
 * classe de base des objets lumières
 */

public abstract class Light extends Transformation implements Identifiable {


    // L'une des principales caractéristiques communes de toutes les formes de sources
    // lumineuses, c'est la couleur de la lumière

    /**
     * couleur de la lumière
     */
    protected ColorRGBA color;

    public final ColorRGBA getColor() {
        return color;
    }

    public final void setColor(ColorRGBA color) {
        this.color = color;
    }

    public Light(final ColorRGBA color) {
        this.color = color;
        this.id = "";
    }

    // Identification de l'objet

    /**
     * identifiant de l'objet
     */
    protected String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}
