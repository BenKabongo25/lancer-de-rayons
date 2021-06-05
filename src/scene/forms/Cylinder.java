package scene.forms;

import geometry.Coords3D;
import scene.Ray;

public class Cylinder extends Form {

    /**
     * Premier côté
     */
    protected Coords3D firstSide;

    public final Coords3D getFirstSide() {
        return firstSide;
    }

    public final void setFirstSide(Coords3D firstSide) {
        this.firstSide = firstSide;
    }

    /**
     * Second côté
     */
    protected Coords3D secondSide;

    public final Coords3D getSecondSide() {
        return secondSide;
    }

    public final void setSecondSide(Coords3D secondSide) {
        this.secondSide = secondSide;
    }

    /**
     * rayon
     */
    protected double radius;

    public final double getRadius() {
        return radius;
    }

    public final void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Cylindre
     * @param firstSide premier côté
     * @param secondSide second côté
     * @param radius rayon
     * @param texture texture
     */
    public Cylinder(final Coords3D firstSide,
                    final Coords3D secondSide,
                    double radius,
                    final Texture texture)
    {
        super(texture);
        this.firstSide = firstSide;
        this.secondSide = secondSide;
        this.radius = radius;
    }

    public Cylinder(final Coords3D firstSide,
                    final Coords3D secondSide,
                    double radius)
    {
        this(firstSide, secondSide, radius, new Texture());
    }

    public Cylinder() {
        this(new Coords3D(), new Coords3D(), 1);
    }

    @Override
    public String logToString() {
        return "Objet de type plan : Cylinder\n" +
                transformationLogToString() +
                "\t" + firstSide.logToString("Pos centre 1") +
                "\t" + secondSide.logToString("Pos centre 2") +
                "\t" + "De rayon " + radius + "\n";
    }

    @Override
    public Intersection intersect(Ray ray) {
        Coords3D v1 = Coords3D.sub(secondSide, firstSide).normalize();
        Coords3D v2 = Coords3D.cross(ray.getDirection(), v1).normalize();
        Coords3D v3 = Coords3D.cross(v1, v2).normalize();
        Coords3D v4 = Coords3D.sub(ray.getOrigin(), firstSide);

        Coords3D v5  = new Coords3D(Coords3D.dot(v4, v2),
                Coords3D.dot(v4, v3),
                Coords3D.dot(v4, v1));
        Coords3D v6  = new Coords3D(Coords3D.dot(ray.getDirection(), v2),
                Coords3D.dot(ray.getDirection(), v3),
                Coords3D.dot(ray.getDirection(), v1));

        double a = v6.getY() * v6.getY();
        double b = 2 * v6.getY() * v5.getY();
        double c = v5.getY() * v5.getY() + v5.getX() * v5.getX() - radius * radius;

        if (a == 0) return new Intersection();
        double delta = b * b - 4 * a * c;
        if (delta < 0) return new Intersection();

        double t1 = (-b - Math.sqrt(delta)) / (2 * a);
        double t2 = (-b + Math.sqrt(delta)) / (2 * a);
        if (t2 <= 0) return new Intersection();

        double t = t1;
        if (t1 <= 0) t = t2;

        Coords3D position = Coords3D.add(ray.getOrigin(), Coords3D.scale(ray.getDirection(), t));

        double e = Coords3D.dot(Coords3D.sub(position, firstSide), v1);
        double f = Coords3D.sub(secondSide, firstSide).length();
        if (e < 0 || e > f) return new Intersection();

        Coords3D normal = Coords3D.sub(position, Coords3D.add(firstSide, Coords3D.scale(v1, e))).normalize();
        return new Intersection(this, ray, position, normal);
    }

}
