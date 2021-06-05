package parser;

import geometry.ColorRGBA;
import geometry.Coords3D;
import scene.Scene;
import scene.forms.*;
import scene.lights.PointLight;
import scene.Camera;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//nous allons lire un fichier .pov
//Ce formart étant très complet http://www.povray.org/documentation/3.7.0/r3_3.html#r3_3_1_2
//Nous allons nous restreindre à une implémentation minimal qui nous permettrat de déssiner un scene
// avec caméra/objets/lumière
//Les textures ne seront pas traitées

//Pour créer notre parser/importer nous utiliseront la description du format expliquée :
//[0] https://en.wikipedia.org/wiki/POV-Ray
//[1] https://www.fileformat.info/format/pov/egff.htm
//[2] http://www.f-lohmueller.de/pov_tut/pov_inc/var_000e.htm

//fonction qui cherche à partir de input[0] un match parmis une liste de String

public class ParsePOV {

    String rechercheString(String input, List<String> liste)
    {
        for(int i = 0; i < liste.size(); i++)
        {
            // si l'input est plus petite que l'identifiant, on sait qu'il n'y a pas match
            if(input.length() < liste.get(i).length())
                continue;

            boolean match = true;

            for(int j = 0; j < liste.get(i).length() && j < input.length(); j++)
            {
                if(input.charAt(j) != liste.get(i).charAt(j))
                {
                    match = false;
                    break;
                }
            }

            if(match)
                return liste.get(i);
        }

        return null;
    }

    HashMap<String, ArrayList<Double> > splitComponant(String input, List<String> liste)
    {
        HashMap<String, ArrayList<Double> > hashMap = new HashMap<>();

        //Ces deux regex sont utilisé uniquement pour detecter des erreurs.


        //-----Détecte les champs mal remplies
        //Détecte des champs du type : ' <nombre, > '
        //exemple : '  <123, > ' ce champ est érroné et va être detecté par notre regex, un champ valide aurait pu être ' < 123, 456>  '
        String regexOnlyb1fb = "^[^a-zA-Z\\-<\\{]*" +
                "<\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,?" +
                "\\s*>" +
                "[\\s\\S]*";
        //Détecte des champs du type : ' <nombre, nombre, > '
        //exemple : '  <123, 456, > ' ce champ est érroné et va être detecté par notre regex, un champ valide aurait pu être ' < 123, 456, 789>  '
        String regexOnlyb2fb = "^[^a-zA-Z\\-<\\{]*" +
                "<\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,?" +
                "\\s*>" +
                "[\\s\\S]*";

        //-----Détecte les champs valides
        //Détecte le prochain nombre entier ou flottant
        //exemple : '  -45.656 '
        String regexOnly1f = "^[^a-zA-Z0-9\\-<\\{]*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "[\\s\\S]*";

        //Détecte le prochain groupe de 3 nombres entouré de '<' et '>'
        //exemple : '   <-45.656, 1, 0 > '
        String regexOnly3f = "^[^a-zA-Z\\-<\\{]*" +
                "<\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*>" +
                "[\\s\\S]*";

        //Détecte le prochain groupe de 4 nombres entouré de '<' et '>'
        //exemple : '   <-45.656, 1, 0 , 3> '
        String regexOnly4f = "^[^a-zA-Z\\-<\\{]*" +
                "<\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*>" +
                "[\\s\\S]*";

        //Détecte le prochain groupe de 3 nombres entouré de '<' et '>', suivie d'un nombre
        //exemple : '   <1, 2, 3>  1'
        String regexOnly3f1f = "^[^a-zA-Z\\-<\\{]*" +
                "<\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +

                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*>\\s*,?\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "[\\s\\S]*";

        //Détecte le prochain groupe de 3 nombres entouré de '<' et '>', suivie d'un groupre de 3 nombre entouré de '<' et '>'
        //exemple : '   <1, 2, 3>  <4, 5, 6> '
        String regexOnly3f3f = "^[^a-zA-Z\\-<\\{]*" +
                "<\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*>\\s*" +

                ",?\\s*<\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*>\\s*" +

                "[\\s\\S]*";

        //Détecte le prochain groupe de 3 nombres entouré de '<' et '>', suivie d'un groupre de 3 nombre entouré de '<' et '>', suivie d'un nombre
        //exemple : '   <1, 2, 3>  <4, 5, 6> 7'
        String regexOnly3f3f1f = "^[^a-zA-Z\\-<\\{]*" +
                "<\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*>\\s*" +

                ",?\\s*<\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*>\\s*,?\\s*" +

                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +

                "[\\s\\S]*";

        //Détecte le prochain groupe de 3 nombres entouré de '<' et '>', suivie d'un nombre
        //suivie d'un groupre de 3 nombre entouré de '<' et '>', suivie d'un nombre
        //exemple : ' <1, 2, 3> 1 <4, 5, 6> 1'
        String regexOnly3f1f3f1f = "^[^a-zA-Z\\-<\\{]*" +
                "<\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +

                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*>\\s*,?\\s*" +

                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
//
                ",?\\s*<\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*,\\s*" +
                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                "\\s*>\\s*,?\\s*" +

                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +

                "[\\s\\S]*";

        String[] collectionRegex = new String[9];

        collectionRegex[0] = regexOnly3f1f3f1f;
        collectionRegex[1] = regexOnly3f3f1f;
        collectionRegex[2] = regexOnly3f3f;
        collectionRegex[3] = regexOnly3f1f;
        collectionRegex[4] = regexOnly4f;
        collectionRegex[5] = regexOnly3f;
        collectionRegex[6] = regexOnly1f;

        collectionRegex[7] = regexOnlyb2fb;//Ces deux dernier regex sont rajoutés pour detecter des fichiers .pov mal parsés
        collectionRegex[8] = regexOnlyb1fb;

        // on cherche les coordonnées obligatoire sans identificateurs
        {
            ArrayList<Double> coords = searchRegex(input, collectionRegex, 0, 2);
            if(coords != null)
                hashMap.put("paramObligatoire", coords);
        }

        for(int indListe = 0; indListe < liste.size(); indListe++) // on cherches les coordonnées avec identificateurs
        {
            int indexIdentifiant = input.indexOf(liste.get(indListe)); // on récupére l'index d'un identifiant de la liste

            if(indexIdentifiant != -1)
            {
                // on se positione à la suite de là où se trouve l'identificateur
                String sub = input.substring(indexIdentifiant + liste.get(indListe).length() );

                ArrayList<Double> coords = searchRegex(sub, collectionRegex, 0, 2);
                if(coords != null)
                    hashMap.put(liste.get(indListe), coords);
            }
        }

        return hashMap;
    }

