package geometry;

/**
 * Couleur des formes et de la scène
 * Implémente les fonctions de manipulations de couleurs
 */
public final class ColorRGBA implements Cloneable {

    /** chaque composant est compris entre [0: 1]
     * r: rouge
     * g: vert
     * b: bleu
     */
    private double r, g, b, a;

    public final double getA() {
        return a;
    }

    public final void setA(double a) {
        if (a < 0 || a > 1)
            throw new IllegalArgumentException("La composante alpha doit être comprise entre 0 et 1");
        this.a = a;
    }

    public final double getR() {
        return r;
    }

    public final void setR(double r) {
        if (r < 0 || r > 1)
            throw new IllegalArgumentException("La composante rouge doit être comprise entre 0 et 1");
        this.r = r;
    }

    public final double getG() {
        return g;
    }

    public final void setG(double g) {
        if (g < 0 || g > 1)
            throw new IllegalArgumentException("La composante verte doit être comprise entre 0 et 1");
        this.g = g;
    }

    public final double getB() {
        return b;
    }

    public final void setB(double b) {
        if (b < 0 || b > 1)
            throw new IllegalArgumentException("La composante bleue doit être comprise entre 0 et 1");
        this.b = b;
    }

    public final void setRGBA(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void log(String name)
    {
        System.out.print(logToString(name));
    }
    public String logToString(String name) {
        return name + " : ( " + r + ", " + g + ", " + b + ", " + a + ")\n";
    }

    /**
     * Couleur RGB
     * @param r rouge
     * @param g vert
     * @param b bleu
     * @throws IllegalArgumentException levée si une composante a une valeur < 0 ou > 1
     */
    public ColorRGBA(double r, double g, double b, double a) {
        if (r < 0 || r > 1)
            throw new IllegalArgumentException("La composante rouge doit être comprise entre 0 et 1");
        if (g < 0 || g > 1)
            throw new IllegalArgumentException("La composante verte doit être comprise entre 0 et 1");
        if (b < 0 || b > 1)
            throw new IllegalArgumentException("La composante bleue doit être comprise entre 0 et 1");
        if (a < 0 || a > 1)
            throw new IllegalArgumentException("La composante alpha doit être comprise entre 0 et 1");
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Couleur RGB
     * @param v valeur de r, g et de b
     */
    public ColorRGBA(double v) {
        this(v, v, v, v);
    }

    /**
     * Couleur RGB
     */
    public ColorRGBA() {
        this(0,0,0, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColorRGBA c = (ColorRGBA) o;
        return  Double.compare(c.r, r) == 0 &&
                Double.compare(c.g, g) == 0 &&
                Double.compare(c.b, b) == 0 &&
                Double.compare(c.a, a) == 0;
    }

    @Override
    public ColorRGBA clone() {
        return new ColorRGBA(r, g, b, a);
    }

    @Override
    public String toString() {
        return "(" + r + "," + g + "," + b + ", " + a + ")";
    }

    /**
     * Normalise la valeur d'une composante
     * La rend nulle si elle inférieure à 0
     * L'égale à 1, si elle est supérieure à 1
     * Ne fait rien si elle est comprise en 0 et 1 inclus
     * @param v valeur de la composante
     * @return
     */
    public static double valueOf(double v) {
        if (v <= 0) return 0;
        if (v >= 1) return 1;
        return v;
    }

    /**
     * Somme
     * @param c autres couleurs
     * @return couleurs sommes
     */
    public final ColorRGBA add(final ColorRGBA c) {
        return new ColorRGBA(valueOf(r + c.r), valueOf(g + c.g), valueOf(b + c.b), valueOf(a + c.a));
    }

    /**
     * Somme
     * @param c1 premières couleurs
     * @param c2 secondes couleurs
     * @return couleurs sommes
     */
    public static ColorRGBA add(final ColorRGBA c1, final ColorRGBA c2) {
        return c1.add(c2);
    }

    /**
     * Différence
     * @param c couleurs
     * @return couleurs de différence
     */
    public final ColorRGBA sub(final ColorRGBA c) {
        return new ColorRGBA(valueOf(r - c.r), valueOf(g - c.g), valueOf(b - c.b), valueOf(a - c.a));
    }

    /**
     * Différence
     * @param c1 premières couleurs
     * @param c2 secondes couleurs
     * @return couleurs de différence
     */
    public static ColorRGBA sub(final ColorRGBA c1, final ColorRGBA c2) {
        return c1.sub(c2);
    }

    /**
     * Produit des couleurs
     * @param c couleurs
     * @return couleurs produit
     */
    public final ColorRGBA mul(final ColorRGBA c) {
        return new ColorRGBA(valueOf(r * c.r), valueOf(g * c.g), valueOf(b * c.b), valueOf(a * c.a));
    }

    /**
     * Produit de couleurs
     * @param c1 premières couleurs
     * @param c2 secondes couleurs
     * @return couleurs produit
     */
    public static ColorRGBA mul(final ColorRGBA c1, final ColorRGBA c2) {
        return c1.mul(c2);
    }

    /**
     * Produit des couleurs avec un facteur
     * @param v facteur de multiplication
     * @return couleurs produits
     */
    public final ColorRGBA scale(double v) {
        return new ColorRGBA(valueOf(r * v), valueOf(g * v), valueOf(b * v), valueOf(a * v));
    }

    /**
     * Produit des couleurs avec un facteur
     * @param c couleurs
     * @param v facteur
     * @return couleurs produits
     */
    public static ColorRGBA scale(final ColorRGBA c, double v) {
        return c.scale(v);
    }

}
