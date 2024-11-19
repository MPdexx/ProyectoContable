package local.scontable.sistemacontable;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import local.scontable.sistemacontable.Clases.CambioPanel;
import local.scontable.sistemacontable.ControlMantenimientos.MantenimientoUsuariosController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrincipalController implements Initializable, CambioPanel {

    private PrincipalController panelPadre;
    public PrincipalController panelMenu;



    @FXML
    Button btn_mantenimientos;
    @FXML
    AnchorPane anpane_padre, pn_menu;
    @FXML
    Label lbl_usuario;
    //@FXML
    //ChoiceBox<String> cbox_optUser;
    private String[] usrOptions = {"Cerrar sesión"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //cbox_optUser.getItems().addAll(usrOptions);
        /*StringBinding binding = Bindings.createStringBinding(() ->
            String.valueOf(cbox_optUser.getValue() != null),
            cbox_optUser.valueProperty()
        );*/
    }

    public void changePanel_manteniminetos(){
        try {
            cMantenimientos("/Mantenimientos/MantenimientoUsuarios.fxml");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void cMantenimientos(String fxml) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
            Pane rgpn = fxmlLoader.load();
            Object controller = fxmlLoader.getController();
            if (controller instanceof CambioPanel) {
                ((CambioPanel) controller).setPanelPadre(this);
            }
            rgpn.getStylesheets().add(getClass().getResource("/Css/MenuPrincipal.css").toExternalForm());
            rgpn.getStylesheets().add(getClass().getResource("/Css/Mantenimientos.css").toExternalForm());
            CargarPHijo(rgpn);
        }catch (Exception ex){
            System.out.printf(String.valueOf(ex));
        }
    }

    private void CargarPHijo(Node PanelH){

        anpane_padre.getChildren().clear();
        anpane_padre.getChildren().add(PanelH);
    }
    @Override
    public void setPanelPadre(PrincipalController panelPadre) {
        this.panelPadre = panelPadre;
    }

    public void loadMantenimientos(String y) throws IOException {
        System.out.println(y);
        try {
            if (y.equals("1")){
                btn_mantenimientos.setVisible(true);
            }
            else{
                btn_mantenimientos.setVisible(false);
            }
        }catch (Exception ex){
            System.out.printf(String.valueOf(ex));
        }

    }

    public void displayUserName(String user){

        lbl_usuario.setText(user);
    }

    /*public void UserOptions(ChoiceBox<String> cbox_optUser) {
        String option = cbox_optUser.getValue();
        if (option.equals("Cerrar sesión")){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmacion");
            alert.setContentText("Está seguro de cerrar sesión?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(SistemaContableMain.class.getResource("/Login.fxml"));
                        Scene scene = new Scene(fxmlLoader.load());
                        scene.getStylesheets().add("Css/Login.css");
                        Stage stage = new Stage();
                        stage.setTitle("R&P Asociados");
                        stage.setResizable(false);
                        stage.setScene(scene);
                        stage.show();

                        Stage st = (Stage) cbox_optUser.getScene().getWindow();
                        st.close();
                    }catch (Exception ex){
                        System.out.printf(String.valueOf(ex));
                    }

                } else {

                }
            });
        }*/
}



