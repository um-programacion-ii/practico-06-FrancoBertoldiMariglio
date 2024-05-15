import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class Farmacia {

    private HashMap<Medicamento, Integer> medicamentoMap;
    private GestionFarmaciaService gestionFarmaciaService;

    public HashMap<Medicamento, Integer> venderMedicamentos(Compra compra) {
        try {
            boolean success = gestionFarmaciaService.darMedicamentos(compra);
            if (success) {
                for (Map.Entry<Medicamento, Integer> entry : compra.getMedicamentoMap().entrySet()) {
                    Medicamento medicamento = entry.getKey();
                    Integer cantidad = entry.getValue();
                    if (this.medicamentoMap.containsKey(medicamento)) {
                        this.medicamentoMap.put(medicamento, this.medicamentoMap.get(medicamento) - cantidad);
                    }
                }
                return new HashMap<Medicamento, Integer>(compra.getMedicamentoMap());
            }
        } catch (MedioDePagoNoAceptadoException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
