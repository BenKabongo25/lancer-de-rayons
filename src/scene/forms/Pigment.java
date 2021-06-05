package scene.forms;

import geometry.ColorRGBA;

/**
 * Renseigne les informations sur les couleurs des
 * formes
 */
public class Pigment {

    // on commence par faire simple en concevant des pigments
    // composés que d'une couleur

    /**
     * Couleur de la forme
     */
    protected ColorRGBA color;

    public final ColorRGBA getColor() {
        return color;
    }

    public final void setColor(ColorRGBA color) {
        this.color = color;
    }

    public Pigment(ColorRGBA color) {
        this.color = color;
    }

    public Pigment() {
        this(new ColorRGBA(1, 1, 1, 0));
    }

}
