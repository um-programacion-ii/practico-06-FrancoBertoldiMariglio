package dao;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.NoArgsConstructor;

import Paciente

@NoArgsConstructor
public class PacienteDAO {
    private static final Logger LOGGER = Logger.getLogger(PacienteDAO.class.getName());
    private HashMap<Integer, Paciente> tablaPacientes = new HashMap<>();

    public void save(Paciente paciente) throws NoSuchElementException {
        try {
            tablaPacientes.put(paciente.getId(), paciente);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding Paciente", e);
            throw new NoSuchElementException("Error adding Paciente", e);
        }
    }

    public Paciente getById(Integer id) throws NoSuchElementException {
        try {
            Paciente paciente = tablaPacientes.get(id);
            if (paciente == null) {
                throw new NoSuchElementException("No Paciente with ID " + id);
            }
            return paciente;
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding Paciente by ID", e);
            throw new NoSuchElementException("Error finding Paciente by ID", e);
        }
    }

    public HashMap<Integer, Paciente> getAll() throws NoSuchElementException {
        try {
            return tablaPacientes;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding all Pacientes", e);
            throw new NoSuchElementException("Error finding all Pacientes", e);
        }
    }

    public void update(Paciente paciente) throws NoSuchElementException {
        try {
            if (tablaPacientes.get(paciente.getId()) == null) {
                throw new NoSuchElementException("No Paciente with ID " + paciente.getId());
            }
            tablaPacientes.put(paciente.getId(), paciente);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating Paciente", e);
            throw new NoSuchElementException("Error updating Paciente", e);
        }
    }

    public void delete(Paciente paciente) throws NoSuchElementException {
        try {
            Paciente removedPaciente = tablaPacientes.remove(paciente.getId());
            if (removedPaciente == null) {
                throw new NoSuchElementException("No Paciente with ID " + paciente.getId());
            }
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting Paciente", e);
            throw new NoSuchElementException("Error deleting Paciente", e);
        }
    }
}