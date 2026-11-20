package ui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Pruebas unitarias aisladas del servicio [RenderizadorConsolaAscii].
 */
class RenderizadorConsolaAsciiTest {

    @Test
    fun testRenderizarLienzoConEjeOrigenYProyectil() {
        val proyeccion = ProyeccionGrilla(
            filas = 3,
            columnas = 5,
            celdasProyectil = listOf(CeldaGrilla(2, 3), CeldaGrilla(0, 1), CeldaGrilla(2, 0)),
            columnaOrigen = 3,
            minX = -6.0,
            maxX = 2.0
        )
        val renderizador = RenderizadorConsolaAscii(
            caracterProyectil = 'X',
            caracterFondo = '.',
            caracterEjeOrigen = '|'
        )

        val lienzo = renderizador.renderizarLienzo(proyeccion)

        assertEquals(listOf(".X.|.", "...|.", "X..X."), lienzo)
    }

    @Test
    fun testImprimirProyeccionVaciaEmiteMensajeInformativo() {
        val salidas = mutableListOf<String>()
        val renderizador = RenderizadorConsolaAscii(escritorLinea = { salidas.add(it) })
        val proyeccionVacia = ProyeccionGrilla(filas = 4, columnas = 6, celdasProyectil = emptyList())

        renderizador.imprimir(proyeccionVacia)

        assertEquals(1, salidas.size)
        assertTrue(salidas.first().contains("No hay puntos"))
    }

    @Test
    fun testImprimirProyeccionConRetrocesoEmiteEncabezadoLienzoYLeyenda() {
        val salidas = mutableListOf<String>()
        val renderizador = RenderizadorConsolaAscii(
            caracterProyectil = '*',
            caracterFondo = '.',
            caracterEjeOrigen = ':',
            escritorLinea = { salidas.add(it) }
        )
        val proyeccion = ProyeccionGrilla(
            filas = 2,
            columnas = 4,
            celdasProyectil = listOf(CeldaGrilla(1, 2), CeldaGrilla(0, 0)),
            columnaOrigen = 2,
            minX = -4.0,
            maxX = 2.0
        )

        renderizador.imprimir(proyeccion)

        assertEquals(4, salidas.size)
        assertTrue(salidas[0].contains("TRAYECTORIA"))
        assertEquals("*.:.", salidas[1])
        assertEquals("..*.", salidas[2])
        assertTrue(salidas[3].contains(":") && salidas[3].contains("-4.00 m a 2.00 m"))
    }
}
