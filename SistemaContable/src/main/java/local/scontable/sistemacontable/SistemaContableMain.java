package local.scontable.sistemacontable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SistemaContableMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(SistemaContableMain.class.getResource("/Login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add("Css/Login.css");
            stage.setTitle("R&P Asociados");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }catch (Exception ex){
            System.out.println(ex);
            System.out.println("hola");

        }

    }

    public static void main(String[] args) {
        launch();
    }
}