package local.scontable.sistemacontable.ControlMantenimientos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import local.scontable.sistemacontable.Clases.CambioPanel;
import local.scontable.sistemacontable.PrincipalController;
import local.scontable.sistemacontable.SistemaContableMain;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MantenimientoUsuariosController implements Initializable, CambioPanel {
    @FXML
    Button btn_return, btn_mUsuarios;
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
    public void cMantenimientoCatalogo(){
        try {
            if (panelPadre != null){
                panelPadre.cMantenimientos("/Mantenimientos/MantenimientoCatalogoCuentaIn.fxml");
            }

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void loadMantenimientos(String y) throws IOException {
        System.out.println(y);
        try {
            if (y.equals("1")){
                btn_mUsuarios.setDisable(false);
            }
            else{
                btn_mUsuarios.setDisable(true);
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
        panelPadre.gotoMenu();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.pn_mantenimientos.setVisible(true);

    }
}
