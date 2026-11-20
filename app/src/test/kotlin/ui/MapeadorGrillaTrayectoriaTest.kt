package ui

import fisicas.motor.PuntoTrayectoria
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Pruebas unitarias aisladas del servicio [MapeadorGrillaTrayectoria], [ProyeccionGrilla] y [CeldaGrilla].
 */
class MapeadorGrillaTrayectoriaTest {

    @Test
    fun testValidacionDimensionesInvalidas() {
        assertFailsWith<IllegalArgumentException> { MapeadorGrillaTrayectoria(filas = 0) }
        assertFailsWith<IllegalArgumentException> { MapeadorGrillaTrayectoria(filas = -3) }
        assertFailsWith<IllegalArgumentException> { MapeadorGrillaTrayectoria(columnas = 0) }
        assertFailsWith<IllegalArgumentException> { MapeadorGrillaTrayectoria(columnas = -8) }
    }

    @Test
    fun testMapearTrayectoriaVaciaRetornaProyeccionVacia() {
        val mapeador = MapeadorGrillaTrayectoria(filas = 5, columnas = 10)
        val proyeccion = mapeador.mapear(emptyList())

        assertTrue(proyeccion.estaVacia())
        assertFalse(proyeccion.tieneDesplazamientoNegativo())
        assertNull(proyeccion.columnaOrigen)
        assertNull(proyeccion.formatearLeyendaEjeX('|'))
        assertEquals(5, proyeccion.filas)
        assertEquals(10, proyeccion.columnas)
    }

    @Test
    fun testMapearTrayectoriaPositivaProyectaCeldasSinColumnaOrigen() {
        val mapeador = MapeadorGrillaTrayectoria(filas = 10, columnas = 10)
        val puntos = listOf(
            PuntoTrayectoria(0.0, 0.0, 0.0),
            PuntoTrayectoria(1.0, 5.0, 50.0),
            PuntoTrayectoria(2.0, 10.0, 0.0)
        )

        val proyeccion = mapeador.mapear(puntos)

        assertFalse(proyeccion.estaVacia())
        assertFalse(proyeccion.tieneDesplazamientoNegativo())
        assertNull(proyeccion.columnaOrigen)
        assertEquals(
            listOf(CeldaGrilla(9, 0), CeldaGrilla(0, 5), CeldaGrilla(9, 9)),
            proyeccion.celdasProyectil
        )
    }

    @Test
    fun testMapearTrayectoriaConDesplazamientoNegativoCalculaColumnaOrigenYLeyenda() {
        val mapeador = MapeadorGrillaTrayectoria(filas = 6, columnas = 11)
        val puntos = listOf(
            PuntoTrayectoria(0.0, 0.0, 0.0),
            PuntoTrayectoria(0.5, 2.0, 10.0),
            PuntoTrayectoria(1.0, -8.0, 0.0)
        )

        val proyeccion = mapeador.mapear(puntos)

        assertTrue(proyeccion.tieneDesplazamientoNegativo())
        assertEquals(8, proyeccion.columnaOrigen)
        assertEquals(-8.0, proyeccion.minX)
        assertEquals(2.0, proyeccion.maxX)
        val leyenda = proyeccion.formatearLeyendaEjeX('|')
        assertTrue(leyenda != null && leyenda.contains("-8.00 m a 2.00 m"))
    }

    @Test
    fun testCeldaGrillaLimitesYDesplazamiento() {
        val celda = CeldaGrilla(fila = 2, columna = 3)
        assertTrue(celda.estaDentroDe(5, 5))
        assertFalse(celda.estaDentroDe(2, 5))
        assertEquals(CeldaGrilla(3, 1), celda.desplazada(1, -2))
    }
}