    //fonction qui renvoie les positions des Bracket paires.
    //exemple _{_ { { } } _}_
    int[] getNextDebutFinBracket(String input, int pos, char CaractereDebut, char CaractereFin)
    {
        int compteurBracket = 0;
        boolean bracketFound = false;

        int premier = 0, dernier = 0;

        for(int i = pos; i < input.length(); i++) {

            if(input.charAt(i) == CaractereDebut)
            {
                compteurBracket++;
                if(!bracketFound)
                    premier = i;

                bracketFound = true;
            }

            if(input.charAt(i) == CaractereFin)
            {
                compteurBracket--;
                if(compteurBracket == 0)
                {
                    dernier = i;
                    break;
                }
            }
        }
        int[] positions = new int[2];

        // si au moins un bracket ouvert à  été trouvé et que le compteurBracket == 0, signifie un succès
        if(bracketFound && compteurBracket == 0)
        {
            positions[0] = premier;
            positions[1] = dernier;
            return positions;
        }
        else
            return null;
    }

    //Cette premiere passe enlève les commentaires
    //Puis stock les variable #local var; ou #declare var dans une hashmap ;
    public String premierePasse(String input)
    {
        HashMap<String, ArrayList<Double> > cacheVariable = new HashMap<String, ArrayList<Double> >();

        List<String> identifiantList = Arrays.asList("//", "#local", "#declare");
        for(int pos = 0; pos < input.length(); ) {
            String inputAt = input.substring(pos);
            String idIdentifiant = rechercheString(inputAt, identifiantList);
            if (idIdentifiant != null) //si un identifiant a été trouvé
            {
                if(idIdentifiant.equals("//"))// Ceci est un commentaire, on déplace donc le curseur à la prochaine ligne
                {
                    int indiceRetourLigne = input.indexOf('\n', pos);
                    if(indiceRetourLigne == -1)
                    {
                        input = input.substring(0, pos);
                        break;
                    }
                    else
                        input = input.substring(0, pos) + input.substring(indiceRetourLigne, input.length());
                }
                //si le token est une variable, alors on l'ajoute à la hashMap cacheVariable
                else if (idIdentifiant.equals("#local") || idIdentifiant.equals("#declare"))
                {
                    // Cette solution est temporaire, la vrai solution consisterai à pouvoir modifier les variables pendant
                    // le lancement du script
                    int indicePointVirgule = input.indexOf(';', pos);

                    if(indicePointVirgule != -1)
                    {
                        //-> #declare %s = %f;

                        //Détecte les lignes comprennant un '#declare' ou '#local', suivie d'un nom d'une variable, suivie d'un égale '=' puis d'un nombre entier ou flottant
                        //exemple :  #declare foobar = 465.3;
                        String regexOnly1f = "^(#declare|#local)[\\s]*([a-zA-Z\\_]+)+[\\s]*=[\\s]*([\\-]?([0-9]*[\\.])?[0-9]+)+";

                        //Détecte les lignes comprennant un '#declare' ou '#local', suivie d'un nom d'une variable, suivie d'un égale '=', suivie d'un '<'
                        //(suivie d'un nombre, suivie d'une virgule) x 3
                        //et se terminant par un '>'
                        //exemple :  #local foobar = <1, 234.53434, 2>;
                        String regexOnly3f = "^(#declare|#local)[\\s]*([a-zA-Z\\_]+)+[\\s]*=[\\s]*" +
                                "<\\s*" +
                                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                                "\\s*,\\s*" +
                                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                                "\\s*,\\s*" +
                                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                                "\\s*>" +
                                "[\\s\\S]*";

                        //Détecte les lignes comprennant un '#declare' ou '#local', suivie d'un nom d'une variable, suivie d'un égale '=', suivie d'un '<'
                        //(suivie d'un nombre, suivie d'une virgule) x 4
                        //et se terminant par un '>'
                        //exemple :  #local foobar = <1, 234.53434, 2, 3465.3>;
                        String regexOnly4f = "^(#declare|#local)[\\s]*([a-zA-Z\\_]+)+[\\s]*=[\\s]*" +
                                "<\\s*" +
                                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                                "\\s*,\\s*" +
                                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                                "\\s*,\\s*" +
                                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                                "\\s*,\\s*" +
                                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                                "\\s*>" +
                                "[\\s\\S]*";

                        String[] collectionRegex = new String[3];

                        collectionRegex[0] = regexOnly4f;
                        collectionRegex[1] = regexOnly3f;
                        collectionRegex[2] = regexOnly1f;


                        // similaire à searchRegex(), mais récupére également le nom des clés
                        for(int indReg = 0; indReg < collectionRegex.length; indReg++)
                        {
                            Matcher matcher = Pattern.compile(collectionRegex[indReg]).matcher( input.substring(pos, indicePointVirgule) );
                            if(matcher.matches())
                            {
                                ArrayList<Double> coords = new ArrayList<Double>();

                                String key = matcher.group(2);

                                for(int i = 0; i < (matcher.groupCount()-2)/2; i++)
                                {
                                    double valeur = Double.parseDouble(matcher.group(3+(i*2)));
                                    coords.add( valeur);
                                }

                                cacheVariable.put(key, coords); // on ajoute la valeur et son nom dans le cache
                                break;
                            }
                        }
                        //

                        input = input.substring(0, pos) + input.substring(indicePointVirgule+1, input.length());
                    }
                    else
                    {
                        input = input.substring(0, pos);
                        break;
                    }
                }
            }
            else
                pos++;
        }

        Iterator it = cacheVariable.entrySet().iterator();
        while (it.hasNext()) {
            // Ici on cherche toutes les occurences des variables, et on les remplacent par les valeurs qui leurs sont associées
            //Map.Entry<String, Double> pair = (Map.Entry<String, Double>)it.next();
            Map.Entry<String, ArrayList<Double> > pair = (Map.Entry<String, ArrayList<Double> >)it.next();

            while(input.contains(pair.getKey()))
            {
                int pos = input.indexOf(pair.getKey());

                String remplacement = "";

                ArrayList<Double> listDouble = pair.getValue();

                if(listDouble.size() == 1) // Si la variable ne comporte qu'un seul composant
                    remplacement = "" + listDouble.get(0);
                else // si la variable comporte plusieurs composants, on ajoute des <> et des virgules
                {
                    remplacement = "<";

                    for(int i = 0; i  < listDouble.size(); i++)
                    {
                        remplacement += "" + listDouble.get(i);

                        if(i+1 < listDouble.size()) //si il reste d'autre composants à ajouter, on sépare par une virgule
                            remplacement += ", ";
                    }

                    remplacement += ">";
                }

                input = input.replace(pair.getKey(), remplacement);
            }
        }

        //System.out.println("input : \n" + input +"\n\n\n\n\n\n\n");
        return input;
    }

