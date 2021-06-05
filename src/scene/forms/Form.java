package scene.forms;

import geometry.Coords3D;
import scene.Identifiable;
import scene.Ray;
import scene.Transformation;


/**
 * Classe mère des objets de la scène
 */

public abstract class Form
        extends Transformation
        implements Identifiable
{

    /**
     * texture de la forme
     */
    protected Texture texture;

    public final Texture getTexture() {
        return texture;
    }

    public final void setTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Forme
     * @param texture texture de la forme
     */
    public Form(Texture texture) {
        this.texture = texture;
        this.id = "";
    }

    public Form() {
        this(new Texture());
    }

    /**
     * @return log de la forme
     */
    public String logToString() {
        return ""; // rédéfinir si besoin pour les classes filles
    }

    /**
     * intersection de la forme courante et d'un rayon
     * @param ray rayon
     * @return Intersection - avec toutes les infos sur l'intersection
     */
    public abstract Intersection intersect(Ray ray);

    // Identification de l'objet

    /**
     * identifiant de l'objet
     */
    protected String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
