package servicios;

import dao.TurnoDAO;
import entidades.Medico;
import entidades.Paciente;
import entidades.Receta;
import entidades.Turno;

import java.util.Optional;

public class GestionTurnoService {

    private final GestionMedicoService gestionMedicoService = GestionMedicoService.getInstance();
    private static GestionTurnoService instance;
    private static Integer id = 0;

    private GestionTurnoService() { }

    public static GestionTurnoService getInstance() {
        if (instance == null) {
            instance = new GestionTurnoService();
        }
        return instance;
    }

    public Turno darTurno(String tipoTurno, String especialidad, Paciente paciente) {
        synchronized (this.gestionMedicoService){
            gestionMedicoService.inicializarMedicosPorEspecialidad();
            Medico medico = gestionMedicoService.buscarMedico(especialidad, tipoTurno);
            while (medico == null) {
                try {
                    wait();
                    medico = gestionMedicoService.buscarMedico(especialidad, tipoTurno);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("La espera fue interrumpida");
                }
            }
            Turno turno = new Turno(paciente, medico, tipoTurno);
            TurnoDAO turnoDAO = new TurnoDAO();
            turnoDAO.save(turno);
            return turno;
        }
    }

    public synchronized Optional<Receta> gestionarTurno(Turno turno, Boolean obtenerReceta) {
        return gestionMedicoService.gestionarPaciente(turno, obtenerReceta);
    }
}
