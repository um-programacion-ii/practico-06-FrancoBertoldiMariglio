package entidades;

import excepciones.MedioDePagoNoAceptadoException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import servicios.GestionFarmaciaService;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
public class Farmacia {

    private HashMap<Medicamento, Integer> medicamentoMap;
    private GestionFarmaciaService gestionFarmaciaService;
    private ArrayList<String> mediodDePagoList;

    public HashMap<Medicamento, Integer> venderMedicamentos(Compra compra) {
        try {
            return gestionFarmaciaService.darMedicamentos(compra, this.medicamentoMap, this.mediodDePagoList);
        } catch (MedioDePagoNoAceptadoException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
