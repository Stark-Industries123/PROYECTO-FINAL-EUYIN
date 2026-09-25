package fisicas.gravedad

/** Representa al planeta Tierra con su gravedad estándar. */
class Tierra : FuenteGravedad {
    override val gravedad: Double = 9.81
    override fun obtenerNombre(): String = "Tierra"
}