package local.scontable.sistemacontable;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrincipalController implements Initializable {
    @FXML
    Button btn_mantenimientos;
    @FXML
    AnchorPane anpane_padre,pn_MenuHome;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void cMantenimientos() throws IOException {
        FXMLLoader loader = new FXMLLoader(SistemaContableMain.class.getResource("/MantenimientoUsuarios.fxml"));
        Pane rgpn = (Pane) loader.load();
        rgpn.getStylesheets().add(getClass().getResource("/MenuPrincipal.css").toExternalForm());
        try {

            anpane_padre.getChildren().clear();
            anpane_padre.getChildren().add(rgpn);


        }catch (Exception ex){
            System.out.printf(String.valueOf(ex));
        }
    }

}

