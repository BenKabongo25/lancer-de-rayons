package scene;

import geometry.Coords3D;

/**
 * Rayon de lumière
 */
public class Ray {

    /**
     * point d'origine du rayon
     */
    private Coords3D origin;

    public Coords3D getOrigin() {
        return origin;
    }

    public void setOrigin(final Coords3D origin) {
        this.origin = origin;
    }

    /**
     * direction du rayon
     */
    private Coords3D direction;

    public Coords3D getDirection() {
        return direction;
    }

    public void setDirection(final Coords3D direction) {
        this.direction = direction;
    }

    /**
     * Rayon
     * @param o origine
     * @param d direction
     */
    public Ray(final Coords3D o, final Coords3D d) {
        origin = o;
        direction = d;
    }

    public Ray() {
        this(new Coords3D(0, 0, 0), new Coords3D(0, 0, 1));
    }
}
