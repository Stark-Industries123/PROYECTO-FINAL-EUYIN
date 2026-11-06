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

    @Test
    fun testDesplazamientoNegativoMuestraLineaVerticalOrigenYRebotesHaciaAtras() {
        val salidas = mutableListOf<String>()
        val graficador = GraficadorConsola(
            filas = 6,
            columnas = 11,
            caracterProyectil = '*',
            caracterFondo = '.',
            escritorLinea = { salidas.add(it) }
        )

        // Proyectil sale de X=0, avanza levemente a +2, retrocede por viento del Norte y rebota hacia atrás (-4, -8)
        val puntos = listOf(
            PuntoTrayectoria(tiempo = 0.0, x = 0.0, y = 0.0),
            PuntoTrayectoria(tiempo = 0.5, x = 2.0, y = 10.0),
            PuntoTrayectoria(tiempo = 1.0, x = -2.0, y = 6.0),
            PuntoTrayectoria(tiempo = 1.5, x = -4.0, y = 0.0), // 1er impacto/rebote hacia atrás
            PuntoTrayectoria(tiempo = 2.0, x = -6.0, y = 5.0), // arco de rebote hacia atrás
            PuntoTrayectoria(tiempo = 2.5, x = -8.0, y = 0.0)  // 2do impacto hacia atrás
        )

        val lienzo = graficador.renderizar(puntos)
        val colOrigen = graficador.mapearCoordenadaX(0.0, -8.0, 2.0) // (-8 a +2 -> 0.0 está en proporción 0.8 -> col 8)
        assertEquals(8, colOrigen)

        // La columna de origen (col 8) debe tener '|' en todas las filas superiores y '*' en el origen (fila 5)
        for (fila in 0 until 5) {
            assertEquals('|', lienzo[fila][colOrigen], "La fila $fila en la columna de origen debe mostrar '|'")
        }
        assertEquals('*', lienzo[5][colOrigen], "El origen (0,0) debe mostrar el proyectil '*'")

        // Los rebotes hacia atrás (-4.0 y -8.0) deben estar a la izquierda de la línea de origen (columnas < colOrigen)
        val colRebote1 = graficador.mapearCoordenadaX(-4.0, -8.0, 2.0) // col 4
        val colArcoRebote = graficador.mapearCoordenadaX(-6.0, -8.0, 2.0) // col 2
        val colRebote2 = graficador.mapearCoordenadaX(-8.0, -8.0, 2.0) // col 0

        assertTrue(colRebote1 < colOrigen && colRebote2 < colRebote1)
        assertEquals('*', lienzo[5][colRebote1], "El primer rebote hacia atrás debe graficarse en el suelo a la izquierda del origen")
        assertEquals('*', lienzo[3][colArcoRebote], "El arco del rebote hacia atrás debe graficarse con '*'")
        assertEquals('*', lienzo[5][colRebote2], "El impacto final hacia atrás debe graficarse en la columna 0")

        // Al graficar por consola debe informar el rango negativo del eje X
        graficador.graficar(puntos)
        assertTrue(salidas.any { it.contains("Sur (-X)") && it.contains("-8.00") })
    }
}
