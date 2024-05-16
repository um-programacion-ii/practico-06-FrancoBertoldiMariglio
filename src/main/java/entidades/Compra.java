package entidades;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
public class Compra {
    private Integer id;
    private HashMap<Medicamento, Integer> medicamentoMap;
    private String medioDePago;
}
