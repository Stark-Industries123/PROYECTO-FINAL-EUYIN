package fisicas.colision

import fisicas.config.BounceConfig
import fisicas.estado.ProjectileState
import fisicas.vector.Vector2D
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ManejadorReboteSueloTest {

    private val delta = 1e-6

    @Test
    fun testEnElAireNoAlteraElEstado() {
        val manejador = ManejadorReboteSuelo()
        val estado = ProjectileState(Vector2D(10.0, 5.0 + delta), Vector2D(3.0, -4.0))
        val config = BounceConfig(coeficienteRestitucion = 0.7, friccionSuelo = 0.8)
        val resuelto = manejador.resolverColision(estado, config)
        assertEquals(estado, resuelto)
    }

    @Test
    fun testImpactoContraSueloInvierteVelocidadVerticalYAplicaFriccion() {
        val manejador = ManejadorReboteSuelo()
        val estado = ProjectileState(Vector2D(15.0, -0.1), Vector2D(10.0, -20.0))
        val config = BounceConfig(coeficienteRestitucion = 0.5, friccionSuelo = 0.8, umbralReposo = 0.1)
        val resuelto = manejador.resolverColision(estado, config)
        assertEquals(0.0, resuelto.posicion.y, delta)
        assertEquals(15.0, resuelto.posicion.x, delta)
        assertEquals(10.0, resuelto.velocidad.y, delta)
        assertEquals(8.0, resuelto.velocidad.x, delta)
        assertFalse(resuelto.estaEnReposo)
    }

    @Test
    fun testVelocidadDebajoDeUmbralDetieneEnReposo() {
        val manejador = ManejadorReboteSuelo()
        val estado = ProjectileState(Vector2D(20.0, 0.0), Vector2D(0.02, -0.04))
        val config = BounceConfig(coeficienteRestitucion = 0.5, friccionSuelo = 0.5, umbralReposo = 0.1)
        val resuelto = manejador.resolverColision(estado, config)
        assertEquals(0.0, resuelto.posicion.y, delta)
        assertEquals(20.0, resuelto.posicion.x, delta)
        assertEquals(0.0, resuelto.velocidad.x, delta)
        assertEquals(0.0, resuelto.velocidad.y, delta)
        assertTrue(resuelto.estaEnReposo)
    }

    @Test
    fun testCoeficienteRestitucionCeroAbsorbeImpactoVertical() {
        val manejador = ManejadorReboteSuelo()
        val estado = ProjectileState(Vector2D(5.0, 0.0), Vector2D(5.0, -10.0))
        val config = BounceConfig(coeficienteRestitucion = 0.0, friccionSuelo = 0.5, umbralReposo = 0.01)
        val resuelto = manejador.resolverColision(estado, config)
        assertEquals(0.0, resuelto.velocidad.y, delta)
        assertEquals(2.5, resuelto.velocidad.x, delta)
        assertFalse(resuelto.estaEnReposo)
    }
}
