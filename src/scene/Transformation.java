package scene;

import geometry.Coords3D;
import geometry.Matrix4x4;

/**
 * Transformation des formes et des lumières
 */
public abstract class Transformation {

    //protected Matrix4x4 mMatrice;

    protected Coords3D mPosition = new Coords3D();
    protected Coords3D mRotation = new Coords3D();
    protected Coords3D mScale = new Coords3D(1, 1, 1);

    public void setPosition(Coords3D var) {mPosition = var; }
    public void setRotation(Coords3D var)
    {
        mRotation = var;
    }
    public void setScale(Coords3D var)
    {
        mScale = var;
    }

    public Coords3D getPosition(){ return mPosition; }

    public Matrix4x4 getTransformedMatrice()
    {
        Matrix4x4 mMatrice = new Matrix4x4();
        mMatrice.loadIdentity();

        //attention l'ordre des opération à une importance
        mMatrice.scale(mScale.getX(), mScale.getY(), mScale.getZ());
        mMatrice.translate(mPosition.getX(), mPosition.getY(), mPosition.getZ());

        mMatrice.rotate(mRotation.getX(),1, 0, 0); //la rotation sur l'axe X
        mMatrice.rotate(mRotation.getY(),0, 1, 0); //la rotation sur l'axe Y
        mMatrice.rotate(mRotation.getZ(),0, 0, 1); //la rotation sur l'axe Z

        return mMatrice;
    }

    public String transformationLogToString() {
        return "\t" + mPosition.logToString("position") +
        "\t" + mRotation.logToString("rotation") +
        "\t" + mScale.logToString("scale");
    }
}
