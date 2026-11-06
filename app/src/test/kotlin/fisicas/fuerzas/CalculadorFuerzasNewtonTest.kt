package fisicas.fuerzas

import fisicas.config.EnvironmentConfig
import fisicas.config.ProjectileConfig
import fisicas.config.WindConfig
import fisicas.estado.ProjectileState
import fisicas.vector.Vector2D
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CalculadorFuerzasNewtonTest {

    private val delta = 1e-6

    @Test
    fun testSinArrastreYSinVientoSoloActuaGravedad() {
        val calculador = CalculadorFuerzasNewton()
        val estado = ProjectileState(Vector2D(0.0, 10.0), Vector2D(15.0, 5.0))
        val proyectil = ProjectileConfig(masa = 2.0, coeficienteArrastre = 0.0)
        val entorno = EnvironmentConfig()
        val a = calculador.calcularAceleracion(estado, proyectil, entorno)
        assertEquals(0.0, a.x, delta)
        assertEquals(-9.81, a.y, delta)
    }

    @Test
    fun testResistenciaDelAireOpuestaAlMovimiento() {
        val calculador = CalculadorFuerzasNewton()
        val estado = ProjectileState(Vector2D(0.0, 10.0), Vector2D(10.0, 20.0))
        val proyectil = ProjectileConfig(masa = 2.0, coeficienteArrastre = 0.5)
        val entorno = EnvironmentConfig()
        val a = calculador.calcularAceleracion(estado, proyectil, entorno)
        assertEquals(-2.5, a.x, delta)
        assertEquals(-14.81, a.y, delta)
    }

    @Test
    fun testFuerzaDelVientoSumaAceleracionHorizontal() {
        val calculador = CalculadorFuerzasNewton()
        val estado = ProjectileState(Vector2D(0.0, 10.0), Vector2D(10.0, 20.0))
        val proyectil = ProjectileConfig(masa = 2.0, coeficienteArrastre = 0.5)
        val entorno = EnvironmentConfig(viento = WindConfig(magnitud = 4.0, anguloGrados = 90.0))
        val a = calculador.calcularAceleracion(estado, proyectil, entorno)
        assertEquals(-0.5, a.x, delta)
        assertEquals(-14.81, a.y, delta)
    }

    @Test
    fun testMayorMasaReduceImpactoDeVientoYArrastre() {
        val calculador = CalculadorFuerzasNewton()
        val estado = ProjectileState(Vector2D(0.0, 10.0), Vector2D(10.0, 0.0))
        val liviano = ProjectileConfig(masa = 1.0, coeficienteArrastre = 0.2)
        val pesado = ProjectileConfig(masa = 10.0, coeficienteArrastre = 0.2)
        val entorno = EnvironmentConfig(viento = WindConfig(magnitud = 5.0, anguloGrados = 90.0))
        val aLiviano = calculador.calcularAceleracion(estado, liviano, entorno)
        val aPesado = calculador.calcularAceleracion(estado, pesado, entorno)
        assertEquals(3.0, aLiviano.x, delta)
        assertEquals(0.3, aPesado.x, delta)
    }
}
