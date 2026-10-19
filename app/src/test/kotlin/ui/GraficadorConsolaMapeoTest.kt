package ui

import fisicas.motor.PuntoTrayectoria
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Pruebas unitarias para verificar la transformación y mapeo de coordenadas continuas a índices discretos.
 */
class GraficadorConsolaMapeoTest {

    @Test
    fun testInversionEjeYYMapeoExtremos() {
        val graficador = GraficadorConsola(filas = 10, columnas = 10, caracterProyectil = '*', caracterFondo = '.')
        val puntos = listOf(
            PuntoTrayectoria(tiempo = 0.0, x = 0.0, y = 0.0),
            PuntoTrayectoria(tiempo = 1.0, x = 5.0, y = 50.0),
            PuntoTrayectoria(tiempo = 2.0, x = 10.0, y = 0.0)
        )

        val lienzo = graficador.renderizar(puntos)

        assertEquals(10, lienzo.size)
        assertEquals('*', lienzo[0][5], "El apogeo Y_max debe mapearse en la fila 0 (arriba)")
        assertEquals('*', lienzo[9][0], "El punto inicial (0,0) debe mapearse en la fila inferior (9), columna 0")
        assertEquals('*', lienzo[9][9], "El punto final (10,0) debe mapearse en la fila inferior (9), columna 9")
    }

    @Test
    fun testMapearCoordenadaXAlcanceCeroOIgualMinMax() {
        val graficador = GraficadorConsola(columnas = 50)
        assertEquals(0, graficador.mapearCoordenadaX(0.0, 0.0, 0.0))
        assertEquals(0, graficador.mapearCoordenadaX(10.0, 10.0, 10.0))
        assertEquals(0, graficador.mapearCoordenadaX(5.0, 10.0, 2.0))
        assertEquals(0, graficador.mapearCoordenadaX(1e-16, 0.0, 1e-15))
        assertEquals(0, graficador.mapearCoordenadaX(0.0, 0.0, GraficadorConsola.EPSILON))
    }

    @Test
    fun testTiroPuntoUnicoAngulo0() {
        val graficador = GraficadorConsola(filas = 5, columnas = 5, caracterProyectil = '*', caracterFondo = '.')
        val puntos = listOf(PuntoTrayectoria(tiempo = 0.0, x = 0.0, y = 0.0))

        val lienzo = graficador.renderizar(puntos)

        assertEquals(5, lienzo.size)
        assertEquals('*', lienzo[4][0])
    }
}
