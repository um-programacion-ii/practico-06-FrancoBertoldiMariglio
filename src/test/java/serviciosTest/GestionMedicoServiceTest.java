package servicios;

import dao.MedicoDAO;
import entidades.Medico;
import entidades.Paciente;
import entidades.Receta;
import entidades.Turno;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestionMedicoServiceTest {

    @Mock
    private MedicoDAO medicoDAO;

    @InjectMocks
    private GestionMedicoService gestionMedicoService;

    private Medico medico1;
    private Medico medico2;
    private Medico medico3;
    private Medico medico4;

    @BeforeEach
    void setUp() {
        medico1 = new Medico("Especialidad1", "Medico1", "Apellido1", true, "ObraSocial1");
        medico2 = new Medico("Especialidad2", "Medico2", "Apellido2", false, "ObraSocial1");
        medico3 = new Medico("Especialidad1", "Medico3", "Apellido3", true, "ObraSocial2");
        medico4 = new Medico("Especialidad2", "Medico4", "Apellido4", false, "ObraSocial2");

        doNothing().when(medicoDAO).save(any(Medico.class));
    }

    @Test
    void testInicializarMedicosPorEspecialidad() {
        gestionMedicoService.inicializarMedicosPorEspecialidad();

        assertEquals(2, gestionMedicoService.listarMedicosPorEspecialidad("Especialidad1").size());
        assertEquals(2, gestionMedicoService.listarMedicosPorEspecialidad("Especialidad2").size());
    }

    @Test
    void testBuscarMedicoPorEspecialidadYObraSocial() {
        gestionMedicoService.inicializarMedicosPorEspecialidad();

        Medico result = gestionMedicoService.buscarMedico("Especialidad1", "consulta", "ObraSocial1");
        assertNotNull(result);
        assertEquals("ObraSocial1", result.getObraSocial());

        result = gestionMedicoService.buscarMedico("Especialidad2", "consulta", "ObraSocial2");
        assertNotNull(result);
        assertEquals("ObraSocial2", result.getObraSocial());
    }

    @Test
    void testBuscarMedicoParticular() {
        gestionMedicoService.inicializarMedicosPorEspecialidad();

        Medico result = gestionMedicoService.buscarMedico("Especialidad1", "particular", "ObraSocial1");
        assertNotNull(result);
        assertTrue(result.isAtiendeParticular());
    }

    @Test
    void testListarMedicosPorEspecialidad() {
        gestionMedicoService.inicializarMedicosPorEspecialidad();

        List<Medico> medicos = gestionMedicoService.listarMedicosPorEspecialidad("Especialidad1");
        assertEquals(2, medicos.size());
    }

    @Test
    void testFiltrarMedicosParticulares() {
        List<Medico> medicos = Arrays.asList(medico1, medico2, medico3, medico4);
        List<Medico> result = gestionMedicoService.filtrarMedicosParticulares(medicos);

        assertEquals(2, result.size());
        assertTrue(result.contains(medico1));
        assertTrue(result.contains(medico3));
    }

    @Test
    void testFiltrarMedicosPorObraSocial() {
        List<Medico> medicos = Arrays.asList(medico1, medico2, medico3, medico4);
        List<Medico> result = gestionMedicoService.filtrarMedicosPorObraSocial(medicos, "ObraSocial1");

        assertEquals(2, result.size());
        assertTrue(result.contains(medico1));
        assertTrue(result.contains(medico2));
    }

    @Test
    void testAgregarMedico() {
        gestionMedicoService.agregarMedico(medico1);

        List<Medico> medicos = gestionMedicoService.listarMedicosPorEspecialidad("Especialidad1");
        assertEquals(1, medicos.size());
        assertTrue(medicos.contains(medico1));
    }

    @Test
    void testGestionarPaciente() throws InterruptedException {
        Paciente paciente = new Paciente("John", "Doe", "123456789", 30, "OSDE", "consulta", "Cardiología", true);
        Turno turno = new Turno(paciente, medico1, "consulta");

        when(medico1.atenderPaciente(any(Paciente.class), eq(true))).thenReturn(Optional.of(new Receta(new HashMap<>(), "Indicaciones")));

        Optional<Receta> result = gestionMedicoService.gestionarPaciente(turno, true);

        assertTrue(result.isPresent());
        verify(medico1).atenderPaciente(paciente, true);
    }
}
