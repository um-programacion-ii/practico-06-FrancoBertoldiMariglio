import dao.MedicoDAO;
import entidades.Medico;
import entidades.Paciente;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Clinica {

    public static void main(String[] args) {

        Paciente paciente1 = new Paciente("Juan", "Perez", "12345678", 30, "ObraSocial1", "TipoTurno1", "Especialidad1", true);
        Paciente paciente2 = new Paciente("Maria", "Gomez", "87654321", 35, "ObraSocial1", "TipoTurno2", "Especialidad2", false);
        Paciente paciente3 = new Paciente("Pedro", "Gonzalez", "23456789", 40, "ObraSocial2", "TipoTurno3", "Especialidad1", true);
        Paciente paciente4 = new Paciente("Ana", "Rodriguez", "98765432", 45, "ObraSocial2", "TipoTurno4", "Especialidad2", false);
        Paciente paciente5 = new Paciente("Carlos", "Lopez", "34567890", 50, "ObraSocial1", "TipoTurno1", "Especialidad1", true);
        Paciente paciente6 = new Paciente("Lucia", "Martinez", "09876543", 55, "ObraSocial2", "TipoTurno2", "Especialidad2", false);
        Paciente paciente7 = new Paciente("Miguel", "Garcia", "45678901", 60, "ObraSocial1", "TipoTurno3", "Especialidad1", true);
        Paciente paciente8 = new Paciente("Sofia", "Fernandez", "10987654", 65, "ObraSocial2", "TipoTurno4", "Especialidad2", false);
        Paciente paciente9 = new Paciente("Antonio", "Torres", "56789012", 70, "ObraSocial1", "TipoTurno1", "Especialidad1", true);
        Paciente paciente10 = new Paciente("Isabel", "Sanchez", "21098765", 75, "ObraSocial2", "TipoTurno2", "Especialidad2", false);
        Paciente paciente11 = new Paciente("Jose", "Castro", "67890123", 80, "ObraSocial1", "TipoTurno3", "Especialidad1", true);
        Paciente paciente12 = new Paciente("Teresa", "Gutierrez", "32109876", 85, "ObraSocial2", "TipoTurno4", "Especialidad2", false);
        Paciente paciente13 = new Paciente("Francisco", "Romero", "78901234", 90, "ObraSocial1", "TipoTurno1", "Especialidad1", true);
        Paciente paciente14 = new Paciente("Carmen", "Vargas", "43210987", 95, "ObraSocial2", "TipoTurno2", "Especialidad2", false);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<Void> future1 = executor.submit(paciente1);
        Future<Void> future2 = executor.submit(paciente2);
        Future<Void> future3 = executor.submit(paciente3);
        Future<Void> future4 = executor.submit(paciente4);
        Future<Void> future5 = executor.submit(paciente5);
        Future<Void> future6 = executor.submit(paciente6);
        Future<Void> future7 = executor.submit(paciente7);
        Future<Void> future8 = executor.submit(paciente8);
        Future<Void> future9 = executor.submit(paciente9);
        Future<Void> future10 = executor.submit(paciente10);
        Future<Void> future11 = executor.submit(paciente11);
        Future<Void> future12 = executor.submit(paciente12);
        Future<Void> future13 = executor.submit(paciente13);
        Future<Void> future14 = executor.submit(paciente14);

        executor.shutdown();
    }
}