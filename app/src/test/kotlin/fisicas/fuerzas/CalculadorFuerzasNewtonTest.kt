package fisicas.fuerzas

import fisicas.config.EnvironmentConfig
import fisicas.config.ProjectileConfig
import fisicas.config.WindConfig
import fisicas.estado.ProjectileState
import fisicas.gravedad.FuenteGravedad
import fisicas.vector.Vector2D
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CalculadorFuerzasNewtonTest {

    private val delta = 1e-6

    private class GravedadPrueba(override val gravedad: Double = 9.8) : FuenteGravedad {
        override fun obtenerNombre(): String = "Prueba"
    }

    @Test
    fun `sin arrastre y sin viento solo actua la aceleracion de gravedad`() {
        val calculador = CalculadorFuerzasNewton()
        val estado = ProjectileState(Vector2D(0.0, 10.0), Vector2D(15.0, 5.0))
        val proyectil = ProjectileConfig(masa = 2.0, coeficienteArrastre = 0.0)
        val entorno = EnvironmentConfig(fuenteGravedad = GravedadPrueba(9.8))

        val a = calculador.calcularAceleracion(estado, proyectil, entorno)

        assertEquals(0.0, a.x, delta)
        assertEquals(-9.8, a.y, delta)
    }

    @Test
    fun `resistencia del aire opuesta al movimiento desacelera en ambos ejes`() {
        val calculador = CalculadorFuerzasNewton()
        // vx = 10, vy = 20, m = 2, k = 0.5
        // Fdrag_x = -0.5 * 10 = -5.0 => ax = -5.0 / 2 = -2.5
        // Fdrag_y = -0.5 * 20 = -10.0 => ay = -9.8 + (-10.0 / 2) = -14.8
        val estado = ProjectileState(Vector2D(0.0, 10.0), Vector2D(10.0, 20.0))
        val proyectil = ProjectileConfig(masa = 2.0, coeficienteArrastre = 0.5)
        val entorno = EnvironmentConfig(fuenteGravedad = GravedadPrueba(9.8))

        val a = calculador.calcularAceleracion(estado, proyectil, entorno)

        assertEquals(-2.5, a.x, delta)
        assertEquals(-14.8, a.y, delta)
    }

    @Test
    fun `fuerza del viento hacia el Este (90 grados) suma aceleracion en X`() {
        val calculador = CalculadorFuerzasNewton()
        // Viento Este 90° con magnitud 4.0 => Fviento = (4.0, 0.0)
        // ax = (Fdrag_x + 4.0) / 2.0 = (-5.0 + 4.0) / 2 = -0.5
        // ay = -9.8 + (-10.0 + 0.0) / 2 = -14.8
        val estado = ProjectileState(Vector2D(0.0, 10.0), Vector2D(10.0, 20.0))
        val proyectil = ProjectileConfig(masa = 2.0, coeficienteArrastre = 0.5)
        val entorno = EnvironmentConfig(
            fuenteGravedad = GravedadPrueba(9.8),
            viento = WindConfig(magnitud = 4.0, anguloGrados = 90.0)
        )

        val a = calculador.calcularAceleracion(estado, proyectil, entorno)

        assertEquals(-0.5, a.x, delta)
        assertEquals(-14.8, a.y, delta)
    }

    @Test
    fun `mayor masa reduce el impacto proporcional del viento y arrastre (Inercia)`() {
        val calculador = CalculadorFuerzasNewton()
        val estado = ProjectileState(Vector2D(0.0, 10.0), Vector2D(10.0, 0.0))
        val proyectilLiviano = ProjectileConfig(masa = 1.0, coeficienteArrastre = 0.2)
        val proyectilPesado = ProjectileConfig(masa = 10.0, coeficienteArrastre = 0.2)
        val entorno = EnvironmentConfig(
            fuenteGravedad = GravedadPrueba(9.8),
            viento = WindConfig(magnitud = 2.0, anguloGrados = 90.0)
        )

        val aLiviano = calculador.calcularAceleracion(estado, proyectilLiviano, entorno)
        val aPesado = calculador.calcularAceleracion(estado, proyectilPesado, entorno)

        // Fdrag_x = -0.2 * 10 = -2.0, Fviento_x = 2.0 => Neto = 0
        assertEquals(0.0, aLiviano.x, delta)
        assertEquals(0.0, aPesado.x, delta)

        // Si cambiamos el viento a 5.0 Este: Fneto_x = -2.0 + 5.0 = 3.0
        val entornoVientoFuerte = EnvironmentConfig(
            fuenteGravedad = GravedadPrueba(9.8),
            viento = WindConfig(magnitud = 5.0, anguloGrados = 90.0)
        )
        val aLivianoFuerte = calculador.calcularAceleracion(estado, proyectilLiviano, entornoVientoFuerte)
        val aPesadoFuerte = calculador.calcularAceleracion(estado, proyectilPesado, entornoVientoFuerte)

        // aLiviano = 3.0 / 1.0 = 3.0
        // aPesado = 3.0 / 10.0 = 0.3
        assertEquals(3.0, aLivianoFuerte.x, delta)
        assertEquals(0.3, aPesadoFuerte.x, delta)
    }
}
