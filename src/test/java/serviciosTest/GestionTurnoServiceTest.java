package servicios;

import dao.TurnoDAO;
import entidades.Medico;
import entidades.Paciente;
import entidades.Receta;
import entidades.Turno;
import entidades.Medicamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestionTurnoServiceTest {

    @Mock
    private GestionMedicoService gestionMedicoService;

    @Mock
    private TurnoDAO turnoDAO;

    @InjectMocks
    private GestionTurnoService gestionTurnoService;

    private Paciente paciente;
    private Medico medico;
    private Turno turno;
    private Receta receta;

    @BeforeEach
    void setUp() {
        paciente = new Paciente("John", "Doe", "123456789", 30, "OSDE", "consulta", "Cardiología", true);
        medico = new Medico("Cardiología", "Dr. Smith", "Brown", true, "OSDE");
        turno = new Turno(paciente, medico, "consulta");

        HashMap<Medicamento, Integer> medicamentoMap = new HashMap<>();
        Medicamento medicamento = new Medicamento("Paracetamol", "Bayern","500mg");
        medicamentoMap.put(medicamento, 2);
        receta = new Receta(medicamentoMap, "Tomar dos veces al día después de las comidas.");
    }

    @Test
    void testDarTurno() {
        when(gestionMedicoService.buscarMedico(anyString(), anyString(), anyString())).thenReturn(medico);

        Turno result = gestionTurnoService.darTurno("consulta", "Cardiología", paciente);

        assertNotNull(result);
        assertEquals(paciente, result.getPaciente());
        assertEquals(medico, result.getMedico());
        verify(turnoDAO).save(result);
    }

    @Test
    void testDarTurnoSinMedicoDisponible() {
        when(gestionMedicoService.buscarMedico(anyString(), anyString(), anyString())).thenReturn(null).thenReturn(medico);

        Thread testThread = new Thread(() -> {
            Turno result = gestionTurnoService.darTurno("consulta", "Cardiología", paciente);
            assertNotNull(result);
            assertEquals(paciente, result.getPaciente());
            assertEquals(medico, result.getMedico());
            verify(turnoDAO).save(result);
        });

        testThread.start();

        synchronized (gestionMedicoService) {
            gestionMedicoService.notifyAll();
        }

        try {
            testThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    void testGestionarTurnoConReceta() {
        when(gestionMedicoService.gestionarPaciente(any(Turno.class), eq(true))).thenReturn(Optional.of(receta));

        Optional<Receta> result = gestionTurnoService.gestionarTurno(turno, true);

        assertTrue(result.isPresent());
        assertEquals(receta, result.get());
        verify(gestionMedicoService).gestionarPaciente(turno, true);
    }

    @Test
    void testGestionarTurnoSinReceta() {
        when(gestionMedicoService.gestionarPaciente(any(Turno.class), eq(false))).thenReturn(Optional.empty());

        Optional<Receta> result = gestionTurnoService.gestionarTurno(turno, false);

        assertFalse(result.isPresent());
        verify(gestionMedicoService).gestionarPaciente(turno, false);
    }
}
