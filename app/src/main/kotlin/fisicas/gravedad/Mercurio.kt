package fisicas.gravedad

/** Representa al planeta Mercurio con su gravedad superficial. */
class Mercurio : FuenteGravedad {
    override val gravedad: Double = 3.7
    override fun obtenerNombre(): String = "Mercurio"
}