package entidades;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
public class Receta {
    private Integer id;
    private HashMap<Medicamento, Integer> medicamentoMap;
    private String indicaciones;
}
