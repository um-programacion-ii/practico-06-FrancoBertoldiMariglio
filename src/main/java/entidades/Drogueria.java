package entidades;

import dao.MedicamentoDAO;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class Drogueria {

    private List<Medicamento> medicamentoList = Arrays.asList(
            new Medicamento("Sertal", "Bayer", "Sertalina"),
            new Medicamento("Bayaspirina", "Bayer", "Aspirina"),
            new Medicamento("Geniol", "Bayer", "Paracetamol"),
            new Medicamento("Keterolac", "Bayer", "Keterolac")
    );

    public Drogueria() {
        MedicamentoDAO medicamentoDAO = new MedicamentoDAO();
        for (Medicamento medicamento : this.medicamentoList) {
            medicamentoDAO.save(medicamento);
        }
    }

    public Optional<Medicamento> buscarMedicamento(String nombre) {
        return medicamentoList.stream().filter(medicamento -> medicamento.getNombre().equals(nombre)).findFirst();
    }
}
