package local.scontable.sistemacontable.ControlConsultas;

import local.scontable.sistemacontable.Clases.CambioPanel;
import local.scontable.sistemacontable.PrincipalController;

import java.io.IOException;

public class BalanzaSelectController implements CambioPanel {
    private PrincipalController panelPadre;

    public void cBalGeneral(){
        try {
            if (panelPadre != null){
                panelPadre.cMantenimientos("/Consultas/BalGeneral.fxml");
            }

        }catch (Exception ex){
            System.out.printf(String.valueOf(ex));
        }
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

    public void returnMenu() throws IOException {
        if (panelPadre != null) {
            panelPadre.cMantenimientos("/Consultas/ConsultaGen.fxml");
        }
    }

    @Override
    public void setPanelPadre(PrincipalController panelPadre) {
        this.panelPadre = panelPadre;
    }
}
