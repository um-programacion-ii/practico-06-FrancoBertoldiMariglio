import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Turno {

    private Integer id;
    private String tipoTurno;
    private String especialidad;
}
