package servicios;

import entidades.Medico;
import entidades.Paciente;
import entidades.Turno;

import java.util.List;

public class GestionTurnoService {

    private GestionMedicoService gestionMedicoService = GestionMedicoService.getInstance();
    private static GestionTurnoService instance;
    private static Integer id = 0;

    private GestionTurnoService() { }

    public static GestionTurnoService getInstance() {
        if (instance == null) {
            instance = new GestionTurnoService();
        }
        return instance;
    }

    public synchronized Turno gestionarTurno(String tipoTurno, String especialidad, Paciente paciente) {
        List<Medico> medicos = gestionMedicoService.buscarMedicos(especialidad, tipoTurno);
        Medico medico = null;
        if (!medicos.isEmpty()) {
            medico = medicos.get(0);
        }
        while (medico == null) {
            try {
                wait();
                medicos = gestionMedicoService.buscarMedicos(especialidad, tipoTurno);
                if (!medicos.isEmpty()) {
                    medico = medicos.get(0);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("La espera fue interrumpida");
            }
        }
        id++;
        return new Turno(id, paciente, medico, tipoTurno);
    }
}