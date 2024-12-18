package local.scontable.sistemacontable.Clases;

import local.scontable.sistemacontable.ControlMantenimientos.MantenimientoCatalogoCuentaInController;

public class SharedController {
    private static SharedController instance;

    private MantenimientoCatalogoCuentaInController pestañaReceptoraController;

    SharedController() {}

    public static SharedController getInstance() {
        if (instance == null) {
            instance = new SharedController();
        }
        return instance;
    }

    public MantenimientoCatalogoCuentaInController getPestañaReceptoraController() {
        return pestañaReceptoraController;
    }

    public void setPestañaReceptoraController(MantenimientoCatalogoCuentaInController controller) {
        this.pestañaReceptoraController = controller;
    }
}
