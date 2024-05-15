import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class Drogueria {

    private List<Medicamento> medicamentoList = Arrays.asList(
            new Medicamento("Sertal", "Bayer", "Sertalina"),
            new Medicamento("Bayaspirina", "Bayer", "Aspirina"),
            new Medicamento("Geniol", "Bayer", "Paracetamol"),
            new Medicamento("Keterolac", "Bayer", "Keterolac")
    );


    public Optional<Medicamento> buscarMedicamento(String nombre) {
        return medicamentoList.stream().filter(medicamento -> medicamento.getNombre().equals(nombre)).findFirst();
    }
}
