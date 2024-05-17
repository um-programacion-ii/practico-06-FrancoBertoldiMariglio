package servicios;

import dao.MedicoDAO;
import entidades.Medico;
import entidades.Receta;
import entidades.Turno;

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

    public void inicializarMedicosPorEspecialidad() {
        MedicoDAO medicoDAO = new MedicoDAO();
        HashMap<Integer, Medico> medicosMap = medicoDAO.getAll();
        List<Medico> medicos = new ArrayList<>(medicosMap.values());
        for (Medico medico : medicos) {
            String especialidad = medico.getEspecialidad();
            Queue<Medico> medicosDeEspecialidad = medicosPorEspecialidad.computeIfAbsent(especialidad, k -> new LinkedList<>());
            medicosDeEspecialidad.add(medico);
        }
    }

    public Medico buscarMedico(String especialidad, String tipoTurno) {
        List<Medico> medicos = listarMedicosPorEspecialidad(especialidad);
        if ("particular".equals(tipoTurno)) {
            medicos = filtrarMedicosParticulares(medicos);
        } else {
            medicos = filtrarMedicosPorObraSocial(medicos, tipoTurno);
        }
        return medicos.isEmpty() ? null : medicos.getFirst();
    }

    public List<Medico> listarMedicosPorEspecialidad(String especialidad) {
        Queue<Medico> medicos = medicosPorEspecialidad.get(especialidad);
        if (medicos != null && !medicos.isEmpty()) {
            return new ArrayList<>(medicos);
        }
        return Collections.emptyList();
    }

    public List<Medico> filtrarMedicosParticulares(List<Medico> medicos) {
        List<Medico> medicosParticulares = new ArrayList<>();
        for (Medico medico : medicos) {
            if (medico.isAtiendeParticular()) {
                medicosParticulares.add(medico);
            }
        }
        return medicosParticulares;
    }

    public List<Medico> filtrarMedicosPorObraSocial(List<Medico> medicos, String obraSocial) {
        List<Medico> medicosConObraSocial = new ArrayList<>();
        for (Medico medico : medicos) {
            if (medico.getObraSocial().equals(obraSocial)) {
                medicosConObraSocial.add(medico);
            }
        }
        return medicosConObraSocial;
    }

    public void agregarMedico(Medico medico) {
        String especialidad = medico.getEspecialidad();
        Queue<Medico> medicos = medicosPorEspecialidad.computeIfAbsent(especialidad, k -> new LinkedList<>());
        medicos.add(medico);
        notifyAll();
    }

    public synchronized Optional<Receta> gestionarPaciente(Turno turno, Boolean obtenerReceta) {
        try {
            Optional<Receta> receta = turno.getMedico().atenderPaciente(turno.getPaciente(), obtenerReceta);
            agregarMedico(turno.getMedico());
            return receta;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("La espera fue interrumpida");
        }
        return Optional.empty();
    }
}