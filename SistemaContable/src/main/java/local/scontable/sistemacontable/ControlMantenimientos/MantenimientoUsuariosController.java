package local.scontable.sistemacontable.ControlMantenimientos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import local.scontable.sistemacontable.Clases.CambioPanel;
import local.scontable.sistemacontable.PrincipalController;
import local.scontable.sistemacontable.SistemaContableMain;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MantenimientoUsuariosController implements Initializable, CambioPanel {
    @FXML
    Button btnMantenimientoUsuarios;
    private PrincipalController panelPadre;
    @FXML
    AnchorPane pn_mantenimientos;

    public void cMantenimientoUsuarios() throws Exception{

        try {
            if (panelPadre != null){
                panelPadre.cMantenimientos("/Mantenimientos/MantenimientoUsuariosIn.fxml");
            }

        }catch (Exception ex){
            System.out.printf(String.valueOf(ex));
        }

    }



    @Override
    public void setPanelPadre(PrincipalController panelPadre) {
        this.panelPadre = panelPadre;
    }

    public void returnMenu() throws IOException {


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.pn_mantenimientos.setVisible(true);
    }
}
