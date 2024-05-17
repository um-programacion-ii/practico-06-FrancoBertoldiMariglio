package excepciones;

public class MedioDePagoNoAceptadoException extends Exception{
    public MedioDePagoNoAceptadoException() {
        super("Medio de pago no aceptado");
    }
}
