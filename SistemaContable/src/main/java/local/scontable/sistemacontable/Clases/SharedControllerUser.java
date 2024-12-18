package local.scontable.sistemacontable.Clases;



import local.scontable.sistemacontable.ControlMantenimientos.MantenimientoUsuariosInController;

public class SharedControllerUser {
    private static SharedControllerUser instance;

    private MantenimientoUsuariosInController pestañaReceptoraController;

    private SharedControllerUser() {}

    public static SharedControllerUser getInstance() {
        if (instance == null) {
            instance = new SharedControllerUser();
        }
        return instance;
    }

    public MantenimientoUsuariosInController getPestañaReceptoraController() {
        return pestañaReceptoraController;
    }

    public void setPestañaReceptoraController(MantenimientoUsuariosInController controller) {
        this.pestañaReceptoraController = controller;
    }
}
