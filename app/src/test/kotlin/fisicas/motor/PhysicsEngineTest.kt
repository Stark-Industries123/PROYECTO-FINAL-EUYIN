package fisicas.motor

import fisicas.colision.ManejadorReboteSuelo
import fisicas.config.EnvironmentConfig
import fisicas.config.ProjectileConfig
import fisicas.estado.ProjectileState
import fisicas.fuerzas.CalculadorFuerzasNewton
import fisicas.vector.Vector2D
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PhysicsEngineTest {

    private val delta = 1e-4

    private fun crearMotor(tiempoMaximo: Double = 300.0): PhysicsEngine {
        return PhysicsEngineImpl(
            calculadorFuerzas = CalculadorFuerzasNewton(),
            manejadorColision = ManejadorReboteSuelo(),
            tiempoMaximo = tiempoMaximo
        )
    }

    @Test
    fun testActualizarPasoEvolucionaEstadoSegunDt() {
        val engine = crearMotor()
        val estadoInicial = ProjectileState(Vector2D(0.0, 10.0), Vector2D(5.0, 0.0), tiempo = 0.0)
        val proyectil = ProjectileConfig(masa = 1.0, coeficienteArrastre = 0.0)
        val entorno = EnvironmentConfig()
        val nuevoEstado = engine.actualizarPaso(estadoInicial, proyectil, entorno, 0.1)
        assertEquals(0.5, nuevoEstado.posicion.x, delta)
        assertEquals(9.9019, nuevoEstado.posicion.y, delta)
        assertEquals(5.0, nuevoEstado.velocidad.x, delta)
        assertEquals(-0.981, nuevoEstado.velocidad.y, delta)
        assertEquals(0.1, nuevoEstado.tiempo, delta)
    }

    @Test
    fun testSimularGeneraTrayectoriaCompletaYFinalizaEnReposo() {
        val engine = crearMotor(tiempoMaximo = 50.0)
        val estadoInicial = ProjectileState(Vector2D(0.0, 0.0), Vector2D(10.0, 10.0), tiempo = 0.0)
        val proyectil = ProjectileConfig(masa = 1.0, coeficienteArrastre = 0.05)
        val entorno = EnvironmentConfig.conVientoYRebote(2.0, 90.0, 0.5, 0.8)
        val trayectoria = engine.simular(estadoInicial, proyectil, entorno, dt = 0.02)
        assertTrue(trayectoria.size > 1)
        val estadoFinal = trayectoria.last()
        assertTrue(estadoFinal.estaEnReposo)
        assertEquals(0.0, estadoFinal.posicion.y, delta)
        assertEquals(0.0, estadoFinal.velocidad.magnitud(), delta)
        assertTrue(estadoFinal.posicion.x > 0.0)
    }

    @Test
    fun testEstadoEnReposoNoSufreAlteraciones() {
        val engine = crearMotor()
        val estadoEnReposo = ProjectileState(Vector2D(0.0, 0.0), Vector2D(0.0, 0.0), 0.0, true)
        val proyectil = ProjectileConfig(masa = 1.0)
        val entorno = EnvironmentConfig()
        val siguiente = engine.actualizarPaso(estadoEnReposo, proyectil, entorno, 0.1)
        assertEquals(estadoEnReposo, siguiente)
        assertEquals(0.0, siguiente.tiempo, delta)
    }
}
