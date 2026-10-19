package fisicas.motor

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Pruebas unitarias para verificar el encapsulamiento e inmutabilidad
 * de los puntos cinemáticos tridimensionales [PuntoTrayectoria].
 */
class PuntoTrayectoriaTest {

    @Test
    fun testEncapsulamientoPuntoTrayectoria() {
        val punto = PuntoTrayectoria(tiempo = 0.5, x = 7.07, y = 3.53)
        assertEquals(0.5, punto.tiempo)
        assertEquals(0.5, punto.t, "El alias t debe coincidir con tiempo")
        assertEquals(7.07, punto.x)
        assertEquals(3.53, punto.y)
        assertEquals(0.0, punto.z, "El valor por defecto de z debe ser 0.0")
        assertEquals(0.0, punto.vx)
        assertEquals(0.0, punto.vy)
        assertEquals(0.0, punto.vz)

        val punto3D = PuntoTrayectoria(
            tiempo = 1.0,
            x = 10.0,
            y = 20.0,
            z = 5.0,
            vx = 2.0,
            vy = 3.0,
            vz = 1.0
        )
        assertEquals(1.0, punto3D.t)
        assertEquals(10.0, punto3D.x)
        assertEquals(20.0, punto3D.y)
        assertEquals(5.0, punto3D.z)
        assertEquals(2.0, punto3D.vx)
        assertEquals(3.0, punto3D.vy)
        assertEquals(1.0, punto3D.vz)
    }
}
