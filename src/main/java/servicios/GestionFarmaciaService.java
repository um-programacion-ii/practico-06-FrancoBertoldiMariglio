package servicios;

import dao.FarmaciaDAO;
import dao.MedicamentoDAO;
import dao.PedidoDAO;
import entidades.*;
import excepciones.MedioDePagoNoAceptadoException;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GestionFarmaciaService {

    private static GestionFarmaciaService instance;

    @Getter
    private final Farmacia farmacia;

    @Getter
    @Setter
    private Drogueria drogueria = new Drogueria();

    private GestionFarmaciaService() {
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
        ArrayList<String> selectedMedioDePagoList = IntStream.range(0, 2)
                .mapToObj(medioDePagoList::get)
                .collect(Collectors.toCollection(ArrayList::new));
        this.farmacia = new Farmacia(medicamentoIntegerMap, selectedMedioDePagoList);
        FarmaciaDAO farmaciaDAO = new FarmaciaDAO();
        farmaciaDAO.save(this.farmacia);
    }

    public static GestionFarmaciaService getInstance() {
        if (instance == null) {
            instance = new GestionFarmaciaService();
        }
        return instance;
    }

    public HashMap<Medicamento, Integer> darMedicamentos(Compra compra) throws MedioDePagoNoAceptadoException {
        synchronized (this) {
            if (!this.farmacia.getMedioDePagoList().contains(compra.getMedioDePago())) {
                throw new MedioDePagoNoAceptadoException();
            }

            HashMap<Medicamento, Integer> medicamentoDisponibles = farmacia.getMedicamentoMap();
            HashMap<Medicamento, Integer> medicamentosInsuficientes = new HashMap<>();
            HashMap<Medicamento, Integer> medicamentosParaDevolver = new HashMap<>();

            for (Map.Entry<Medicamento, Integer> entry : compra.getMedicamentoMap().entrySet()) {
                Medicamento medicamento = entry.getKey();
                Integer cantidadRequerida = entry.getValue();

                if (!medicamentoDisponibles.containsKey(medicamento) || medicamentoDisponibles.get(medicamento) < cantidadRequerida) {
                    medicamentosInsuficientes.put(medicamento, cantidadRequerida);
                } else {
                    medicamentosParaDevolver.put(medicamento, cantidadRequerida);
                    medicamentoDisponibles.put(medicamento, medicamentoDisponibles.get(medicamento) - cantidadRequerida);
                }
            }
            if (!medicamentosInsuficientes.isEmpty()) {
                Pedido pedido = new Pedido(medicamentosInsuficientes);
                PedidoDAO pedidoDAO = new PedidoDAO();
                pedidoDAO.save(pedido);
                HashMap<Medicamento, Integer> medicamentosDrogueria = obtenerMedicamentosDeDrogueria(pedido);
                medicamentosParaDevolver.putAll(medicamentosDrogueria);
            }
            return medicamentosParaDevolver;
        }
    }


    public HashMap<Medicamento, Integer> obtenerMedicamentosDeDrogueria(Pedido pedido) {
        HashMap<Medicamento, Integer> medicamentosObtenidos = new HashMap<>();

        for (Map.Entry<Medicamento, Integer> entry : pedido.getMedicamentoMap().entrySet()) {
            Medicamento medicamentoPedido = entry.getKey();
            Integer cantidadPedido = entry.getValue();

            Optional<Medicamento> medicamentoEncontrado = drogueria.buscarMedicamento(medicamentoPedido.getNombre());

            medicamentoEncontrado.ifPresent(medicamento -> medicamentosObtenidos.put(medicamento, cantidadPedido));
        }
        return medicamentosObtenidos;
    }
}
