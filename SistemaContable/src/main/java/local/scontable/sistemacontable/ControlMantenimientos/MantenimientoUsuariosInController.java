package local.scontable.sistemacontable.ControlMantenimientos;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MantenimientoUsuariosInController {
    @FXML
    TableView tview_users;
    @FXML
    TextField txtf_user, txtf_pass, txtf_NameUser, txtf_LastName,txtf_email;
    @FXML
    Button btn_save, btn_edit, btn_delete, btn_search;
    
}
