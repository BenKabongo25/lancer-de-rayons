package gui;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Controleur extends ActionEvent {
    Modele modele;
    Stage stage = null;

    Controleur(Stage stage, Modele modele)
    {
        this.modele = modele;
        this.stage = stage;
    }

    public void handleResetChange(ActionEvent event, Button button) {

        TextArea texteEntree = (TextArea) stage.getScene().lookup("#texteEntree");

        if(texteEntree != null)
        {
            texteEntree.setText(modele.getDefaultPOV());
        }
    }

    public void handleChangeTexte(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {

        TextArea texteSortie = (TextArea) stage.getScene().lookup("#texteSortie");
        TextArea texteErreur = (TextArea) stage.getScene().lookup("#texteErreur");
        TextArea texteEntree = (TextArea) stage.getScene().lookup("#texteEntree");

        if(texteSortie != null && texteEntree != null && texteErreur != null)
        {
            texteSortie.setText("LOG :");
            texteErreur.setText("LOG d'erreur:");

            modele.updateScene(texteEntree.getText());

            //met à jour le texte de sortie et d'erreur
            String mLog = modele.getScene().logToString();
            texteSortie.setText(texteSortie.getText() + "\n" + mLog);
            String mLogErreur = modele.getScene().getLogErreur();
            if(mLogErreur.length() > 0)
                texteErreur.setText(texteErreur.getText() + "\n________________________\n" + mLogErreur);

            //Scroll automatiquement au bas du texte
            texteSortie.selectPositionCaret(texteSortie.getLength());
            texteSortie.deselect();
            texteErreur.selectPositionCaret(texteErreur.getLength());
            texteErreur.deselect();
        }
    }

    public void handleLoad(ActionEvent event, Button button) {

        TextArea texteEntree = (TextArea) stage.getScene().lookup("#texteEntree");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(stage);

        if(file != null)
        {
            String data = "";
            try {

                modele.setFilePathSave(file.getAbsolutePath());
                data = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));

                if(texteEntree != null)
                {
                    modele.setDefaultPOV(data);
                    texteEntree.setText(data);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleSave(ActionEvent event, Button button) {

        TextArea texteEntree = (TextArea) stage.getScene().lookup("#texteEntree");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder Fichier .pov");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fichier .POV", "*.pov", "*.txt"));
        File file = fileChooser.showSaveDialog(stage);

        if(texteEntree != null && file != null)
        {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()));

                writer.write(texteEntree.getText());

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleCleanLog(ActionEvent event, Button button) {

        TextArea texteSortie = (TextArea) stage.getScene().lookup("#texteSortie");
        TextArea texteErreur = (TextArea) stage.getScene().lookup("#texteErreur");

        if(texteSortie != null)
            texteSortie.setText("LOG :");

        if(texteErreur != null)
            texteErreur.setText("LOG d'erreur:");
    }

    public void handleGenerer(ActionEvent event, Button button) {

        TextArea texteSortie = (TextArea) stage.getScene().lookup("#texteSortie");
        TextArea texteErreur = (TextArea) stage.getScene().lookup("#texteErreur");
        TextArea texteEntree = (TextArea) stage.getScene().lookup("#texteEntree");

        if(texteSortie != null && texteEntree != null && texteErreur != null)
        {
            modele.updateScene(texteEntree.getText());

            //met à jour le texte de sortie et d'erreur
            String mLog = modele.getScene().logToString();
            texteSortie.setText(texteSortie.getText() + "\n" + mLog);
            String mLogErreur = modele.getScene().getLogErreur();
            if(mLogErreur.length() > 0)
            texteErreur.setText(texteErreur.getText() + "\n________________________\n" + mLogErreur);

            //Scroll automatiquement au bas du texte
            texteSortie.selectPositionCaret(texteSortie.getLength());
            texteSortie.deselect();
            texteErreur.selectPositionCaret(texteErreur.getLength());
            texteErreur.deselect();
        }
    }
}
