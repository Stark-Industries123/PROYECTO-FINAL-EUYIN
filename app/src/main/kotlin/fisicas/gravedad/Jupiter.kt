package fisicas.gravedad

/** Representa al planeta Júpiter con su gravedad superficial. */
class Jupiter : FuenteGravedad {
    override val gravedad: Double = 24.79
    override fun obtenerNombre(): String = "Júpiter"
}