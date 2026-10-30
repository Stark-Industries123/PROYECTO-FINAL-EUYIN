package fisicas.motor

import fisicas.config.BounceConfig
import fisicas.config.EnvironmentConfig
import fisicas.config.ProjectileConfig
import fisicas.config.WindConfig
import fisicas.estado.ProjectileState
import fisicas.gravedad.FuenteGravedad
import fisicas.vector.Vector2D
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PhysicsEngineTest {

    private val delta = 1e-4

    private class GravedadConstante(override val gravedad: Double = 9.8) : FuenteGravedad {
        override fun obtenerNombre(): String = "GravedadConstante"
    }

    @Test
    fun `actualizarPaso evoluciona estado en el tiempo segun dt`() {
        val engine = PhysicsEngineImpl()
        val estadoInicial = ProjectileState(
            posicion = Vector2D(0.0, 10.0),
            velocidad = Vector2D(5.0, 0.0),
            tiempo = 0.0
        )
        val proyectil = ProjectileConfig(masa = 1.0, coeficienteArrastre = 0.0)
        val entorno = EnvironmentConfig(fuenteGravedad = GravedadConstante(10.0))

        val dt = 0.1
        // Con g = 10, ax = 0, ay = -10
        // Euler-Cromer:
        // nuevaVy = 0 + (-10) * 0.1 = -1.0
        // nuevaVx = 5.0
        // nuevaX = 0 + 5.0 * 0.1 = 0.5
        // nuevaY = 10.0 + (-1.0) * 0.1 = 9.9
        val nuevoEstado = engine.actualizarPaso(estadoInicial, proyectil, entorno, dt)

        assertEquals(0.5, nuevoEstado.posicion.x, delta)
        assertEquals(9.9, nuevoEstado.posicion.y, delta)
        assertEquals(5.0, nuevoEstado.velocidad.x, delta)
        assertEquals(-1.0, nuevoEstado.velocidad.y, delta)
        assertEquals(0.1, nuevoEstado.tiempo, delta)
    }

    @Test
    fun `simular genera trayectoria completa y finaliza en reposo`() {
        val engine = PhysicsEngineImpl()
        // Disparo a 45 grados desde el origen
        val estadoInicial = ProjectileState(
            posicion = Vector2D(0.0, 0.0),
            velocidad = Vector2D(10.0, 10.0),
            tiempo = 0.0
        )
        val proyectil = ProjectileConfig(masa = 1.0, coeficienteArrastre = 0.05)
        val entorno = EnvironmentConfig(
            fuenteGravedad = GravedadConstante(9.8),
            viento = WindConfig(magnitud = 2.0, anguloGrados = 90.0), // Viento hacia Este (+X)
            rebote = BounceConfig(coeficienteRestitucion = 0.5, friccionSuelo = 0.8, umbralReposo = 0.2)
        )

        val trayectoria = engine.simular(estadoInicial, proyectil, entorno, dt = 0.02, tiempoMaximo = 50.0)

        assertTrue(trayectoria.size > 1)
        val estadoFinal = trayectoria.last()

        assertTrue(estadoFinal.estaEnReposo)
        assertEquals(0.0, estadoFinal.posicion.y, delta)
        assertEquals(0.0, estadoFinal.velocidad.magnitud(), delta)
        assertTrue(estadoFinal.posicion.x > 0.0)
    }

    @Test
    fun `estado ya en reposo no sufre alteraciones adicionales`() {
        val engine = PhysicsEngineImpl()
        val estadoEnReposo = ProjectileState.ORIGEN_REPOSO
        val proyectil = ProjectileConfig(masa = 1.0)
        val entorno = EnvironmentConfig()

        val siguiente = engine.actualizarPaso(estadoEnReposo, proyectil, entorno, 0.1)

        assertEquals(estadoEnReposo, siguiente)
    }
}
