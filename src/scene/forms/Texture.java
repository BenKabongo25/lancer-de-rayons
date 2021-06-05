package scene.forms;


/**
 * Objet de référencement des propriétés des formes
 */
public class Texture {

    /**
     * Informations sur la coloration de la forme
     */
    protected Pigment pigment;

    public final Pigment getPigment() {
        return pigment;
    }

    public final void setPigment(Pigment pigment) {
        this.pigment = pigment;
    }

    /**
     * Informations sur la finition et les propriétés matérielles
     * et physiques de la forme
     */
    protected Finish finish;

    public Finish getFinish() {
        return finish;
    }

    public void setFinish(Finish finish) {
        this.finish = finish;
    }

    /**
     * Texture
     * @param pigment coloration de la forme
     * @param finish propriétés physiques de la forme
     */
    public Texture(Pigment pigment, Finish finish) {
        this.pigment = pigment;
        this.finish = finish;
    }

    public Texture() {
        this(new Pigment(), new Finish());
    }

    public String logToString() {
        String mLog = "";

        if(pigment != null)
            mLog += "\tpigment\n\t\tpigmentColor = " + pigment.getColor() + "\n";

        if(finish != null)
            mLog  += "\tfinish\n\t\tambient = " + finish.getAmbient() + "\n"+
            "\t\tdiffuse = " + finish.getDiffuse() + "\n"+
            "\t\tspecular = " + finish.getSpecular() + "\n"+
            "\t\tphong = " + finish.getPhong() + "\n"+
            "\t\treflection = " + finish.getReflection() + "\n"+
            "\t\trefraction = " + finish.getRefraction() + "\n"+
            "\t\troughness = " + finish.getRoughness() + "\n";

        return mLog;
    }

}
