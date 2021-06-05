package scene;

/**
 * Interface d'indentification des objets
 */
public interface Identifiable {

    /**
     * @return l'identifiant de l'objet
     */
    String getId();

    /**
     * modifie l'identifiant d'un objet
     * @param id
     */
    void setId(String id);

}
