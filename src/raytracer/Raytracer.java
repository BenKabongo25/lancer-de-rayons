package raytracer;

import geometry.ColorRGBA;
import geometry.Coords3D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import scene.Scene;
import scene.Ray;
import scene.forms.Form;
import scene.forms.Intersection;
import scene.lights.Light;
import scene.lights.PointLight;

import java.util.List;

import static com.sun.javafx.util.Utils.clamp;

/**
 * Gestion du rendu 3D sur une scène
 */
public class Raytracer {

    // Le raytracing est un procédé plutôt récursif
    // Les rayons bondissent et rebondissent encore et encore sur diverses surfaces de la scène
    // On fixe le maximum de récursion pour éviter une récursion infine
    private static final int MAX_DEPTH = 5;

    /**
     * Scène à rendre
     */
    private Scene scene;

    public final Scene getScene() {
        return scene;
    }

    public Raytracer(Scene scene) {
        this.scene = scene;
    }

    private double calcFresnel(final Coords3D dir, final Coords3D Nml, final double ior) {
        double cosi = clamp(-1, 1, dir.dot(Nml));
        double etai = 1, etat = ior;
        if (cosi > 0) {
            double swap = etai;
            etai = etat;
            etat = swap;
        }
        // Snell, equation expliqué ici : https://www.physicsclassroom.com/class/refrn/Lesson-2/Snell-s-Law
        double sint = etai / etat * Math.sqrt(Math.max(0.f, 1 - cosi * cosi));

        double resultat;
        if (sint >= 1) {
            resultat = 1;
        } else {
            double cost = Math.sqrt(Math.max(0.f, 1 - sint * sint));
            cosi = Math.abs(cosi);
            double Rs = ((etat * cosi) - (etai * cost)) / ((etat * cosi) + (etai * cost));
            double Rp = ((etai * cosi) - (etat * cost)) / ((etai * cosi) + (etat * cost));
            resultat = (Rs * Rs + Rp * Rp) / 2;
        }

        //resultataa = 1 - resultataa;
        return resultat;
    }

    private Coords3D getRefractionVector(final Coords3D dir, final Coords3D Nml, final double ior) {
        double cosi = clamp(-1, 1, dir.dot(Nml));
        double etai = 1, etat = ior;
        Coords3D n = Nml;
        if (cosi < 0) {
            cosi = -cosi;
        } else {
            double temp = etai;
            etai = etat;
            etat = temp;
            n = Nml.scale(-1);
        }
        double eta = etai / etat;
        double k = 1 - (eta * eta) * (1 - (cosi * cosi));
        if (k <= 0) {
            return new Coords3D();
        } else {
            return dir.scale(eta).add(n.scale(((eta * cosi) - Math.sqrt(k))));
        }
    }

