package scene.forms;

// Finish
// Il contrôle les propriétés de la surface d'un objet. Cela peut le rendre
// brillant et réfléchissant, ou terne et plat. Il peut également spécifier
// ce qu'il advient de la lumière qui passe à travers les pigments
// transparents, ce qu'il advient de la lumière diffusée par des surfaces
// moins que parfaitement lisses et ce qu'il advient de la lumière réfléchie
// par des surfaces ayant des propriétés d'interférence en couche mince.
// Il existe douze propriétés différentes disponibles dans POV-Ray pour
// spécifier la finition d'un objet donné. Ceux - ci sont contrôlés par les
// mots - clés suivants: ambient, diffuse, brilliance, phong, specular,
// metallic, reflection, crandet iridescence
// Nous n'implémenterons que quelques unes

/**
 * Propriétés matérielles et physiques des formes
 */
public class Finish {

    // Ambient
    // -------
    // Étant donné que les objets dans POV-Ray sont éclairés par des sources
    // de lumière, les parties de ces objets qui sont dans l'ombre seraient
    // complètement noires sans les deux premières propriétés de finition,
    // ambient et diffuse. Ambient est utilisé pour simuler la lumière diffusée
    // autour de la scène qui ne provient pas directement d'une source
    // lumineuse. Diffuse détermine la quantité de lumière vue provenant
    // directement d'une source lumineuse. Ces deux mots clés fonctionnent
    // ensemble pour contrôler la simulation de la lumière ambiante

    private double ambient;

    public double getAmbient() {
        return ambient;
    }

    public void setAmbient(double ambient) {
        this.ambient = ambient;
    }

    private double diffuse;

    public double getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(double diffuse) {
        this.diffuse = diffuse;
    }

    // Phong
    // ----------------------
    // POV-Ray nous donne deux façons de spécifier les reflets spéculaires de surface.
    // Le premier s'appelle la mise en évidence de phong. Habituellement, les faits
    // saillants de phong sont décrits à l'aide de deux mots clés: phong et phong_size.
    // phong détermine la luminosité de la surbrillance tandis que phongSize détermine
    // sa taille

    private double phong;

    public double getPhong() {
        return phong;
    }

    public void setPhong(double phong) {
        this.phong = phong;
    }

    private double phongSize;

    public double getPhongSize() {
        return phongSize;
    }

    public void setPhongSize(double phongSize) {
        this.phongSize = phongSize;
    }

    private double brilliance;

    // Specular
    // ----------
    // Un autre type de surbrillance qui est calculé par un moyen différent appelé
    // mise en évidence spéculaire . Il est spécifié à l'aide du mot-clé specular
    // et fonctionne en conjonction avec un autre mot-clé appelé roughness.
    // Ces deux mots-clés fonctionnent ensemble de la même manière que phong et
    // phong_size pour créer des reflets qui modifient la brillance apparente de la
    // surface

    private double specular;

    public double getSpecular() {
        return specular;
    }

    public void setSpecular(double specular) {
        this.specular = specular;
    }

    private double roughness;

    public double getRoughness() {
        return roughness;
    }

    public void setRoughness(double roughness) {
        this.roughness = roughness;
    }


    /**
     * indique le degré réflection
     */
    private double reflection;

    public double getReflection() {
        return reflection;
    }

    public void setReflection(double reflection) {
        this.reflection = reflection;
    }


    private double refraction;

    public double getRefraction() {
        return refraction;
    }

    public void setRefraction(double refraction) {
        this.refraction = refraction;
    }

    public Finish() {
        ambient     = 0.1;// par défaut situé à 0.10 d'après la doc
        diffuse     = 0;
        phong       = 0.2;
        phongSize   = 40; //par defaut est de 40 : http://www.povray.org/documentation/view/3.6.1/347/
        specular    = 1;
        roughness   = 0;
        reflection  = 0;
        refraction = 1;//1 == pas de refraction, 1.3 refraction de l'eau
    }
}
