import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
public class Compra {

    private HashMap<Medicamento, Integer> medicamentoMap;
    private String medioDePago;
}
