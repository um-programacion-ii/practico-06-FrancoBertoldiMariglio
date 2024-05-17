package serviciosTest;

import dao.FarmaciaDAO;
import dao.PedidoDAO;
import entidades.*;
import excepciones.MedioDePagoNoAceptadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import servicios.GestionFarmaciaService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestionFarmaciaServiceTest {

    @Mock
    private FarmaciaDAO farmaciaDAO;

    @Mock
    private PedidoDAO pedidoDAO;

    @Mock
    private Drogueria drogueria;

    @InjectMocks
    private GestionFarmaciaService gestionFarmaciaService;

    private Farmacia farmacia;

    @BeforeEach
    void setUp() {
        List<Medicamento> medicamentoList = Arrays.asList(
                new Medicamento("Sertal", "Bayer", "Sertalina"),
                new Medicamento("Bayaspirina", "Bayer", "Aspirina"),
                new Medicamento("Geniol", "Bayer", "Paracetamol"),
                new Medicamento("Keterolac", "Bayer", "Keterolac")
        );
        HashMap<Medicamento, Integer> medicamentoIntegerMap = new HashMap<>();
        Random random = new Random();
        for (Medicamento medicamento : medicamentoList) {
            Integer cantidad = random.nextInt(10) + 1;
            medicamentoIntegerMap.put(medicamento, cantidad);
        }
        ArrayList<String> medioDePagoList = new ArrayList<>(Arrays.asList("Efectivo", "Tarjeta de crédito", "Tarjeta de débito"));
        ArrayList<String> selectedMedioDePagoList = new ArrayList<>(medioDePagoList.subList(0, 2));
        this.farmacia = new Farmacia(medicamentoIntegerMap, selectedMedioDePagoList);
        doNothing().when(farmaciaDAO).save(any(Farmacia.class));
        gestionFarmaciaService = GestionFarmaciaService.getInstance();
    }

    @Test
    void testInicializarFarmacia() {
        assertNotNull(gestionFarmaciaService.getFarmacia());
        assertEquals(2, gestionFarmaciaService.getFarmacia().getMedioDePagoList().size());
        assertTrue(gestionFarmaciaService.getFarmacia().getMedicamentoMap().size() > 0);
    }

    @Test
    void testDarMedicamentosExitoso() throws MedioDePagoNoAceptadoException {
        Medicamento medicamento1 = new Medicamento("Sertal", "Bayer", "Sertalina");
        Medicamento medicamento2 = new Medicamento("Bayaspirina", "Bayer", "Aspirina");

        HashMap<Medicamento, Integer> medicamentosSolicitados = new HashMap<>();
        medicamentosSolicitados.put(medicamento1, 1);
        medicamentosSolicitados.put(medicamento2, 1);

        Compra compra = new Compra(medicamentosSolicitados, "Efectivo");

        HashMap<Medicamento, Integer> resultado = gestionFarmaciaService.darMedicamentos(compra);

        assertEquals(2, resultado.size());
        assertEquals(1, resultado.get(medicamento1));
        assertEquals(1, resultado.get(medicamento2));
    }

    @Test
    void testDarMedicamentosMedioDePagoNoAceptado() {
        Medicamento medicamento1 = new Medicamento("Sertal", "Bayer", "Sertalina");

        HashMap<Medicamento, Integer> medicamentosSolicitados = new HashMap<>();
        medicamentosSolicitados.put(medicamento1, 1);

        Compra compra = new Compra(medicamentosSolicitados, "Cheque");

        assertThrows(MedioDePagoNoAceptadoException.class, () -> gestionFarmaciaService.darMedicamentos(compra));
    }

    @Test
    void testDarMedicamentosInsuficientes() throws MedioDePagoNoAceptadoException {
        Medicamento medicamento1 = new Medicamento("Sertal", "Bayer", "Sertalina");

        HashMap<Medicamento, Integer> medicamentosSolicitados = new HashMap<>();
        medicamentosSolicitados.put(medicamento1, 20);

        Compra compra = new Compra(medicamentosSolicitados, "Efectivo");

        HashMap<Medicamento, Integer> resultado = gestionFarmaciaService.darMedicamentos(compra);

        assertTrue(resultado.isEmpty());
        verify(pedidoDAO).save(any(Pedido.class));
    }

    @Test
    void testObtenerMedicamentosDeDrogueria() {
        Medicamento medicamento1 = new Medicamento("Sertal", "Bayer", "Sertalina");
        Medicamento medicamento2 = new Medicamento("Bayaspirina", "Bayer", "Aspirina");

        HashMap<Medicamento, Integer> medicamentosSolicitados = new HashMap<>();
        medicamentosSolicitados.put(medicamento1, 2);
        medicamentosSolicitados.put(medicamento2, 3);

        Pedido pedido = new Pedido(medicamentosSolicitados);

        when(drogueria.buscarMedicamento(anyString())).thenAnswer(invocation -> {
            String nombre = invocation.getArgument(0);
            if ("Sertal".equals(nombre)) {
                return Optional.of(medicamento1);
            } else if ("Bayaspirina".equals(nombre)) {
                return Optional.of(medicamento2);
            }
            return Optional.empty();
        });

        HashMap<Medicamento, Integer> resultado = gestionFarmaciaService.obtenerMedicamentosDeDrogueria(pedido);

        assertEquals(2, resultado.size());
        assertEquals(2, resultado.get(medicamento1));
        assertEquals(3, resultado.get(medicamento2));
    }
}
