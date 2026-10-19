package ui

import fisicas.motor.PuntoTrayectoria
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * Pruebas unitarias de renderizado básico, configuración y límites del [GraficadorConsola].
 */
class GraficadorConsolaRenderTest {

    @Test
    fun testValoresPorDefectoConstructor() {
        val graficador = GraficadorConsola()
        val lienzo = graficador.renderizar(emptyList())
        assertEquals(GraficadorConsola.DEFAULT_FILAS, lienzo.size)
        assertEquals(GraficadorConsola.DEFAULT_COLUMNAS, lienzo[0].length)
        assertTrue(lienzo.all { fila -> fila.all { it == GraficadorConsola.DEFAULT_CARACTER_FONDO } })
        assertEquals(15, graficador.obtenerFilas())
        assertEquals(60, graficador.obtenerColumnas())
        assertEquals('*', graficador.obtenerCaracterProyectil())
        assertEquals('.', graficador.obtenerCaracterFondo())
    }

    @Test
    fun testValidacionDimensionesConstructor() {
        assertFailsWith<IllegalArgumentException> { GraficadorConsola(filas = 0) }
        assertFailsWith<IllegalArgumentException> { GraficadorConsola(filas = -5) }
        assertFailsWith<IllegalArgumentException> { GraficadorConsola(columnas = 0) }
        assertFailsWith<IllegalArgumentException> { GraficadorConsola(columnas = -10) }
    }

    @Test
    fun testRenderizadoDimensionesYCaracteresPersonalizados() {
        val graficador = GraficadorConsola(
            filas = 8,
            columnas = 20,
            caracterProyectil = 'O',
            caracterFondo = ' '
        )

        val lienzo = graficador.renderizar(emptyList())

        assertEquals(8, lienzo.size, "Debe tener exactamente 8 filas")
        assertTrue(lienzo.all { it.length == 20 }, "Cada fila debe medir exactamente 20 caracteres")
        assertTrue(lienzo.all { fila -> fila.all { it == ' ' } }, "El fondo debe coincidir con el caracter configurado")
    }

    @Test
    fun testGraficarInyectandoEscritorLinea() {
        val salidas = mutableListOf<String>()
        val graficador = GraficadorConsola(
            filas = 4,
            columnas = 8,
            caracterProyectil = '*',
            caracterFondo = '.',
            escritorLinea = { salidas.add(it) }
        )

        val puntos = listOf(
            PuntoTrayectoria(0.0, 0.0, 0.0),
            PuntoTrayectoria(1.0, 4.0, 10.0),
            PuntoTrayectoria(2.0, 8.0, 0.0)
        )

        graficador.graficar(puntos)

        assertTrue(salidas.isNotEmpty(), "Debe haberse emitido texto")
        assertTrue(salidas.any { it.contains("TRAYECTORIA") }, "Debe incluir el título de trayectoria")
        val filasRenderizadas = salidas.filter { it.length == 8 }
        assertEquals(4, filasRenderizadas.size, "Debe contener exactamente 4 filas impresas")
    }

    @Test
    fun testGraficarTrayectoriaVacia() {
        val salidas = mutableListOf<String>()
        val graficador = GraficadorConsola(escritorLinea = { salidas.add(it) })

        graficador.graficar(emptyList())

        assertEquals(1, salidas.size)
        assertTrue(salidas.first().contains("No hay puntos"))
    }
}
