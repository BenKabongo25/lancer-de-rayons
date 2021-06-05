package geometry;

import java.util.Objects;

/**
 * Coordonnées 3D
 * Représente aussi bien un vecteur 3D qu'un point 3D
 */
public final class Coords3D implements Cloneable {

    /**
     * x: coordonnée x (abcisse)
     */
    private double x;

    public final double getX() {
        return x;
    }

    public final void setX(double x) {
        this.x = x;
    }

    /**
     * y: coordonnée y (ordonnée)
     */
    private double y;

    public final double getY() {
        return y;
    }

    public final void setY(double y) {
        this.y = y;
    }

    /**
     * z: coordonnée z
     */
    private double z;

    public final double getZ() {
        return z;
    }

    public final void setZ(double z) {
        this.z = z;
    }

    /**
     * Objet de dimension 3
     * @param x coordonnée x
     * @param y coordonnée y
     * @param z coordonnée z
     */
    public Coords3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coords3D(Coords3D other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public void setXYZ(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public void log(String name)
    {
        System.out.print(logToString(name));
    }
    public String logToString(String name)
    {
        return name + " : (" + x + ", " + y + ", " + z + ")\n";
    }

    /**
     * Objet de dimension 3
     * De valeur (0, 0, 0)
     */
    public Coords3D() {
        this(0, 0, 0);
    }

    /**
     * Objet de dimension 3
     * De valeur (v, v, v)
     * @param v valeur des 3 coordonnées x, y, z
     */
    public Coords3D(double v) {
        this(v, v, v);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coords3D coords3D = (Coords3D) o;
        return  Double.compare(coords3D.x, x) == 0 &&
                Double.compare(coords3D.y, y) == 0 &&
                Double.compare(coords3D.z, z) == 0;
    }

    @Override
    public Coords3D clone() {
        return new Coords3D(x, y, z);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }

    /**
     * @return Un tableau {x, y, z} des coordonnées
     */
    public double[] toArray() {
        return new double[] {x, y, z};
    }

    /**
     * @return Carré de la longueur
     */
    public final double length2() {
        return x * x + y * y + z * z;
    }

    /**
     * @return Longueur
     */
    public final double length() {
        return Math.sqrt(length2());
    }

    /**
     * Produit scalaire
     * @param c autres coordoonées
     * @return
     */
    public final double dot(final Coords3D c) {
        return x * c.x + y * c.y + z * c.z;
    }

    /**
     * Produit scalaire
     * @param c1 premières coordonnées
     * @param c2 secondes coordonnées
     * @return
     */
    public static double dot(final Coords3D c1, final Coords3D c2) {
        return c1.dot(c2);
    }

    /**
     * Produit croisé
     * @param c autres coordonnées
     * @return coordonnées du produit
     */
    public final Coords3D cross(final Coords3D c) {
        return new Coords3D(y * c.z - z * c.y,
                            z * c.x - x * c.z,
                            x * c.y - y * c.x);
    }

    /**
     * Produit croisé
     * @param c1 premières coordonnées
     * @param c2 secondes coordonnées
     * @return coordonnées du produit
     */
    public static Coords3D cross(final Coords3D c1, final Coords3D c2) {
        return c1.cross(c2);
    }

    /**
     * Somme
     * @param c autres coordonnées
     * @return coordonnées sommes
     */
    public final Coords3D add(final Coords3D c) {
        return new Coords3D(x + c.x, y + c.y, z + c.z);
    }

    /**
     * Somme
     * @param c1 premières coordonnées
     * @param c2 secondes coordonnées
     * @return coordonnées sommes
     */
    public static Coords3D add(final Coords3D c1, final Coords3D c2) {
        return c1.add(c2);
    }

    /**
     * Différence
     * @param c coordonnées
     * @return coordonnées de différence
     */
    public final Coords3D sub(final Coords3D c) {
        return new Coords3D(x - c.x, y - c.y, z - c.z);
    }

    /**
     * Différence
     * @param c1 premières coordonnées
     * @param c2 secondes coordonnées
     * @return coordonnées de différence
     */
    public static Coords3D sub(final Coords3D c1, final Coords3D c2) {
        return c1.sub(c2);
    }

    /**
     * Produit coordonnée par coordonnée
     * @param c
     * @return
     */
    public final Coords3D mul(final Coords3D c) {
        return new Coords3D(x * c.x, y * c.y, z * c.z);
    }

    /**
     * Produit coordonnée par coordonnée
     * @param c1
     * @param c2
     * @return
     */
    public static Coords3D mul(final Coords3D c1, final Coords3D c2) {
        return c1.mul(c2);
    }

    /**
     * Produit des coordonnées avec un facteur
     * @param v facteur de multiplication
     * @return coordonnées produits
     */
    public final Coords3D scale(double v) {
        return new Coords3D(x * v, y * v, z * v);
    }

    /**
     * Produit des coordonnées avec un facteur
     * @param c coordonnées
     * @param v facteur
     * @return coordonnées produits
     */
    public static Coords3D scale(final Coords3D c, double v) {
        return c.scale(v);
    }

    /**
     * Normalise les coordonnées
     * @return coordonnées normalisées
     */
    public final Coords3D normalize() {
        double l = this.length();
        if (l > 0) {
            return new Coords3D(x / l, y / l, z / l);
        } return new Coords3D(x, y, z);
    }

    /**
     * Normalise les coordonnées
     * @param c coordonnées
     * @return coordonnées normalisées
     */
    public static Coords3D normalize(final Coords3D c) {
        return c.normalize();
    }

    /**
     * Calcule la distance au carré entre les coordonnées
     * @param c autres coordonnées
     * @return distance au carré
     */
    public final double dist2(final Coords3D c) {
        return Math.pow(x - c.x, 2) + Math.pow(y - c.y, 2) + Math.pow(z - c.z, 2);
    }

    /**
     * Distance au carré entre deux coordonnées
     * @param c1 premières coordonnées
     * @param c2 secondes coordonnées
     * @return distance au carré
     */
    public static double dist2(final Coords3D c1, final Coords3D c2) {
        return c1.dist2(c2);
    }

}
