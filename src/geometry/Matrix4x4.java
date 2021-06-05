package geometry;

public class Matrix4x4 {

    double[] matrice = new double[16];


    public Matrix4x4()
    {
        loadIdentity();
    }

    //Créer une matrice 4X4 avec une matrice d'identité
    //1 0 0 0
    //0 1 0 0
    //0 0 1 0
    //0 0 0 1
    public void loadIdentity()
    {
        for(int i = 0; i < 16; i++)
            matrice[i] = 0.0;

        // Puis nous mettons à 1 les valeurs de la diagonale
        matrice[0] = 1.0;
        matrice[5] = 1.0;
        matrice[10] = 1.0;
        matrice[15] = 1.0;
    }

    public Matrix4x4 copy()
    {
        Matrix4x4 copie = new Matrix4x4();

        for(int i = 0; i < 16; i++)
            copie.matrice[i] = matrice[i];

        return copie;
    }

    public void multiplierMatrice(Matrix4x4 autreMat)
    {
        Matrix4x4 copieMat = copy();

        for(int i = 0; i < 16; i++)
        matrice[i] = 0;

        float e;
        for(int k = 0; k < 4; k++)
        {
            for(int j = 0; j < 4; j++)
            {
                for(int i = 0; i < 4; i++)
                    matrice[4 * j + k] = matrice[4 * j + k] + copieMat.matrice[4 * j + i] *  autreMat.matrice[4 * i + k];
            }
        }
    }

    public void translate(double x, double y, double z)
    {
        Matrix4x4 translationMat = new Matrix4x4();// on initialise une matrice d'identité

        translationMat.matrice[12] = x;
        translationMat.matrice[13] = y;
        translationMat.matrice[14] = z;

        //et on l'a multiplie à la matrice actuelle
        multiplierMatrice(translationMat);
    }

    public void scale(double x, double y, double z)
    {
        Matrix4x4 scaleMat = new Matrix4x4();

        scaleMat.matrice[0] = x;
        scaleMat.matrice[5] = y;
        scaleMat.matrice[10] = z;
        scaleMat.matrice[15] = 1.0;

        multiplierMatrice(scaleMat);
    }

    public void rotate(double angle, double x, double y, double z)
    {
        // Création de la matrice de rotation
        Matrix4x4 rotationMat = new Matrix4x4();

        // Conversion de l'angle de degrées en radians
        angle = (angle * Math.PI  / 180.0);  /* M_PI est la constante pour PI (environ 3.14) */

        // Normalisation du vecteur axe
        double axe[] = {x, y, z};
        //normaliserVecteur(axe);

        // Ajout des paramètres
        rotationMat.matrice[0] = axe[0]*axe[0] * (1 - Math.cos(angle)) + Math.cos(angle) ;
        rotationMat.matrice[1] = axe[0]*axe[1] * (1 - Math.cos(angle)) - axe[2]*Math.sin(angle) ;
        rotationMat.matrice[2] = axe[0]*axe[2] * (1 - Math.cos(angle)) + axe[1]*Math.sin(angle) ;

        rotationMat.matrice[4] = axe[0]*axe[1] * (1 - Math.cos(angle)) + axe[2]*Math.sin(angle) ;
        rotationMat.matrice[5] = axe[1]*axe[1] * (1 - Math.cos(angle)) + Math.cos(angle) ;
        rotationMat.matrice[6] = axe[1]*axe[2] * (1 - Math.cos(angle)) - axe[0]*Math.sin(angle) ;

        rotationMat.matrice[8] = axe[0]*axe[2] * (1 - Math.cos(angle)) - axe[1]*Math.sin(angle) ;
        rotationMat.matrice[9] = axe[1]*axe[2] * (1 - Math.cos(angle)) + axe[0]*Math.sin(angle) ;
        rotationMat.matrice[10] = axe[2]*axe[2] * (1 - Math.cos(angle)) + Math.cos(angle) ;

        rotationMat.matrice[15] = 1.0;

        // Multiplication de la matrice par la matrice de rotation
        multiplierMatrice(rotationMat);
    }

