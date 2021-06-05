package scene.forms;

import geometry.Coords3D;
import scene.Ray;

// Une boîte est définie en spécifiant les coordonnées 3D de ses coins opposés.
// Le premier vecteur est généralement les coordonnées minimales x, y et z
// Le deuxième vecteur doit être les valeurs maximales x, y et z
// Cependant deux coins opposés peuvent être utilisés
// Les objets Boîte ne peuvent être définis que parallèlement aux axes du système de coordonnées universel
// Nous pouvons plus tard les faire pivoter à n'importe quel angle.

/**
 * Objet de la scène
 * Représente une box
 */
public class Box extends Form {

    /**
     * Coordonnées minimales de la box
     */
    protected Coords3D boundingBoxMin;

    public final Coords3D getBoundingBoxMin() {
        return boundingBoxMin;
    }

    public final void setBoundingBoxMin(Coords3D boundingBoxMin) {
        this.boundingBoxMin = boundingBoxMin;
    }

    /**
     * Coordonnées maximales de la box
     */
    protected Coords3D boundingBoxMax;

    public final Coords3D getBoundingBoxMax() {
        return boundingBoxMax;
    }

    public final void setBoundingBoxMax(Coords3D boundingBoxMax) {
        this.boundingBoxMax = boundingBoxMax;
    }

    /**
     * Box
     * @param boundingBoxMin coordonnées minimales
     * @param boundingBoxMax coordonnées maximales
     * @param texture texture
     */
    public Box(final Coords3D boundingBoxMin, final Coords3D boundingBoxMax, final Texture texture) {
        super(texture);
        this.boundingBoxMin = boundingBoxMin;
        this.boundingBoxMax = boundingBoxMax;
    }

    public Box(final Coords3D boundingBoxMin, final Coords3D boundingBoxMax) {
        this(boundingBoxMin, boundingBoxMax, new Texture());
    }

    public Box() {
        this(new Coords3D(), new Coords3D());
    }

    @Override
    public String logToString() {
        return  "Objet de type box \n" +
                transformationLogToString() +
                boundingBoxMin.logToString("\tboundingBoxMin") +
                boundingBoxMin.logToString("\tboundingBoxMax");
    }

    // sources
    // http://ray-tracing-conept.blogspot.com/2015/01/ray-box-intersection-and-normal.html
    //
    @Override
    public Intersection intersect(final Ray ray) {
        double  minx = Math.min(boundingBoxMin.getX(), boundingBoxMax.getX()),
                maxx = Math.max(boundingBoxMin.getX(), boundingBoxMax.getX()),
                miny = Math.min(boundingBoxMin.getY(), boundingBoxMax.getY()),
                maxy = Math.max(boundingBoxMin.getY(), boundingBoxMax.getY()),
                minz = Math.min(boundingBoxMin.getZ(), boundingBoxMax.getY()),
                maxz = Math.max(boundingBoxMin.getZ(), boundingBoxMax.getZ());

        double  tNear = Double.NEGATIVE_INFINITY, // le plus proche
                tFar  = Double.POSITIVE_INFINITY, // le plus loin
                tmin, tmax, t;

        if ((ray.getDirection().getX() == 0) && (ray.getOrigin().getX() < minx) && (ray.getOrigin().getX() > maxx))
            return new Intersection();
        if (ray.getDirection().getX() != 0) {
            tmin = (minx - ray.getOrigin().getX()) / ray.getDirection().getX();
            tmax = (maxx - ray.getOrigin().getX()) / ray.getDirection().getX();
            if (tmin > tmax) {
                double tmp = tmin;
                tmin = tmax;
                tmax = tmp;
            }
            tNear = Math.max(tNear, tmin);
            tFar = Math.min(tFar, tmax);
            if (tNear > tFar || tFar <= 0)
                return new Intersection();
        }

        if ((ray.getDirection().getY() == 0) && (ray.getOrigin().getY() < miny) && (ray.getOrigin().getY() > maxy))
            return new Intersection();
        if (ray.getDirection().getY() != 0) {
            tmin = (miny - ray.getOrigin().getY()) / ray.getDirection().getY();
            tmax = (maxy - ray.getOrigin().getY()) / ray.getDirection().getY();
            if (tmin > tmax) {
                double tmp = tmin;
                tmin = tmax;
                tmax = tmp;
            }
            tNear = Math.max(tNear, tmin);
            tFar = Math.min(tFar, tmax);
            if (tNear > tFar || tFar <= 0)
                return new Intersection();
        }

        if ((ray.getDirection().getZ() == 0) && (ray.getOrigin().getZ() < minz) && (ray.getOrigin().getZ() > maxz))
            return new Intersection();
        if (ray.getDirection().getZ() != 0) {
            tmin = (minz - ray.getOrigin().getZ()) / ray.getDirection().getZ();
            tmax = (maxz - ray.getOrigin().getZ()) / ray.getDirection().getZ();
            if (tmin > tmax) {
                double tmp = tmin;
                tmin = tmax;
                tmax = tmp;
            }
            tNear = Math.max(tNear, tmin);
            tFar = Math.min(tFar, tmax);
            if (tNear > tFar || tFar <= 0)
                return new Intersection();
        }

        t = tNear;
        if (t <= 0) return new Intersection();

        Coords3D position = Coords3D.add(ray.getOrigin(), Coords3D.scale(ray.getDirection(), t));

        Coords3D normal;
        double fi = 0.01;
        if (Math.abs(position.getX() - minx) < fi)
            normal = new Coords3D(-1, 0, 0);
        else if (Math.abs(position.getX() - maxx) < fi)
            normal = new Coords3D(1, 0, 0);
        else if (Math.abs(position.getY() - miny) < fi)
            normal = new Coords3D(0, -1, 0);
        else if (Math.abs(position.getY() - maxy) < fi)
            normal = new Coords3D(0, 1, 0);
        else if (Math.abs(position.getZ() - minz) < fi)
            normal = new Coords3D(0, 0, -1);
        else // if (Math.abs(position.getZ() - maxz) < fi)
            normal = new Coords3D(0, 0, 1);

        return new Intersection(this, ray, position, normal);
    }

}
