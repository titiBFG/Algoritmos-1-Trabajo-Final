// NA.java
package utils.validation;

/**
 * Representa un valor faltante (“Not Available”).
 * Se usa NA.INSTANCE en lugar de null o "" para indicar que la celda no tiene dato.
 */
public final class NA {
    // Única instancia de NA 
    public static final NA INSTANCE = new NA();

    // Constructor privado para evitar crear más instancias
    private NA() { }

    @Override
    public String toString() {
        return "NA";
    }

    @Override
    public boolean equals(Object obj) {
        // Todos los NA son la misma instancia
        return this == obj;
    }

    @Override
    public int hashCode() {
        // Cualquier valor constante está bien; todos los NA tienen el mismo hash
        return 0;
    }
}
