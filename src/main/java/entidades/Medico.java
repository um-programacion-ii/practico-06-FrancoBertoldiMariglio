package entidades;

import dao.MedicamentoDAO;
import dao.RecetaDAO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
public class Medico {
    private String especialidad;
    private String nombre;
    private String apellido;
    private boolean atiendeParticular;
    private String obraSocial;

    public Optional<Receta> atenderPaciente(Paciente paciente, Boolean obtenerReceta) throws InterruptedException {
        System.out.println("El medico " + this.nombre + " " + this.apellido + " esta atendiendo al paciente " + paciente.getNombre() + " " + paciente.getApellido());
        Thread.sleep(2000);
        if (obtenerReceta) {
            HashMap<Medicamento, Integer> medicamentoMap = getMedicamentoIntegerHashMap();
            String indicaciones = "Tomar los medicamentos cada " + (new Random().nextInt(3) + 1) + " horas";
            Receta receta = new Receta(medicamentoMap, indicaciones);
            RecetaDAO recetaDAO = new RecetaDAO();
            recetaDAO.save(receta);
            return Optional.of(receta);
        }
        return Optional.empty();
    }

    private HashMap<Medicamento, Integer> getMedicamentoIntegerHashMap() {
        List<Medicamento> medicamentoList = Arrays.asList(
            new Medicamento("Sertal", "Bayer", "Sertalina"),
            new Medicamento("Bayaspirina", "Bayer", "Aspirina"),
            new Medicamento("Geniol", "Bayer", "Paracetamol"),
            new Medicamento("Keterolac", "Bayer", "Keterolac")
        );
        HashMap<Medicamento, Integer> medicamentoMap = new HashMap<>();
        Random random = new Random();
        int numMedicamentos = random.nextInt(medicamentoList.size()) + 1;

        for (int i = 0; i < numMedicamentos; i++) {
            Medicamento medicamento = medicamentoList.get(random.nextInt(medicamentoList.size()));
            Integer cantidad = random.nextInt(3) + 1;
            medicamentoMap.put(medicamento, cantidad);
        }
        return medicamentoMap;
    }

    public Receta generarReceta(List<Medicamento> medicamentos) {
        return null;
    }
}
