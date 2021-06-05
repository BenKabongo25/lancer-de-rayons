package scene.forms;

import geometry.Coords3D;
import scene.Ray;

/**
 * Objet de la scène
 * Représente un cône
 */
public class Cone extends Form {

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
     * Premier rayon
     */
    protected double firstRadius;

    public final double getFirstRadius() {
        return firstRadius;
    }

    public final void setFirstRadius(double firstRadius) {
        this.firstRadius = firstRadius;
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
     * second rayon
     */
    protected double secondRadius;

    public final double getSecondRadius() {
        return secondRadius;
    }

    public final void setSecondRadius(double secondRadius) {
        this.secondRadius = secondRadius;
    }

    /**
     * Cone
     * @param firstSide premier côté
     * @param firstRadius premier rayon
     * @param secondSide second côté
     * @param secondRadius second rayon
     * @param texture texture
     */
    public Cone(final Coords3D firstSide,
                double firstRadius,
                final Coords3D secondSide,
                double secondRadius,
                final Texture texture)
    {
        super(texture);
        this.firstSide = firstSide;
        this.firstRadius = firstRadius;
        this.secondSide = secondSide;
        this.secondRadius = secondRadius;
    }

    public Cone(final Coords3D firstSide,
                double firstRadius,
                final Coords3D secondSide,
                double secondRadius)
    {
        this(firstSide, firstRadius, secondSide, secondRadius, new Texture());
    }

    public Cone() {
        this(new Coords3D(), 1, new Coords3D(), 1);
    }

    @Override
    public String logToString() {

        return "Objet de type plan : Cone\n" +
                transformationLogToString() +
                "\t" + firstSide.logToString("Pos centre 1") +
                "\t" + "De rayon 1 : " + firstRadius + "\n" +
                "\t" + secondSide.logToString("Pos centre 2") +
                "\t" + "De rayon 2 : " + secondRadius + "\n";

    }

    @Override
    public Intersection intersect(Ray ray) {
        return null;
    }

}
