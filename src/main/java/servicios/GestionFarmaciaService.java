package servicios;

import entidades.Compra;
import entidades.Drogueria;
import entidades.Medicamento;
import entidades.Pedido;
import excepciones.MedioDePagoNoAceptadoException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    public void setDrogueria(Drogueria drogueria) {
        this.drogueria = drogueria;
    }

    public Drogueria getDrogueria() {
        return drogueria;
    }

    public HashMap<Medicamento, Integer> darMedicamentos(Compra compra, HashMap<Medicamento, Integer> medicamentoMap, ArrayList<String> medioDePagoList) throws MedioDePagoNoAceptadoException {
        if (!medioDePagoList.contains(compra.getMedioDePago())) {
            throw new MedioDePagoNoAceptadoException();
        }

        HashMap<Medicamento, Integer> medicamentosInsuficientes = new HashMap<>();
        HashMap<Medicamento, Integer> medicamentosParaDevolver = new HashMap<>();

        for (Map.Entry<Medicamento, Integer> entry : compra.getMedicamentoMap().entrySet()) {
            Medicamento medicamento = entry.getKey();
            Integer cantidadRequerida = entry.getValue();

            if (!medicamentoMap.containsKey(medicamento) || medicamentoMap.get(medicamento) < cantidadRequerida) {
                medicamentosInsuficientes.put(medicamento, cantidadRequerida);
            } else {
                medicamentosParaDevolver.put(medicamento, cantidadRequerida);
                medicamentoMap.put(medicamento, medicamentoMap.get(medicamento) - cantidadRequerida);
            }
        }
        if (!medicamentosInsuficientes.isEmpty()) {
            Pedido pedido = new Pedido(medicamentosInsuficientes);
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
