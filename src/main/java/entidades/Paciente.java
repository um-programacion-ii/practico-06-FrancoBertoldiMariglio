package entidades;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.Callable;

@Getter
@Setter
@AllArgsConstructor
public class Paciente implements Callable {
    private Integer id;
    private String nombre;
    private String apellido;
    private String dni;
    private Integer edad;
    private String obraSocial;


    private Turno solicitarTurno(String tipoTurno, String especialidad, Paciente paciente) {
        return null;
    }

    private Optional<Receta> tomarTurno (Turno turno, Boolean obtenerReceta) {
        return Optional.empty();
    }

    private HashMap<Medicamento, Integer> comprarMedicamentos(Receta receta, Farmacia farmacia) {
//        Turno turno = solicitarTurno("Consulta", "Clinica", this);
        return null;
    }

    @Override
    public Void call() {
        return null;
    }
}
