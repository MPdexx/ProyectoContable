package local.scontable.sistemacontable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SistemaContableMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(SistemaContableMain.class.getResource("/Principal.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add("/Css/MenuPrincipal.css");
            stage.setTitle("Sistema Contable");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }catch (Exception ex){
            System.out.println(ex);
        }

    }

    public static void main(String[] args) {
        launch();
    }
}