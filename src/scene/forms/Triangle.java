package scene.forms;

import geometry.Coords3D;
import scene.Ray;

public class Triangle extends Form {

    /**
     * Sommets du triangle
     */
    private Coords3D corner1;
    private Coords3D corner2;
    private Coords3D corner3;

    public Coords3D getCorner1() {
        return corner1;
    }

    public Coords3D getCorner2() {
        return corner2;
    }

    public Coords3D getCorner3() {
        return corner3;
    }

    public void setCorner1(Coords3D corner1) {
        this.corner1 = corner1;
    }

    public void setCorner2(Coords3D corner2) {
        this.corner2 = corner2;
    }

    public void setCorner3(Coords3D corner3) {
        this.corner3 = corner3;
    }

    /**
     * Triangle
     * @param corner1 premier sommet
     * @param corner2 second sommet
     * @param corner3 troisième sommet
     */
    public Triangle(final Coords3D corner1, final Coords3D corner2, final Coords3D corner3) {
        super();
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.corner3 = corner3;
    }

    public Triangle() {
        super();
        this.corner1 = new Coords3D(1, 0, 0);
        this.corner2 = new Coords3D(0, 1, 0);
        this.corner3 = new Coords3D(0, 0, 1);
    }

    @Override
    public String logToString() {
        return "Objet de type triangle \n" +
                transformationLogToString() +
                corner1.logToString("\tcorner1") +
                corner2.logToString("\tcorner2") +
                corner3.logToString("\tcorner3");
    }

    // source : Algorithme d'intersection de Möller–Trumbore
    // https://fr.wikipedia.org/wiki/Algorithme_d%27intersection_de_M%C3%B6ller%E2%80%93Trumbore#Impl%C3%A9mentation_Java
    @Override
    public Intersection intersect(Ray ray) {
        double fi = 0.0000001;

        Coords3D edge1 = Coords3D.sub(corner2, corner1);
        Coords3D edge2 = Coords3D.sub(corner3, corner2);

        double a, f, u, v;

        Coords3D h = Coords3D.cross(ray.getDirection(), edge2);
        a = edge1.dot(h);
        if (a > -fi && a < fi)
            return new Intersection();    // Le rayon est parallèle au triangle.

        f = 1.0 / a;
        Coords3D s = Coords3D.sub(ray.getOrigin(), corner1);
        u = f * (s.dot(h));
        if (u < 0.0 || u > 1.0)
            return new Intersection();

        Coords3D q = Coords3D.cross(s, edge1);
        v = f * ray.getDirection().dot(q);
        if (v < 0.0 || u + v > 1.0)
            return new Intersection();

        // On calcule t pour savoir ou le point d'intersection se situe sur la ligne.
        double t = f * edge2.dot(q);
        Coords3D position;
        if (t > fi)
            position = Coords3D.add(ray.getOrigin(), Coords3D.scale(ray.getDirection(), t));
        else // On a bien une intersection de droite, mais pas de rayon.
            return new Intersection();

        // calcul de la normale
        Coords3D normal = Coords3D.cross(Coords3D.sub(corner1, corner2),
                                         Coords3D.sub(corner1, corner3)).normalize();

        return new Intersection(this, ray, position, normal);
    }
}
