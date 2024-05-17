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
        List<Medico> medicos = crearMedicos();
        for (Medico medico : medicos) {
            String especialidad = medico.getEspecialidad();
            Queue<Medico> medicosDeEspecialidad = medicosPorEspecialidad.computeIfAbsent(especialidad, k -> new LinkedList<>());
            medicosDeEspecialidad.add(medico);
        }
    }

    private List<Medico> crearMedicos() {
        Medico medico1 = new Medico("Especialidad1", "Medico1", "Apellido1", true, "ObraSocial1");
        Medico medico2 = new Medico("Especialidad2", "Medico2", "Apellido2", false, "ObraSocial1");
        Medico medico3 = new Medico("Especialidad1", "Medico3", "Apellido3", true, "ObraSocial2");
        Medico medico4 = new Medico("Especialidad2", "Medico4", "Apellido4", false, "ObraSocial2");
        Medico medico5 = new Medico("Especialidad1", "Medico5", "Apellido5", true, "ObraSocial1");
        Medico medico6 = new Medico("Especialidad2", "Medico6", "Apellido6", false, "ObraSocial1");
        Medico medico7 = new Medico("Especialidad1", "Medico7", "Apellido7", true, "ObraSocial2");
        Medico medico8 = new Medico("Especialidad2", "Medico8", "Apellido8", false, "ObraSocial2");

        MedicoDAO medicoDAO = new MedicoDAO();
        medicoDAO.save(medico1);
        medicoDAO.save(medico2);
        medicoDAO.save(medico3);
        medicoDAO.save(medico4);
        medicoDAO.save(medico5);
        medicoDAO.save(medico6);
        medicoDAO.save(medico7);
        medicoDAO.save(medico8);

        return new ArrayList<>(Arrays.asList(medico1, medico2, medico3, medico4, medico5, medico6, medico7, medico8));
    }

    public Medico buscarMedico(String especialidad, String tipoTurno, String obraSocial) {
        List<Medico> medicos = listarMedicosPorEspecialidad(especialidad);
        if ("particular".equals(tipoTurno)) {
            medicos = filtrarMedicosParticulares(medicos);
        } else {
            medicos = filtrarMedicosPorObraSocial(medicos, obraSocial);
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