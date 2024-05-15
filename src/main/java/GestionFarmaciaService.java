import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
public class GestionFarmaciaService {

    private static GestionFarmaciaService instance;
    private Drogueria drogueria;

    private GestionFarmaciaService() {
    }

    public static GestionFarmaciaService getInstance() {
        if (instance == null) {
            instance = new GestionFarmaciaService();
        }
        return instance;
    }

    public boolean darMedicamentos(Compra compra) throws MedioDePagoNoAceptadoException {
        return false;
    }

    public HashMap<Medicamento, Integer> obtenerMedicamentosDeDrogueria(Pedido pedido) {
        return null;
    }
}
