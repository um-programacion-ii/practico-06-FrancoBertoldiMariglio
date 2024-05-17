package servicios;

import dao.MedicamentoDAO;
import dao.PedidoDAO;
import entidades.*;
import excepciones.MedioDePagoNoAceptadoException;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class GestionFarmaciaService {

    private static GestionFarmaciaService instance;

    @Getter
    private final Farmacia farmacia;

    @Getter
    @Setter
    private Drogueria drogueria;

    private GestionFarmaciaService() {
        MedicamentoDAO medicamentoDAO = new MedicamentoDAO();
        HashMap<Integer, Medicamento> medicamentosMap = medicamentoDAO.getAll();
        HashMap<Medicamento, Integer> medicamentoIntegerMap = new HashMap<>();
        Random random = new Random();
        for (Medicamento medicamento : medicamentosMap.values()) {
            Integer cantidad = random.nextInt(10) + 1;
            medicamentoIntegerMap.put(medicamento, cantidad);
        }
        ArrayList<String> medioDePagoList = new ArrayList<>(Arrays.asList("Efectivo", "Tarjeta de crédito", "Tarjeta de débito"));
        Collections.shuffle(medioDePagoList);
        ArrayList<String> selectedMedioDePagoList = (ArrayList<String>) medioDePagoList.subList(0, 2);
        this.farmacia = new Farmacia(medicamentoIntegerMap, selectedMedioDePagoList);
    }

    public static GestionFarmaciaService getInstance() {
        if (instance == null) {
            instance = new GestionFarmaciaService();
        }
        return instance;
    }

    public HashMap<Medicamento, Integer> darMedicamentos(Compra compra) throws MedioDePagoNoAceptadoException {
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