    Matrix4x4 getCopyInverse()
    {
        double[] inv = new double[16];
        double det; //le determinant de la matrice
        int i;

        //implémenté à partir de :
        //https://fr.wikipedia.org/wiki/Calcul_du_d%C3%A9terminant_d%27une_matrice
        //https://fr.wikipedia.org/wiki/Matrice_inversible

        inv[0] = matrice[5]*matrice[10]*matrice[15] - matrice[5]*matrice[11]*matrice[14] - matrice[9]*matrice[6]*matrice[15] + matrice[9]*matrice[7]*matrice[14] + matrice[13]*matrice[6]*matrice[11] - matrice[13]*matrice[7]*matrice[10];
        inv[4] = -matrice[4]*matrice[10]*matrice[15] + matrice[4]*matrice[11]*matrice[14] + matrice[8]*matrice[6]*matrice[15] - matrice[8]*matrice[7]*matrice[14] - matrice[12]*matrice[6]*matrice[11] + matrice[12]*matrice[7]*matrice[10];
        inv[8] = matrice[4]*matrice[9]*matrice[15] - matrice[4]*matrice[11]*matrice[13] - matrice[8]*matrice[5]*matrice[15] + matrice[8]*matrice[7]*matrice[13] + matrice[12]*matrice[5]*matrice[11] - matrice[12]*matrice[7]*matrice[9];
        inv[12] = -matrice[4]*matrice[9]*matrice[14] + matrice[4]*matrice[10]*matrice[13] + matrice[8]*matrice[5]*matrice[14] - matrice[8]*matrice[6]*matrice[13] - matrice[12]*matrice[5]*matrice[10] + matrice[12]*matrice[6]*matrice[9];
        inv[1] = -matrice[1]*matrice[10]*matrice[15] + matrice[1]*matrice[11]*matrice[14] + matrice[9]*matrice[2]*matrice[15] - matrice[9]*matrice[3]*matrice[14] - matrice[13]*matrice[2]*matrice[11] + matrice[13]*matrice[3]*matrice[10];
        inv[5] = matrice[0]*matrice[10]*matrice[15] - matrice[0]*matrice[11]*matrice[14] - matrice[8]*matrice[2]*matrice[15] + matrice[8]*matrice[3]*matrice[14] + matrice[12]*matrice[2]*matrice[11] - matrice[12]*matrice[3]*matrice[10];
        inv[9] = -matrice[0]*matrice[9]*matrice[15] + matrice[0]*matrice[11]*matrice[13] + matrice[8]*matrice[1]*matrice[15] - matrice[8]*matrice[3]*matrice[13] - matrice[12]*matrice[1]*matrice[11] + matrice[12]*matrice[3]*matrice[9];
        inv[13] = matrice[0]*matrice[9]*matrice[14] - matrice[0]*matrice[10]*matrice[13] - matrice[8]*matrice[1]*matrice[14] + matrice[8]*matrice[2]*matrice[13] + matrice[12]*matrice[1]*matrice[10] - matrice[12]*matrice[2]*matrice[9];
        inv[2] = matrice[1]*matrice[6]*matrice[15] - matrice[1]*matrice[7]*matrice[14] - matrice[5]*matrice[2]*matrice[15] + matrice[5]*matrice[3]*matrice[14] + matrice[13]*matrice[2]*matrice[7] - matrice[13]*matrice[3]*matrice[6];
        inv[6] = -matrice[0]*matrice[6]*matrice[15] + matrice[0]*matrice[7]*matrice[14] + matrice[4]*matrice[2]*matrice[15] - matrice[4]*matrice[3]*matrice[14] - matrice[12]*matrice[2]*matrice[7] + matrice[12]*matrice[3]*matrice[6];
        inv[10] = matrice[0]*matrice[5]*matrice[15] - matrice[0]*matrice[7]*matrice[13] - matrice[4]*matrice[1]*matrice[15] + matrice[4]*matrice[3]*matrice[13] + matrice[12]*matrice[1]*matrice[7] - matrice[12]*matrice[3]*matrice[5];
        inv[14] = -matrice[0]*matrice[5]*matrice[14] + matrice[0]*matrice[6]*matrice[13] + matrice[4]*matrice[1]*matrice[14] - matrice[4]*matrice[2]*matrice[13] - matrice[12]*matrice[1]*matrice[6] + matrice[12]*matrice[2]*matrice[5];
        inv[3] = -matrice[1]*matrice[6]*matrice[11] + matrice[1]*matrice[7]*matrice[10] + matrice[5]*matrice[2]*matrice[11] - matrice[5]*matrice[3]*matrice[10] - matrice[9]*matrice[2]*matrice[7] + matrice[9]*matrice[3]*matrice[6];
        inv[7] = matrice[0]*matrice[6]*matrice[11] - matrice[0]*matrice[7]*matrice[10] - matrice[4]*matrice[2]*matrice[11] + matrice[4]*matrice[3]*matrice[10] + matrice[8]*matrice[2]*matrice[7] - matrice[8]*matrice[3]*matrice[6];
        inv[11] = -matrice[0]*matrice[5]*matrice[11] + matrice[0]*matrice[7]*matrice[9] + matrice[4]*matrice[1]*matrice[11] - matrice[4]*matrice[3]*matrice[9] - matrice[8]*matrice[1]*matrice[7] + matrice[8]*matrice[3]*matrice[5];
        inv[15] = matrice[0]*matrice[5]*matrice[10] - matrice[0]*matrice[6]*matrice[9] - matrice[4]*matrice[1]*matrice[10] + matrice[4]*matrice[2]*matrice[9] + matrice[8]*matrice[1]*matrice[6] - matrice[8]*matrice[2]*matrice[5];

        det = matrice[0]*inv[0] + matrice[1]*inv[4] + matrice[2]*inv[8] + matrice[3]*inv[12];

        assert(det == 0);

        det = 1.0 / det;

        Matrix4x4 matInverse = new Matrix4x4();

        for (i = 0; i < 16; i++)
            matInverse.matrice[i] = inv[i] * det;

        return matInverse;
    }


}
