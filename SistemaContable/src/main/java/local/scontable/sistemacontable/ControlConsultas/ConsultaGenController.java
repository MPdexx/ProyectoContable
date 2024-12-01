package local.scontable.sistemacontable.ControlConsultas;

import local.scontable.sistemacontable.Clases.CambioPanel;
import local.scontable.sistemacontable.PrincipalController;

public class ConsultaGenController implements CambioPanel {

    private PrincipalController panelPadre;


    public void returnMenu(){
        panelPadre.gotoMenu();
    }
    @Override
    public void setPanelPadre(PrincipalController panelPadre) {
        this.panelPadre = panelPadre;
    }
}
