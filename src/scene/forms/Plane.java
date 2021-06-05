package scene.forms;

import geometry.Coords3D;
import scene.Ray;

/**
 * Objet de la scène
 * Représente un plan 2D
 */
public class Plane extends Form {

    /**
     * La normale du plan
     */
    protected Coords3D normal;

    public final Coords3D getNormal() {
        return normal;
    }

    public final void setNormal(Coords3D normal) {
        this.normal = normal;
    }

    /**
     * distance à laquelle le plan est déplacé
     * le long de la normale à partir de l'origine
     */
    protected double offset;

    public final double getOffset() {
        return offset;
    }

    public final void setOffset(double offset) {
        this.offset = offset;
    }

    /**
     * Plan
     * @param normal normale du plan
     * @param offset distance
     * @param texture texture
     */
    public Plane(final Coords3D normal, double offset, final Texture texture) {
        super(texture);
        this.normal = normal;
        this.offset = offset;
    }

    public Plane(final Coords3D normal, double offset) {
        this(normal, offset, new Texture());
    }

    public Plane() {
        this(new Coords3D(), 0);
    }

    @Override
    public String logToString() {
        return "Objet de type plan :\n" +
                transformationLogToString() +
                normal.logToString("\tequationPlan") +
                "\toffset = " + offset + "\n";
    }

    @Override
    public Intersection intersect(final Ray ray) {
        double scalarProduct = Coords3D.dot(ray.getDirection(), normal);
        if (scalarProduct == 0) return new Intersection();

        double t = (offset - Coords3D.dot(ray.getOrigin(), normal)) / scalarProduct;
        if (t <= 0) return new Intersection();

        Coords3D position = Coords3D.add(ray.getOrigin(), Coords3D.scale(ray.getDirection(), t));

        // le produit scalaire des normales est 0
        Coords3D normalIntersection;
        if (normal.getX() != 0)
            normalIntersection = new Coords3D((-normal.getY() - normal.getZ()) / normal.getX(), 1, 1).normalize();
        else if (normal.getY() != 0)
            normalIntersection = new Coords3D(1, -normal.getZ()/normal.getY(), 1).normalize();
        else
            normalIntersection = new Coords3D(1, 1, 0).normalize();

        return new Intersection(this, ray, position, normalIntersection);
    }
}
