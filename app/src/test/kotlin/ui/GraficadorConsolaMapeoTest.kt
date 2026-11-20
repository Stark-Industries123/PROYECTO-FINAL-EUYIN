package ui

import fisicas.motor.PuntoTrayectoria
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Pruebas unitarias para verificar la transformación y mapeo de coordenadas continuas a índices discretos.
 */
class GraficadorConsolaMapeoTest {

    private fun crearTrayectoriaConRebotesNegativos(): List<PuntoTrayectoria> = listOf(
        PuntoTrayectoria(tiempo = 0.0, x = 0.0, y = 0.0),
        PuntoTrayectoria(tiempo = 0.5, x = 2.0, y = 10.0),
        PuntoTrayectoria(tiempo = 1.0, x = -2.0, y = 6.0),
        PuntoTrayectoria(tiempo = 1.5, x = -4.0, y = 0.0),
        PuntoTrayectoria(tiempo = 2.0, x = -6.0, y = 5.0),
        PuntoTrayectoria(tiempo = 2.5, x = -8.0, y = 0.0)
    )

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
        assertFalse(lienzo.any { it.contains('|') }, "Sin desplazamientos negativos no debe dibujarse la línea de origen '|'")
    }

    @Test
    fun testMapearCoordenadaXAlcanceCeroOIgualMinMax() {
        val mapeador = MapeadorGrillaTrayectoria(columnas = 50)
        assertEquals(0, mapeador.mapearCoordenadaX(0.0, 0.0, 0.0))
        assertEquals(0, mapeador.mapearCoordenadaX(10.0, 10.0, 10.0))
        assertEquals(0, mapeador.mapearCoordenadaX(5.0, 10.0, 2.0))
        assertEquals(0, mapeador.mapearCoordenadaX(1e-16, 0.0, 1e-15))
        assertEquals(0, mapeador.mapearCoordenadaX(0.0, 0.0, MapeadorGrillaTrayectoria.EPSILON))
    }

    @Test
    fun testTiroPuntoUnicoAngulo0() {
        val graficador = GraficadorConsola(filas = 5, columnas = 5, caracterProyectil = '*', caracterFondo = '.')
        val puntos = listOf(PuntoTrayectoria(tiempo = 0.0, x = 0.0, y = 0.0))

        val lienzo = graficador.renderizar(puntos)

        assertEquals(5, lienzo.size)
        assertEquals('*', lienzo[4][0])
    }

    @Test
    fun testDesplazamientoNegativoMuestraLineaVerticalOrigenYRebotesHaciaAtras() {
        val salidas = mutableListOf<String>()
        val mapeador = MapeadorGrillaTrayectoria(filas = 6, columnas = 11)
        val renderizador = RenderizadorConsolaAscii(
            caracterProyectil = '*',
            caracterFondo = '.',
            escritorLinea = { salidas.add(it) }
        )
        val graficador = GraficadorConsola(mapeador, renderizador)
        val puntos = crearTrayectoriaConRebotesNegativos()

        val lienzo = graficador.renderizar(puntos)
        val colOrigen = mapeador.mapearCoordenadaX(0.0, -8.0, 2.0)
        assertEquals(8, colOrigen)

        for (fila in 0 until 5) {
            assertEquals('|', lienzo[fila][colOrigen], "La fila $fila en la columna de origen debe mostrar '|'")
        }
        assertEquals('*', lienzo[5][colOrigen], "El origen (0,0) debe mostrar el proyectil '*'")

        val colRebote1 = mapeador.mapearCoordenadaX(-4.0, -8.0, 2.0)
        val colArcoRebote = mapeador.mapearCoordenadaX(-6.0, -8.0, 2.0)
        val colRebote2 = mapeador.mapearCoordenadaX(-8.0, -8.0, 2.0)

        assertTrue(colRebote1 < colOrigen && colRebote2 < colRebote1)
        assertEquals('*', lienzo[5][colRebote1])
        assertEquals('*', lienzo[3][colArcoRebote])
        assertEquals('*', lienzo[5][colRebote2])

        graficador.graficar(puntos)
        assertTrue(salidas.any { it.contains("Sur (-X)") && it.contains("-8.00") })
    }
}
