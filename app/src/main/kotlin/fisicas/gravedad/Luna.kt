package fisicas.gravedad

/** Representa a la Luna con su gravedad superficial. */
class Luna : FuenteGravedad {
    override val gravedad: Double = 1.62
    override fun obtenerNombre(): String = "Luna"
}