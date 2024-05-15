import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
public class Pedido {

    private HashMap<Medicamento, Integer> medicamentoMap;
}
