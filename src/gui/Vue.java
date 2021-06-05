package gui;

import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

public class Vue implements Observer {

    Stage stage;


    void dessinerImage(Modele modele)
    {
        if(stage.getScene() == null)
            return;

        Canvas canvas = new Canvas(modele.getImageW(), modele.getImageH());
        BorderPane rootPane = (BorderPane) stage.getScene().lookup("#rootPane");

        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image mImage = modele.renderScene();

        gc.drawImage(mImage, 0, 0, modele.getImageW(), modele.getImageH());
        if(rootPane != null)
            rootPane.setRight(canvas);
    }

    public Vue(Stage stage, Modele modele, Controleur controleur)
    {
        this.stage = stage;
        modele.addObserver(this);

        dessinerImage(modele);
        //layout de type BorderPane : plus d'info ici :https://docs.oracle.com/javafx/2/layout/builtin_layouts.htm
        BorderPane rootPane = new BorderPane();
        rootPane.setPrefSize(1200, 800);


        //Dialogue input, ou l'on copiera/modifiera directement les fichiers .pov
        BorderPane subroot = new BorderPane();
        TextArea texteEntree = new TextArea();
        texteEntree.setText(modele.getDefaultPOV());
        texteEntree.setPrefSize(600, 600);
        texteEntree.setId("texteEntree");

        FlowPane topPannelSetting = new FlowPane(); // layout de type FlowPane http://tutorials.jenkov.com/javafx/flowpane.html
        Button buttonLoad = new Button("Importer un fichier .pov");
        topPannelSetting.getChildren().add(buttonLoad);
        Button buttonSave = new Button("Enregistrer fichier.pov");
        topPannelSetting.getChildren().add(buttonSave);
        Button buttonResetChange = new Button("Annuler les changements");
        topPannelSetting.getChildren().add(buttonResetChange);


        FlowPane botPannelSetting = new FlowPane(); // layout de type FlowPane http://tutorials.jenkov.com/javafx/flowpane.html
        Button buttonGenerer = new Button("Générer la scène .pov");

        Button buttonCleanLog = new Button("Nettoyer le fichier log");
        botPannelSetting.getChildren().add(buttonCleanLog);
        buttonGenerer.setStyle("-fx-font-weight: bold");
        botPannelSetting.getChildren().add(buttonGenerer);

        subroot.setTop(topPannelSetting);
        subroot.setCenter(texteEntree);
        subroot.setBottom(botPannelSetting);

        //Cette partie du code peut être fait avec FXML

        buttonResetChange.setOnAction( event -> controleur.handleResetChange(event, buttonCleanLog) );
        buttonCleanLog.setOnAction( event -> controleur.handleCleanLog(event, buttonCleanLog) );
        buttonGenerer.setOnAction( event -> controleur.handleGenerer(event, buttonCleanLog) );
        buttonSave.setOnAction( event -> controleur.handleSave(event, buttonCleanLog) );
        buttonLoad.setOnAction( event -> controleur.handleLoad(event, buttonCleanLog) );

        //handler test, a chaque fois que le texte est changé, régénere la scene automatiquement. Feature pour tester plus rapidement, mais à ne pas mettre en production
        texteEntree.textProperty().addListener((obs,old,niu)-> controleur.handleChangeTexte(obs, old, niu));
        //~

        //Boite de dialogue ouput, où l'on peut écrire du texte de sortie, a des fin de débug ou de log
        TextArea texteSortie = new TextArea ();
        texteSortie.setPrefSize(600, 200);
        texteSortie.setEditable(false);
        texteSortie.setText("LOG :");
        texteSortie.setId("texteSortie");

        TextArea texteErreur = new TextArea ();
        texteErreur.setPrefSize(600, 200);
        texteErreur.setEditable(false);
        texteErreur.setStyle("-fx-text-inner-color: red;");
        texteErreur.setText("LOG d'erreur :");
        texteErreur.setId("texteErreur");

        BorderPane logPane = new BorderPane();
        logPane.setRight(texteErreur);
        logPane.setLeft(texteSortie);

        //
        rootPane.setLeft(subroot);//subroot texteEntree
        //rootPane.setRight(null); //<-Ici sera dessiné l'image du raytracing
        rootPane.setBottom(logPane);
        rootPane.setId("rootPane");

        Scene scene = new Scene(rootPane);
        stage.setScene(scene);
        stage.setTitle("Lancer de Rayon");
        stage.setResizable(false);

        stage.show();
        //
    }

    @Override
    public void update(Observable o, Object arg) {
        dessinerImage( (Modele) o);
    }
}
