package entidades;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Turno {
    private Integer id;
    private Paciente paciente;
    private Medico medico;
    private String tipoTurno;
}
