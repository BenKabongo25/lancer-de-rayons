package scene;

import geometry.Coords3D;

public class Camera {

    /* D'après Wikipedia voici les informations compris dans un objet de type Camera
    [0] https://en.wikipedia.org/wiki/POV-Ray

    // Places a camera
    // direction: Sets, among other things, the field of view of the camera
    // right: Sets the aspect ratio of the image
    // look_at: Tells the camera where to look

    camera       { location  <0.0, 0.5, -4.0>
                direction 1.5*z
                right     x*image_width/image_height
                look_at   <0.0, 0.0, 0.0> }
    */

    /**
     * Position de la caméra
     */
    private Coords3D position;

    public Coords3D getPosition() {
        return position;
    }

    public final void setPosition(Coords3D position) {
        this.position = position;
    }

    /**
     * direction de la caméra
     */
    private Coords3D lookAt;

    public final Coords3D getLookAt() {
        return lookAt;
    }

    public final void setLookAt(Coords3D lookAt) {
        this.lookAt = lookAt;
    }

    private Coords3D up;

    /**
     * Longueur de l'image
     */
    private int imageWidth;

    public final int getImageWidth() {
        return imageWidth;
    }

    public final void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    /**
     * Hauteur de l'image
     */
    private int imageHeight;

    public final int getImageHeight() {
        return imageHeight;
    }

    public final void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    /**
     * Rapport entre la longueur et la hauteur de l'image
     */
    private double aspectratio;

    public final double getAspectratio() {
        return aspectratio;
    }

    public final void setAspectratio(double aspectratio) {
        this.aspectratio = aspectratio;
    }

    /**
     * champ de vision (en degrés)
     */
    private double fov;

    public final double getFov() {
        return fov;
    }

    public final void setFov(double fov) {
        this.fov = fov;
    }

    /**
     * Camera
     * @param position position de la caméra
     * @param lookAt direction de la caméra
     * @param imageWidth longueur de l'image
     * @param imageHeight largeur de l'image
     * @param fov point de vue, champ de vision
     * @param aspectratio rapport entre la longueur de l'image et sa hauteur
     */
    public Camera(final Coords3D position,
                  final Coords3D lookAt,
                  int imageWidth,
                  int imageHeight,
                  double fov,
                  double aspectratio)
    {
        this.position = position;
        this.lookAt = lookAt;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.fov = fov;
        this.aspectratio = aspectratio;

        //TODO : récuperer le vecteur up dans le parser, mais 99% du temps up = (0,1,0)
        up = new Coords3D(0, 1, 0);
    }

    /**
     * Camera
     * @param position position de la caméra
     * @param lookAt direction de la caméra
     */
    public Camera(final Coords3D position, final Coords3D lookAt) {
        this(position, lookAt, 400, 300, 90, 1);
    }

    public Camera() {

        this(new Coords3D(), new Coords3D(0, 0, 0));
    }

    public String logToString()
    {
        String log = "Camera :\n";
        log += position.logToString("\tposition");
        log += lookAt.logToString("\tlookAt");
        log += "\tfov = " + getFov() + "\n";
        return log;
    }

    /**
     * Lancer d'un rayon de la camera sur la scène pour le pixel (x, y)
     * @param x abcisse du pixel
     * @param y ordonnée du pixel
     * @return
     */

    public Ray getRay(int x, int y) {

        Coords3D origin = position;

        double ratioDiagonal = (double)  imageWidth / imageHeight;
        double theta = fov*Math.PI/180;
        double moitieW = Math.tan(theta/2.0);
        double moitieH = moitieW/ratioDiagonal;

        double bias = 0.0001;
        Coords3D mLookAt = lookAt;
        if( lookAt.getX() == position.getX()  && lookAt.getZ() == position.getZ() )
        mLookAt = mLookAt.add(new Coords3D(0, 0, bias));

        double focus_dist = 1;//origin.sub(lookAt).length();
        Coords3D w = Coords3D.normalize(origin.sub(mLookAt)); //vector pointing out of camera
        Coords3D u = Coords3D.normalize(Coords3D.cross(up,w)); //vector pointing out of side of camera, orthogonal to both view and up direction
        Coords3D v = Coords3D.cross(w,u); //vector pointing out top of camera

        Coords3D basGauche = origin.sub(u.scale(moitieW*focus_dist)).sub(v.scale(moitieH*focus_dist)).sub(w.scale(focus_dist));
        Coords3D horizontal = u.scale(2*moitieW*focus_dist);
        Coords3D vertical = v.scale(2*moitieH*focus_dist);

        double s = (double) x / imageWidth;
        double t = (double) y / imageHeight;

        Coords3D direction = basGauche.add(horizontal.scale(s)).add(vertical.scale(t)).sub(origin);

        return new Ray(origin, direction.normalize());
    }
}
