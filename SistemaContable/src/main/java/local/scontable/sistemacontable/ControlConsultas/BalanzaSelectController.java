package local.scontable.sistemacontable.ControlConsultas;

import local.scontable.sistemacontable.Clases.CambioPanel;
import local.scontable.sistemacontable.PrincipalController;

public class BalanzaSelectController implements CambioPanel {
    private PrincipalController panelPadre;

    public void cBalGeneral(){

    }

    public void cBalComprobacion(){
        try {
            if (panelPadre != null){
                panelPadre.cMantenimientos("/Consultas/BalComprobacion.fxml");
            }

        }catch (Exception ex){
            System.out.printf(String.valueOf(ex));
        }
    }

    public void returnMenu(){

    }

    @Override
    public void setPanelPadre(PrincipalController panelPadre) {
        this.panelPadre = panelPadre;
    }
}
