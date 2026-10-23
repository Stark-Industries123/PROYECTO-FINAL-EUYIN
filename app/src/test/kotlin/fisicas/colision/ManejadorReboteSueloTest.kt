package fisicas.colision

import fisicas.config.BounceConfig
import fisicas.estado.ProjectileState
import fisicas.vector.Vector2D
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ManejadorReboteSueloTest {

    private val delta = 1e-6

    @Test
    fun `en el aire (y mayor a cero) no altera el estado`() {
        val manejador = ManejadorReboteSuelo()
        val estado = ProjectileState(Vector2D(10.0, 5.0), Vector2D(3.0, -4.0))
        val config = BounceConfig(coeficienteRestitucion = 0.7, friccionSuelo = 0.8)

        val resuelto = manejador.resolverColision(estado, config)

        assertEquals(estado, resuelto)
    }

    @Test
    fun `impacto contra el suelo invierte y amortigua velocidad vertical y aplica friccion horizontal`() {
        val manejador = ManejadorReboteSuelo()
        // y = -0.1, vy = -20.0, vx = 10.0
        val estado = ProjectileState(Vector2D(15.0, -0.1), Vector2D(10.0, -20.0))
        val config = BounceConfig(
            coeficienteRestitucion = 0.5,
            friccionSuelo = 0.8,
            umbralReposo = 0.1
        )

        val resuelto = manejador.resolverColision(estado, config)

        // y corregida a 0.0
        assertEquals(0.0, resuelto.posicion.y, delta)
        assertEquals(15.0, resuelto.posicion.x, delta)
        // vy_nueva = -0.5 * (-20.0) = 10.0
        assertEquals(10.0, resuelto.velocidad.y, delta)
        // vx_nueva = 10.0 * 0.8 = 8.0
        assertEquals(8.0, resuelto.velocidad.x, delta)
        assertFalse(resuelto.estaEnReposo)
    }

    @Test
    fun `si la velocidad cae por debajo del umbral minimo se detiene en reposo`() {
        val manejador = ManejadorReboteSuelo()
        // Rapidez pequeña por debajo del umbral 0.1
        val estado = ProjectileState(Vector2D(20.0, 0.0), Vector2D(0.02, -0.04))
        val config = BounceConfig(
            coeficienteRestitucion = 0.5,
            friccionSuelo = 0.5,
            umbralReposo = 0.1
        )

        val resuelto = manejador.resolverColision(estado, config)

        assertEquals(0.0, resuelto.posicion.y, delta)
        assertEquals(20.0, resuelto.posicion.x, delta)
        assertEquals(0.0, resuelto.velocidad.x, delta)
        assertEquals(0.0, resuelto.velocidad.y, delta)
        assertTrue(resuelto.estaEnReposo)
    }

    @Test
    fun `cuando coeficiente de restitucion es cero se absorbe impacto vertical`() {
        val manejador = ManejadorReboteSuelo()
        val estado = ProjectileState(Vector2D(5.0, 0.0), Vector2D(5.0, -10.0))
        val config = BounceConfig(
            coeficienteRestitucion = 0.0,
            friccionSuelo = 0.5,
            umbralReposo = 0.01
        )

        val resuelto = manejador.resolverColision(estado, config)

        assertEquals(0.0, resuelto.velocidad.y, delta)
        assertEquals(2.5, resuelto.velocidad.x, delta)
        assertFalse(resuelto.estaEnReposo)
    }
}
