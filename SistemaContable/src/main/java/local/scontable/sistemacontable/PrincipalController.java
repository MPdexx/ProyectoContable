package local.scontable.sistemacontable;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
        try {
            Pane rgpn = (Pane) CargarFxml("/MantenimientoUsuarios.fxml");
            rgpn.getStylesheets().add(getClass().getResource("/MenuPrincipal.css").toExternalForm());
            CargarPHijo(rgpn);
        }catch (Exception ex){
            System.out.printf(String.valueOf(ex));
        }
    }

    private void CargarPHijo(Node PanelH){
        anpane_padre.getChildren().clear();
        anpane_padre.getChildren().add(PanelH);
    }

    private Node CargarFxml(String fxml){
        try{
           return FXMLLoader.load(SistemaContableMain.class.getResource(fxml));

        } catch (Exception e) {

            return null;
        }
    }

}

