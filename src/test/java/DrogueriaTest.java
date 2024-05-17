import entidades.Drogueria;
import entidades.Medicamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class DrogueriaTest {
    private Drogueria drogueria;
    private List<Medicamento> medicamentoList;

    @BeforeEach
    public void setUp() {
        medicamentoList = Arrays.asList(
                new Medicamento("Sertal", "Bayer", "Sertalina"),
                new Medicamento("Bayaspirina", "Bayer", "Aspirina"),
                new Medicamento("Geniol", "Bayer", "Paracetamol"),
                new Medicamento("Keterolac", "Bayer", "Keterolac")
        );

        drogueria = Mockito.spy(new Drogueria());
        Mockito.doReturn(medicamentoList).when(drogueria).getMedicamentoList();
    }

    @Test
    public void testBuscarMedicamento() {
        Medicamento expectedMedicamento = medicamentoList.get(0);
        when(drogueria.buscarMedicamento("Sertal")).thenReturn(java.util.Optional.of(expectedMedicamento));

        assertEquals(java.util.Optional.of(expectedMedicamento), drogueria.buscarMedicamento("Sertal"));
    }
}