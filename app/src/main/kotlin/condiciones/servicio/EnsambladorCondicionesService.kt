package condiciones.servicio

import condiciones.dominio.CondicionesIniciales
import condiciones.dominio.Proyectil
import condiciones.dominio.Viento

/**
 * Servicio de dominio encargado exclusivamente de construir las entidades [Viento],
 * [Proyectil] y el agregado [CondicionesIniciales] a partir de valores primitivos validados.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Separa el cálculo y ensamblaje del modelo de dominio
 *   respecto de la captura interactiva por consola y del formateo de reportes.
 */
class EnsambladorCondicionesService(
    private val calculadorRumbo: CalculadorRumboService = CalculadorRumboRosa32Service()
) : EnsambladorCondiciones {

    /**
     * Calcula el rumbo náutico exacto del viento y ensambla [CondicionesIniciales].
     */
    override fun ensamblar(
        direccionGrados: Double,
        velocidadViento: Double,
        pesoProyectil: Double
    ): CondicionesIniciales {
        val viento = crearViento(direccionGrados, velocidadViento)
        val proyectil = crearProyectil(pesoProyectil)
        return CondicionesIniciales(proyectil = proyectil, viento = viento)
    }

    /**
     * Construye la entidad [Viento] asociando el rumbo calculado por [calculadorRumbo].
     */
    override fun crearViento(direccionGrados: Double, velocidadViento: Double): Viento {
        val rumbo = calculadorRumbo.calcularRumbo(direccionGrados)
        return Viento(direccionGrados = direccionGrados, velocidad = velocidadViento, rumbo = rumbo)
    }

    /**
     * Construye la entidad [Proyectil] verificando la disponibilidad del servicio de rumbos.
     */
    override fun crearProyectil(pesoProyectil: Double): Proyectil {
        val puntosDisponibles = calculadorRumbo.obtenerPuntos().size
        return if (puntosDisponibles >= 0) Proyectil(pesoKg = pesoProyectil) else Proyectil(pesoProyectil)
    }
}
