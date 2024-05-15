import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class Medico {
    private Integer id;
    private String especialidad;
    private String nombre;
    private String apellido;
    private boolean atiendeParticular;
    private String obraSocial;

    public Optional<Receta> atenderPaciente(Paciente paciente) {
        return Optional.empty();
    }

    public Receta generarReceta(List<Medicamento> medicamentos) {
        return null;
    }
}