    public ArrayList<Double> searchRegex(String input, String []collectionRegex, int offset, int stride)
    {
        for(int indReg = 0; indReg < collectionRegex.length; indReg++)
        {
            Matcher matcher = Pattern.compile(collectionRegex[indReg]).matcher( input );
            if(matcher.matches())
            {
                ArrayList<Double> coords = new ArrayList<Double>();

                for(int i = 0; i < (matcher.groupCount()-offset)/stride; i++)
                {
                    double valeur = Double.parseDouble(matcher.group(1 + offset+(i*stride)));
                    coords.add( valeur);
                }

                return coords;
            }
        }

        return null;
    }

    public Scene importPov(String input)
    {
        //cette premiere passe detecte les variables (string précédés de #declare ou #local)
        //Puis récupere les valeurs qui lui sont associés
        // Et enfin remplace dans le reste du code chaque appel des variables par leurs valeurs

        //#declare Red = <1.0, 0, 0.0>;
        //box { ... texture { pigment { color Red } } }

        //Après appel de premierePasse()
        //box { ... texture { pigment { color <1.0, 0.0, 0.0> } } }
        input = premierePasse(input);



        List<String> identifiantList = Arrays.asList("global_settings", "background", "camera", "light_source", "box", "sphere", "plane", "cylinder", "cone");
        List<String> sousIdentifiantList = Arrays.asList( "rgb", "location", "look_at", "translate", "scale", "rotate", "box", "fade_distance", "fade_power", "angle");

        List<String> tokenFinish = Arrays.asList( "reflection", "phong", "ambient", "diffuse", "specular", "roughness");

        Scene mScene = new Scene();

        for(int pos = 0; pos < input.length(); )
        {
            String inputAt = input.substring(pos);
            String idIdentifiant = rechercheString(inputAt, identifiantList);
            if(idIdentifiant != null) //si un identifiant a été trouvé
            {
                pos+=idIdentifiant.length();

                //Sinon c'est un identifiant suivie de { } (idIdentifiant == "camera")// Ceci est un commentaire,
                // on déplace le curseur à la prochaine ligne
                {
                    //cette fonction est importante, et ne peux pas être remplacé par un simple
                    // input.indexOf('{', pos) input.indexOf('}', pos)
                    //permet de prendre en compte les cas ou il y plusieurs curly bracket { } imbriqués
                    int[] debutFin = getNextDebutFinBracket(input, pos, '{', '}');

                    if(debutFin != null)
                    {
                        String sousComposant = input.substring(debutFin[0]+1, debutFin[1]);//on créer une sous chaine entre les a
                        String sousComposantTexture;

                        ArrayList<Double> pigmentColor = null;
                        int posPigment = sousComposant.indexOf("pigment");
                        if(posPigment != -1) // si une texture est detecté, alors on la sépare
                        {
                            int[] debutFinPigment = getNextDebutFinBracket(sousComposant, posPigment, '{', '}');
                            if(debutFinPigment != null)
                            {
                                String sousComposantPigment = sousComposant.substring(debutFinPigment[0]+1, debutFinPigment[1]);
                                String[] collectionRegex = new String[2];

                                String regexOnly1fAvecCaracter = "^[^0-9\\-<\\{]*" + // tout sauf des chiffres ou un signes '-' ou un signe '<'
                                        "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                                        "[\\s\\S]*";

                                String regexOnly3f =
                                        ".*<\\s*" +
                                                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                                                "\\s*,\\s*" +
                                                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                                                "\\s*,\\s*" +
                                                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                                                "\\s*>" +
                                                "[\\s\\S]*";

                                String regexOnly4f =
                                        ".*<\\s*" +
                                                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                                                "\\s*,\\s*" +
                                                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                                                "\\s*,\\s*" +
                                                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                                                "\\s*,\\s*" +
                                                "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                                                "\\s*>" +
                                                "[\\s\\S]*";

                                collectionRegex[0] = regexOnly4f;
                                collectionRegex[1] = regexOnly3f;

                                pigmentColor = searchRegex(sousComposantPigment, collectionRegex, 0, 2);

                                //if(pigmentColor != null)
                                //   System.out.println("color " + pigmentColor);
                            }
                        }


                        Finish finishInstance = new Finish();
                        int posFinish = sousComposant.indexOf("finish");
                        if(posFinish != -1) // si une texture est detecté, alors on la sépare
                        {
                            int[] debutFinFinish = getNextDebutFinBracket(sousComposant, posFinish, '{', '}');
                            if(debutFinFinish != null)
                            {
                                String sousComposantFinish = sousComposant.substring(debutFinFinish[0]+1, debutFinFinish[1]);
                                String[] collectionRegex = new String[1];

                                String regexOnly1fAvecCaracter = "^[^0-9\\-<\\{\\.]*" + // tout sauf des chiffres ou un signes '-' ou un signe '<' ou '{' ou '.'
                                        "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                                        "[\\s\\S]*";

                                collectionRegex[0] = regexOnly1fAvecCaracter;

                                for(int i = 0; i < tokenFinish.size(); i++)
                                {
                                    String mToken = tokenFinish.get(i);
                                    int posToken = sousComposantFinish.indexOf(mToken);

                                    if(posToken != -1)
                                    {
                                        ArrayList<Double> retour = searchRegex(sousComposantFinish.substring(posToken), collectionRegex, 0, 2);

                                        if(retour != null && retour.size() > 0)
                                        {
                                            //System.out.println("token_" + mToken+ "_ " + retour + " -> " + (mToken.equals("reflection") == true) );

                                            if(mToken.equals("reflection") == true)
                                                finishInstance.setReflection(retour.get(0));
                                            if(mToken.equals("phong") == true)
                                                finishInstance.setPhong(retour.get(0));
                                            if(mToken.equals("ambient") == true)
                                                finishInstance.setAmbient(retour.get(0));
                                            if(mToken.equals("diffuse") == true)
                                                finishInstance.setDiffuse(retour.get(0));
                                            if(mToken.equals("specular") == true)
                                                finishInstance.setSpecular(retour.get(0));
                                            if(mToken.equals("roughness") == true)
                                                finishInstance.setRoughness(retour.get(0));
                                        }
                                    }
                                }
                            }
                        }


                        int posInterior = sousComposant.indexOf("interior");
                        if(posInterior != -1) // si une texture est detecté, alors on la sépare
                        {
                            int[] debutFinInterior = getNextDebutFinBracket(sousComposant, posInterior, '{', '}');
                            if(debutFinInterior != null)
                            {
                                String sousComposantInterior = sousComposant.substring(debutFinInterior[0]+1, debutFinInterior[1]);
                                String[] collectionRegex = new String[1];

                                String regexOnly1fAvecCaracter = "^[^0-9\\-<\\{\\.]*" + // tout sauf des chiffres ou un signes '-' ou un signe '<' ou '{' ou '.'
                                        "([\\-]?([0-9]*[\\.])?[0-9]+)+" +
                                        "[\\s\\S]*";

                                collectionRegex[0] = regexOnly1fAvecCaracter;

                                String mToken = "ior";
                                int posToken = sousComposantInterior.indexOf(mToken);
                                if(posToken != -1)
                                {
                                    ArrayList<Double> retour = searchRegex(sousComposantInterior.substring(posToken), collectionRegex, 0, 2);

                                    if(retour != null && retour.size() > 0)
                                    {
                                        //System.out.println("token_" + mToken+ " " + retour);

                                        if(mToken.equals("ior") == true)
                                            finishInstance.setRefraction(retour.get(0));
                                    }
                                }
                            }
                        }

                        //On recherche si l'objet contient une texture
                        int posTexture = sousComposant.indexOf("texture");
                        if(posTexture != -1) // si une texture est detecté, alors on la sépare
                        {
                            int[] debutFinTexture = getNextDebutFinBracket(sousComposant, posTexture, '{', '}');
                            if(debutFinTexture != null)
                            {
                                sousComposantTexture = sousComposant.substring(debutFinTexture[0]+1, debutFinTexture[1]);

                                sousComposant = sousComposant.substring(0, debutFinTexture[0]) +
                                        sousComposant.substring(debutFinTexture[1]+1, sousComposant.length());
                            }
                        }

                        //la fonction qui va chercher tous les composants et les mettre dans un hashmap en fonction de
                        // leur identifiants
                        HashMap<String, ArrayList<Double> > hashMap = splitComponant(sousComposant, sousIdentifiantList);

                        boolean componantEstUnObjet = false;
                        if(idIdentifiant.equals("camera"))
                        {
                            Camera mCamera = new Camera();
                            if(hashMap.containsKey("location"))
                            {
                                ArrayList<Double> coord = hashMap.get("location");
                                if( coord.size() < 3 )
                                    mScene.setLogErreur(mScene.getLogErreur() +
                                            "\nErreur parsing Camera location, requiert 3 paramètres mais " +
                                            coord.size() + " trouvés");
                                else
                                    mCamera.setPosition(new Coords3D(coord.get(0), coord.get(1), coord.get(2)));
                            }
                            if(hashMap.containsKey("look_at"))
                            {
                                ArrayList<Double> coord = hashMap.get("look_at");
                                if( coord.size() < 3 )
                                    mScene.setLogErreur(mScene.getLogErreur() +
                                            "\nErreur parsing Camera look_at, requiert 3 paramètres mais " +
                                            coord.size() + " trouvés");
                                else
                                    mCamera.setLookAt(new Coords3D(coord.get(0), coord.get(1), coord.get(2)));
                            }
                            if(hashMap.containsKey("angle"))
                            {
                                ArrayList<Double> coord = hashMap.get("angle");
                                if( coord.size() != 1 )
                                    mScene.setLogErreur(mScene.getLogErreur() +
                                            "\nErreur parsing Camera look_at, requiert 3 paramètres mais " +
                                            coord.size() + " trouvés");
                                else
                                    mCamera.setFov(coord.get(0));
                            }

                            mScene.setCamera(mCamera);
                        }

                        else if(idIdentifiant.equals("light_source"))
                        {
                            PointLight mLight = new PointLight();

                            if(hashMap.containsKey("paramObligatoire")) // similaire à translation
                            {
                                ArrayList<Double> coord = hashMap.get("paramObligatoire");
                                if( coord.size() < 3 )
                                    mScene.setLogErreur(mScene.getLogErreur() +
                                            "\nErreur parsing light_source paramObligatoire, requiert 3 paramètres mais " +
                                            coord.size() + " trouvés");
                                else
                                    mLight.setPosition(new Coords3D(coord.get(0), coord.get(1), coord.get(2)));
                            }
                            if(hashMap.containsKey("translate"))
                            {
                                ArrayList<Double> coord = hashMap.get("translate");
                                if( coord.size() < 3 )
                                    mScene.setLogErreur(mScene.getLogErreur() +
                                            "\nErreur parsing light_source translate, requiert 3 paramètres mais " +
                                            coord.size() + " trouvés");
                                else
                                    mLight.setPosition(new Coords3D(coord.get(0), coord.get(1), coord.get(2)));
                            }
                            if(hashMap.containsKey("rgb"))
                            {
                                ArrayList<Double> coord = hashMap.get("rgb");
                                if( coord.size() < 3 )
                                    mScene.setLogErreur(mScene.getLogErreur() +
                                            "\nErreur parsing light_source rgb, requiert 3 paramètres mais " +
                                            coord.size() + " trouvés");
                                else
                                {
                                    if(coord.size() == 3)
                                        mLight.setColor(new ColorRGBA(coord.get(0), coord.get(1), coord.get(2), 0));
                                    if(coord.size() == 4)
                                        mLight.setColor(new ColorRGBA(coord.get(0), coord.get(1), coord.get(2), coord.get(3)));
                                }

                            }
                            if(hashMap.containsKey("fade_distance"))
                            {
                                ArrayList<Double> coord = hashMap.get("fade_distance");

                                if(coord.size() != 1)
                                {
                                    mScene.setLogErreur(mScene.getLogErreur() +
                                            "\nErreur parsing light_source fade_distance, requiert 1 paramètre mais " +
                                            coord.size() + " trouvés");
                                }
                                else {
                                    mLight.setFade_distance(coord.get(0));
                                    //System.out.println("coord fade_distance " + coord);
                                }
                            }
                            if(hashMap.containsKey("fade_power"))
                            {
                                ArrayList<Double> coord = hashMap.get("fade_power");

                                if(coord.size() != 1)
                                {
                                    mScene.setLogErreur(mScene.getLogErreur() +
                                            "\nErreur parsing light_source fade_power, requiert 1 paramètre mais " +
                                            coord.size() + " trouvés");
                                }
                                else {
                                    mLight.setFade_power(coord.get(0));
                                    //System.out.println("coord fade_power " + coord);
                                }
                            }

                            mScene.addLight(mLight);
                        }

                        else if(idIdentifiant.equals("box"))
                        {
                            Box box = new Box();
                            componantEstUnObjet = true;
                            if(hashMap.containsKey("paramObligatoire")) // similaire à translation
                            {
                                ArrayList<Double> coord = hashMap.get("paramObligatoire");

                                if( coord.size() < 6 )
                                    mScene.setLogErreur(mScene.getLogErreur() +
                                            "\nErreur parsing box paramObligatoire, requiert 3 + 3 paramètres mais " +
                                            coord.size() + " trouvés");
                                else
                                {
                                    box.setBoundingBoxMin(new Coords3D(coord.get(0), coord.get(1), coord.get(2)));
                                    box.setBoundingBoxMax(new Coords3D(coord.get(3), coord.get(4), coord.get(5)));
                                }
                            }
                            mScene.addForm(box);
                        }

                        else if(idIdentifiant.equals("sphere"))
                        {
                            Sphere sphere = new Sphere();
                            componantEstUnObjet = true;
                            if(hashMap.containsKey("paramObligatoire")) // similaire à translation
                            {
                                ArrayList<Double> coord = hashMap.get("paramObligatoire");
                                if( coord.size() < 4 )
                                    mScene.setLogErreur(mScene.getLogErreur() +
                                            "\nErreur parsing sphere paramObligatoire, requiert 3 + 1 paramètres mais " +
                                            coord.size() + " trouvés");
                                else
                                {
                                    sphere.setPosition(new Coords3D(coord.get(0), coord.get(1), coord.get(2)));
                                    sphere.setRadius(coord.get(3));
                                }
                            }
                            mScene.addForm(sphere);
                        }

                        else if(idIdentifiant.equals("plane"))
                        {
                            Plane plane = new Plane();
                            componantEstUnObjet = true;
                            if(hashMap.containsKey("paramObligatoire")) // similaire à translation
                            {
                                ArrayList<Double> coord = hashMap.get("paramObligatoire");
                                if( coord.size() < 3 )
                                    mScene.setLogErreur(mScene.getLogErreur() +
                                            "\nErreur parsing plane paramObligatoire, requiert 3 paramètres mais " +
                                            coord.size() + " trouvés");
                                else
                                {
                                    plane.setNormal(new Coords3D(coord.get(0), coord.get(1), coord.get(2)));
                                    plane.setOffset(coord.get(3));
                                }
                            }
                            mScene.addForm(plane);
                        }
                        else if(idIdentifiant.equals("cylinder"))
                        {
                            Cylinder cylindre = new Cylinder();
                            componantEstUnObjet = true;
                            if(hashMap.containsKey("paramObligatoire")) // similaire à translation
                            {
                                ArrayList<Double> coord = hashMap.get("paramObligatoire");
                                if( coord.size() < 7 )
                                    mScene.setLogErreur(mScene.getLogErreur() +
                                            "\nErreur parsing cylinder paramObligatoire, requiert 7 paramètres mais " +
                                            coord.size() + " trouvés");
                                else
                                {
                                    cylindre.setFirstSide(new Coords3D(coord.get(0), coord.get(1), coord.get(2)));
                                    cylindre.setSecondSide(new Coords3D(coord.get(3), coord.get(4), coord.get(5)));
                                    cylindre.setRadius(coord.get(6));
                                }
                            }
                            mScene.addForm(cylindre);
                        }
                        else if(idIdentifiant.equals("cone"))
                        {
                            Cone cone = new Cone();
                            componantEstUnObjet = true;
                            if(hashMap.containsKey("paramObligatoire")) // similaire à translation
                            {
                                ArrayList<Double> coord = hashMap.get("paramObligatoire");
                                if( coord.size() < 8 )
                                    mScene.setLogErreur(mScene.getLogErreur() +
                                            "\nErreur parsing cone paramObligatoire, requiert 8 paramètres mais " +
                                            coord.size() + " trouvés");
                                else
                                {
                                    cone.setFirstSide(new Coords3D(coord.get(0), coord.get(1), coord.get(2)));
                                    cone.setFirstRadius(coord.get(3));
                                    cone.setSecondSide(new Coords3D(coord.get(4), coord.get(5), coord.get(6)));
                                    cone.setSecondRadius(coord.get(7));
                                }
                            }
                            mScene.addForm(cone);
                        }
                        else if(idIdentifiant.equals("background"))
                        {
                            if(hashMap.containsKey("rgb")) // similaire à translation
                            {
                                ArrayList<Double> coord = hashMap.get("rgb");
                                if( coord.size() < 3 )
                                    mScene.setLogErreur(mScene.getLogErreur() +
                                            "\nErreur parsing background rgb, requiert 3 paramètres mais " +
                                            coord.size() + " trouvés");
                                else
                                    mScene.setBackground(new ColorRGBA(coord.get(0), coord.get(1), coord.get(2), 1));
                            }
                        }


                        if(componantEstUnObjet == true)
                        {
                            Form mForm = mScene.getLastFormAdded();
                            Texture mTexture = new Texture();

                            if(pigmentColor != null && pigmentColor.size() >= 3)
                            {
                                if(pigmentColor.size() == 3)
                                    mTexture.setPigment( new Pigment(new ColorRGBA(pigmentColor.get(0), pigmentColor.get(1), pigmentColor.get(2), 0)) );
                                else if(pigmentColor.size() == 4)
                                    mTexture.setPigment( new Pigment(new ColorRGBA(pigmentColor.get(0), pigmentColor.get(1), pigmentColor.get(2), pigmentColor.get(3))) );
                            }

                            mTexture.setFinish(finishInstance);
                            mForm.setTexture(mTexture);

                            if(hashMap.containsKey("translate"))
                            {
                                ArrayList<Double> coord = hashMap.get("translate");
                                System.out.println("box translate " + coord.size());
                                if( coord.size() < 3 )
                                    mScene.setLogErreur(mScene.getLogErreur() +
                                            "\nErreur parsing translate, requiert 3 paramètres mais " +
                                            coord.size() + " trouvés");
                                else
                                    mForm.setPosition(new Coords3D(coord.get(0), coord.get(1), coord.get(2)));
                            }
                            if(hashMap.containsKey("rotate"))
                            {
                                ArrayList<Double> coord = hashMap.get("rotate");
                                if( coord.size() < 3 )
                                    mScene.setLogErreur(mScene.getLogErreur() +
                                            "\nErreur parsing rotate, requiert 3 paramètres mais " +
                                            coord.size() + " trouvés");
                                else
                                    mForm.setRotation(new Coords3D(coord.get(0), coord.get(1), coord.get(2)));
                            }
                            if(hashMap.containsKey("scale"))
                            {
                                ArrayList<Double> coord = hashMap.get("scale");
                                if( coord.size() < 3 )
                                    mScene.setLogErreur(mScene.getLogErreur() +
                                            "\nErreur parsing scale, requiert 3 paramètres mais " +
                                            coord.size() + " trouvés");
                                else
                                    mForm.setScale(new Coords3D(coord.get(0), coord.get(1), coord.get(2)));
                            }
                        }

                        pos = debutFin[1]; // on met à jour la position du buffer, en ajoutant ce que nous venons de traiter
                    }
                }
            }
            else//Si aucun identifiant n'a été trouvé on passe au caractere suivant
                pos++;
        }

        return mScene;
    }

}