    private ColorRGBA getColorPixel(final Ray ray, final int niveauRecursion) {

        ColorRGBA defaultColor = scene.getBackground().clone();

        if(niveauRecursion < 0 )
            return defaultColor;

        // l'intersection la plus proche si elle existe
        Intersection intersection = scene.intersect(ray, false);

        if (intersection == null)
            return defaultColor;

        if (!intersection.isIntersect())
            return defaultColor;

        ColorRGBA diffuse = new ColorRGBA(0,0,0, 0);

        ColorRGBA ambient = intersection.getForm().getTexture().getPigment().getColor().scale( intersection.getForm().getTexture().getFinish().getAmbient() );

        ColorRGBA specular = new ColorRGBA(0,0,0, 0);
        Coords3D mNormal = intersection.getNormal().normalize();
        Coords3D mPosIntersc = intersection.getPosition();
        Coords3D mRayDir = ray.getDirection();//déjà normalisé

        double bias = 0.0001;

        List<PointLight> mLights = scene.getLights();

        for(int i = 0; i < mLights.size(); i++)
        {
            PointLight mLight = mLights.get(i);

            Coords3D mPosLight = mLight.getPosition();
            if(mLight != null)
            {
                Coords3D lightDir = mPosLight.sub(mPosIntersc);
                Coords3D lightDirNormalized = lightDir.normalize();
                Ray RayonPointToLumiere = new Ray ( mPosIntersc.add(mNormal.scale(-bias)), lightDirNormalized);

                Intersection intersectionLumiereEtPoint = scene.intersect(RayonPointToLumiere, true);

                double lumiereEstVisible = 1;
                if(intersectionLumiereEtPoint != null && intersectionLumiereEtPoint.isIntersect() == true)
                {
                    Coords3D obstructionToLumiere = mPosLight.sub(intersectionLumiereEtPoint.getPosition()).normalize();

                    if(obstructionToLumiere.dot(lightDirNormalized) > 0)//On verifie que le vecteur () et le vecteur () n'aie pas une direction opposé
                    {
                        lumiereEstVisible = intersectionLumiereEtPoint.getForm().getTexture().getPigment().getColor().getA();
                    }
                    //lumiereEstVisible = false;
                }

                if(lumiereEstVisible > 0)
                {
                    //diffuse
                    double attenuation = java.lang.Math.pow(lightDir.length() / mLight.getFade_distance(), mLight.getFade_power());
                    //https://www.povray.org/documentation/view/3.6.1/317/ equation décrite dans la documentation povray
                    attenuation = 2.f / (1.f + attenuation);

                    double produitScalaire = mNormal.dot(lightDirNormalized);
                    if(produitScalaire < 0)
                        produitScalaire = 0;

                    diffuse = diffuse.add(mLight.getColor().scale(produitScalaire).scale(attenuation));

                    //specular
                    Coords3D reflectionLightDir = lightDirNormalized.sub(  mNormal.scale(2 *lightDirNormalized.dot(mNormal)) ).normalize();

                    double phong = intersection.getForm().getTexture().getFinish().getPhong();
                    double phongSize = intersection.getForm().getTexture().getFinish().getPhongSize();
                    double specCoef = Math.pow(Math.max( Coords3D.dot(mRayDir, reflectionLightDir), 0.0), phongSize);
                    specular = specular.add(mLight.getColor().scale(attenuation * specCoef * phong));

                    diffuse = diffuse.scale(lumiereEstVisible);
                    specular = specular.scale(lumiereEstVisible);
                }

            }
        }

        double refraction = intersection.getForm().getTexture().getFinish().getRefraction();
        double reflection = intersection.getForm().getTexture().getFinish().getReflection();

        double fresnel = calcFresnel(mRayDir, mNormal, refraction);

        ColorRGBA mRefractionColor = defaultColor;
        ColorRGBA mReflectColor = defaultColor;

        if(refraction > 1)
        {
            if (fresnel < 1) {
                Coords3D refractionDirection = getRefractionVector(mRayDir, mNormal, refraction).normalize();
                Ray rayRefraction = new Ray ( mPosIntersc.add( refractionDirection.scale(0.1)  ), refractionDirection);
                mRefractionColor = getColorPixel(rayRefraction, niveauRecursion-1);
            }
        }

        if(reflection > 0)
        {
            Coords3D reflectionDir = mRayDir.sub(  mNormal.scale(2 *mRayDir.dot(mNormal)) ).normalize();
            Ray rayReflection = new Ray ( mPosIntersc.add(mNormal.scale(bias)),  reflectionDir);
            mReflectColor = getColorPixel(rayReflection, niveauRecursion-1);
        }

        if(refraction <= 1.0 && reflection > 0)
            diffuse = diffuse.add(mReflectColor);
        else if(refraction > 1 || reflection > 0)
            diffuse = diffuse.add(mReflectColor).scale(fresnel).scale(reflection).
                    add( mRefractionColor.scale(1 -fresnel).scale( (refraction > 1)? 1 : 0 ) );

        ColorRGBA mColor = intersection.getForm().getTexture().getPigment().getColor().mul( ambient.add(diffuse).add(specular) );

        return mColor;
    }


    /**
     * rendu de la scène
     */
    public Image render(int width, int height) {
        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Ray ray = scene.getCamera().getRay(x, y);
                ColorRGBA colorRGB = getColorPixel(ray, MAX_DEPTH);
                Color color = new Color(colorRGB.getR(), colorRGB.getG(), colorRGB.getB(), 1);
                pixelWriter.setColor(width-x-1, height-y-1, color);
            }
        }

        return image;
    }

}
