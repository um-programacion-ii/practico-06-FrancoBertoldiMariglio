package entidadesTest;

import dao.RecetaDAO;
import entidades.Medico;
import entidades.Paciente;
import entidades.Receta;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicoTest {

    @Mock
    private RecetaDAO recetaDAO;

    @InjectMocks
    private Medico medico;

    @Test
    void testAtenderPaciente_ConReceta() throws InterruptedException {
        Paciente paciente = new Paciente("John", "Doe", "123456789", 30, "OSDE", "consulta", "Cardiología", true);

        doNothing().when(recetaDAO).save(any(Receta.class));

        Optional<Receta> recetaOptional = medico.atenderPaciente(paciente, true);

        assertTrue(recetaOptional.isPresent());
        verify(recetaDAO, times(1)).save(any(Receta.class));
    }

    @Test
    void testAtenderPaciente_SinReceta() throws InterruptedException {
        Paciente paciente = new Paciente("John", "Doe", "123456789", 30, "OSDE", "consulta", "Cardiología", true);

        Optional<Receta> recetaOptional = medico.atenderPaciente(paciente, false);

        assertFalse(recetaOptional.isPresent());
        verify(recetaDAO, never()).save(any(Receta.class));
    }
}
