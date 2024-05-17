package entidades;

import dao.CompraDAO;
import dao.PacienteDAO;
import excepciones.MedioDePagoNoAceptadoException;
import servicios.GestionTurnoService;
import servicios.GestionFarmaciaService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.Callable;

@Getter
@Setter
@AllArgsConstructor
public class Paciente implements Callable<Void> {
    private String nombre;
    private String apellido;
    private String dni;
    private Integer edad;
    private String obraSocial;
    private String tipoTurno;
    private String especialidad;
    private boolean necesitaReceta;


    private Turno solicitarTurno(String tipoTurno, String especialidad, Paciente paciente) {
        GestionTurnoService gestionTurnoService = GestionTurnoService.getInstance();
        return gestionTurnoService.darTurno(tipoTurno, especialidad, paciente);
    }

    private Optional<Receta> tomarTurno(Turno turno, Boolean obtenerReceta) {
        GestionTurnoService gestionTurnoService = GestionTurnoService.getInstance();
        return gestionTurnoService.gestionarTurno(turno, obtenerReceta);
    }

    private HashMap<Medicamento, Integer> comprarMedicamentos(Receta receta) {
        try {
            GestionFarmaciaService gestionFarmaciaService = GestionFarmaciaService.getInstance();
            Compra compra = new Compra(receta.getMedicamentoMap(), "Efectivo");
            CompraDAO compraDAO = new CompraDAO();
            compraDAO.save(compra);
            return gestionFarmaciaService.darMedicamentos(compra);
        } catch (MedioDePagoNoAceptadoException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Void call() {
        PacienteDAO pacienteDAO = new PacienteDAO();
        pacienteDAO.save(this);
        Turno turno = solicitarTurno(tipoTurno, especialidad, this);
        Optional<Receta> optionalReceta = tomarTurno(turno, necesitaReceta);
        if (optionalReceta.isPresent()) {
            Receta receta = optionalReceta.get();
            HashMap<Medicamento, Integer> medicamentosComprados = this.comprarMedicamentos(receta);
            System.out.println("Paciente " + this.nombre + " " + this.apellido + " ha finalizado su turno. Compro los siguientes medicamentos: " + medicamentosComprados.toString());
        } else {
            System.out.println("Paciente " + this.nombre + " " + this.apellido + " ha finalizado su turno. No compro medicamentos.");
        }
        return null;
    }
}
