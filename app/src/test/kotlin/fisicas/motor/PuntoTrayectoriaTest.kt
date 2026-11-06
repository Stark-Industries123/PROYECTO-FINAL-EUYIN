package fisicas.motor

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Pruebas unitarias para verificar el encapsulamiento e inmutabilidad
 * de los puntos cinemáticos tridimensionales [PuntoTrayectoria].
 */
class PuntoTrayectoriaTest {

    @Test
    fun testPuntoTrayectoriaValoresPorDefecto() {
        val punto = PuntoTrayectoria(tiempo = 0.5, x = 7.07, y = 3.53)
        assertEquals(0.5, punto.tiempo)
        assertEquals(0.5, punto.t)
        assertEquals(7.07, punto.x)
        assertEquals(3.53, punto.y)
        assertEquals(0.0, punto.z)
        assertEquals(0.0, punto.vx)
        assertEquals(0.0, punto.vy)
        assertEquals(0.0, punto.vz)
    }

    @Test
    fun testPuntoTrayectoriaCompleto3D() {
        val punto3D = PuntoTrayectoria(1.0, 10.0, 20.0, 5.0, 2.0, 3.0, 1.0)
        assertEquals(1.0, punto3D.t)
        assertEquals(10.0, punto3D.x)
        assertEquals(20.0, punto3D.y)
        assertEquals(5.0, punto3D.z)
        assertEquals(2.0, punto3D.vx)
        assertEquals(3.0, punto3D.vy)
        assertEquals(1.0, punto3D.vz)
    }
}
