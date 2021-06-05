package scene.forms;

import geometry.Coords3D;
import scene.Ray;

/**
 * Objet de la scène
 * Représente une scène
 */
public class Sphere extends Form {

    /**
     * Rayon de la sphère
     */
    private double radius;

    public final double getRadius() {
        return radius;
    }

    public final void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Sphère
     * @param position position de l'objet
     * @param radius rayon
     * @param texture texture
     */
    public Sphere(final Coords3D position, double radius, final Texture texture) {
        super(texture);

        setPosition(position);
        this.radius = radius;
    }

    public Sphere(final Coords3D position, double radius) {
        this(position, radius, new Texture());
    }

    public Sphere() {
        this(new Coords3D(), 1);
    }

    @Override
    public String logToString() {
        return  "Objet de type sphère :\n" +
                transformationLogToString() +
                getPosition().logToString("\tposition") +
                "\trayon = " + radius + "\n" +
                texture.logToString();
    }

    // Un point P se trouve dans une sphère de centre O et de rayon R ssi
    //        ||P-S.O||^2 = S.R^2
    // Un rayon est en fait une demi-droite, dont on connait l'origine et la direction
    // On peut donc écrire une équation des points de cette demi-droite
    // P sera un point du rayon d'origine O et de direction D ssi
    //        il existe t appartenant à |R tel que
    //        P = R.O + t * R.D
    // Afin donc de trouver l'intersection entre un rayon et une sphère, il s'agira donc
    // de reporter l'équation des points des rayons dans l'équation des points de la shpère
    // Cela reviendra à résoudre une équation du second degré en t, dont le coefficient sont a, b,c
    // tels que
    //        a = 1
    //        b = R.D * (R.O - S.O)
    //        c = ||R.0 - S.0||^2 - S.R^2
    public Intersection intersect(Ray ray) {

        double a = 1;
        double b = 2 * Coords3D.dot(ray.getDirection(), Coords3D.sub(ray.getOrigin(), getPosition()));
        double c = Coords3D.sub(ray.getOrigin(), getPosition()).length2() -
                Math.pow(Sphere.this.getRadius(), 2);

        double delta = b * b - 4 * a * c;
        if (delta < 0) return new Intersection();

        double t, t1, t2;
        t1 = (-b - Math.sqrt(delta)) / (2 * a);
        t2 = (-b + Math.sqrt(delta)) / (2 * a);
        if (t2 <= 0) return new Intersection();

        if (t1 > 0) t = t1;
        else t = t2;

        Coords3D position = Coords3D.add(ray.getOrigin(), Coords3D.scale(ray.getDirection(), t));
        Coords3D normal = Coords3D.sub(position, getPosition()).normalize();

        return new Intersection(this, ray, position, normal);
    }
}
