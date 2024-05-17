package entidades;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
public class Farmacia {
    private HashMap<Medicamento, Integer> medicamentoMap;
    private ArrayList<String> medioDePagoList;
}
