package servicios;

import entidades.Medico;

import java.util.*;

public class GestionMedicoService {

    private static GestionMedicoService instance;
    private final Map<String, Queue<Medico>> medicosPorEspecialidad = new HashMap<>();

    private GestionMedicoService() {}

    public static GestionMedicoService getInstance() {
        if (instance == null) {
            instance = new GestionMedicoService();
        }
        return instance;
    }

    public synchronized List<Medico> buscarMedicos(String especialidad, String tipoTurno) {
        if ("particular".equals(tipoTurno)) {
            return buscarMedicosParticulares(especialidad);
        } else {
            return buscarMedicosPorObraSocial(especialidad, tipoTurno);
        }
    }

    private List<Medico> buscarMedicosParticulares(String especialidad) {
        List<Medico> medicosParticulares = listarMedicosParticulares();
        medicosParticulares.removeIf(medico -> !medico.getEspecialidad().equals(especialidad));
        return medicosParticulares;
    }

    private List<Medico> buscarMedicosPorObraSocial(String especialidad, String obraSocial) {
        List<Medico> medicosPorObraSocial = listarMedicosPorEspecialidadYObraSocial(especialidad, obraSocial);
        return medicosPorObraSocial;
    }

    public synchronized List<Medico> listarMedicosPorEspecialidadYObraSocial(String especialidad, String obraSocial) {
        Queue<Medico> medicos = medicosPorEspecialidad.get(especialidad);
        if (medicos != null && !medicos.isEmpty()) {
            List<Medico> medicosConObraSocial = new ArrayList<>();
            for (Medico medico : medicos) {
                if (medico.getObraSocial().equals(obraSocial)) {
                    medicosConObraSocial.add(medico);
                }
            }
            return medicosConObraSocial;
        }
        return Collections.emptyList();
    }

    public synchronized List<Medico> listarMedicosParticulares() {
        List<Medico> medicosParticulares = new ArrayList<>();
        for (Queue<Medico> medicos : medicosPorEspecialidad.values()) {
            for (Medico medico : medicos) {
                if (medico.isAtiendeParticular()) {
                    medicosParticulares.add(medico);
                }
            }
        }
        return medicosParticulares;
    }

    public synchronized void agregarMedico(Medico medico) {
        String especialidad = medico.getEspecialidad();
        Queue<Medico> medicos = medicosPorEspecialidad.computeIfAbsent(especialidad, k -> new LinkedList<>());
        medicos.add(medico);
    }
}