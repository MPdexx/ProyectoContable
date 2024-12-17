package local.scontable.sistemacontable.ControlConsultas;

import local.scontable.sistemacontable.Clases.CambioPanel;
import local.scontable.sistemacontable.PrincipalController;

public class ConsultaGenController implements CambioPanel {

    private PrincipalController panelPadre;

    public void cConsultaCatalogo(){
        try {
            if (panelPadre != null){
                panelPadre.cMantenimientos("/Consultas/PorCatalogo.fxml");
            }

        }catch (Exception ex){
            System.out.printf(String.valueOf(ex));
        }
    }

    public void cConsultaTrans(){
        try {
            if (panelPadre != null){
                panelPadre.cMantenimientos("/Consultas/PorTrans.fxml");
            }

        }catch (Exception ex){
            System.out.printf(String.valueOf(ex));
        }
    }

    public void cConsultaBal(){
        try {
            if (panelPadre != null){
                panelPadre.cMantenimientos("/Consultas/BalanzaSelect.fxml");
            }

        }catch (Exception ex){
            System.out.printf(String.valueOf(ex));
        }
    }

    public void returnMenu(){
        panelPadre.gotoMenu();
    }
    @Override
    public void setPanelPadre(PrincipalController panelPadre) {
        this.panelPadre = panelPadre;
    }
}
