package scene.forms;

import geometry.Coords3D;
import scene.Ray;
import scene.forms.Form;

/**
 * Classe de base de gestion des intersections
 */
public class Intersection {

    /**
     * Résultat de l'intersection
     * true s'il y a intersection; false, sinon
     */
    private final boolean intersect;

    /**
     * Détermine s'il y a interesection ou pas
     * @return true s'il y a intersection; false, sinon
     */
    public boolean isIntersect() {
        return intersect;
    }

    /**
     * Coordonnées d'intersection
     */
    private final Coords3D position;

    public final Coords3D getPosition() {
        return position;
    }

    /**
     * Normale du point d'intersection
     */
    private final Coords3D normal;

    public final Coords3D getNormal() {
        return normal;
    }

    /**
     * Forme
     */
    private final Form form;

    public final Form getForm() {
        return form;
    }

    /**
     * Rayon
     */
    private final Ray ray;

    public final Ray getRay() {
        return ray;
    }

    /**
     * Distance au carré entre le rayon lancé et
     */
    private final double distMin;

    public final double getDistMin() {
        return distMin;
    }

    /**
     * Intersection entre une forme et un rayon
     * @param form forme
     * @param ray rayon lancé
     * @param position position de l'intersection ; vaut null s'il n'y a pas d'intersection
     * @param normal normale de l'intersection ; vaut null s'il n'y a pas d'intersection
     */
    public Intersection(final Form form,
                        final Ray ray,
                        final Coords3D position,
                        final Coords3D normal)
    {
        this.form = form;
        this.ray = ray;
        this.position = position;
        this.normal = normal;
        this.intersect = true;

        if (position != null)
            this.distMin = Coords3D.dist2(ray.getOrigin(), position);
        else
            this.distMin = Double.POSITIVE_INFINITY;
    }

    /**
     * Intersection entre une forme et un rayon
     * (Dans le cas où il n'y pas d'intersection)
     */
    public Intersection() {
        form = null;
        ray = null;
        position = null;
        normal = null;
        intersect = false;
        distMin = Double.POSITIVE_INFINITY;
    }

}
