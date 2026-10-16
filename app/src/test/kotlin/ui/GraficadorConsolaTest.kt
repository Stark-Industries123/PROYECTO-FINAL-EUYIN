package ui

import fisicas.gravedad.Tierra
import fisicas.motor.PuntoTrayectoria
import fisicas.motor.Simulador
import kotlin.test.*

class GraficadorConsolaTest {

    @Test
    fun testValoresPorDefectoConstructor() {
        val graficador = GraficadorConsola()
        assertEquals(15, graficador.filas)
        assertEquals(60, graficador.columnas)
        assertEquals('*', graficador.caracterProyectil)
        assertEquals('.', graficador.caracterFondo)
    }

    @Test
    fun testValidacionDimensionesConstructor() {
        assertFailsWith<IllegalArgumentException> {
            GraficadorConsola(filas = 0)
        }
        assertFailsWith<IllegalArgumentException> {
            GraficadorConsola(filas = -5)
        }
        assertFailsWith<IllegalArgumentException> {
            GraficadorConsola(columnas = 0)
        }
        assertFailsWith<IllegalArgumentException> {
            GraficadorConsola(columnas = -10)
        }
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
    fun testInversionEjeYYMapeoExtremos() {
        val graficador = GraficadorConsola(filas = 10, columnas = 10, caracterProyectil = '*', caracterFondo = '.')
        // Trayectoria simple: (0,0) -> apogeo en (5, 50) -> impacto en (10, 0)
        val puntos = listOf(
            PuntoTrayectoria(tiempo = 0.0, x = 0.0, y = 0.0),
            PuntoTrayectoria(tiempo = 1.0, x = 5.0, y = 50.0),
            PuntoTrayectoria(tiempo = 2.0, x = 10.0, y = 0.0)
        )

        val lienzo = graficador.renderizar(puntos)

        assertEquals(10, lienzo.size)
        // La altura máxima (y = 50.0) debe estar en la fila 0 (arriba)
        assertEquals('*', lienzo[0][5], "El apogeo Y_max debe mapearse en la fila 0 (arriba)")

        // La posición inicial (0, 0) debe estar en la fila inferior (filas - 1 = 9), columna 0
        assertEquals('*', lienzo[9][0], "El punto inicial (0,0) debe mapearse en la fila inferior (9), columna 0")

        // La posición final (10, 0) debe estar en la fila inferior (filas - 1 = 9), columna 9
        assertEquals('*', lienzo[9][9], "El punto final (10,0) debe mapearse en la fila inferior (9), columna 9")
    }

    @Test
    fun testTiroVerticalAngulo90() {
        // En tiro vertical, X es constante (x = 0) para todos los puntos y debe generar una línea vertical
        val graficador = GraficadorConsola(filas = 5, columnas = 10, caracterProyectil = '*', caracterFondo = '.')
        val puntos = listOf(
            PuntoTrayectoria(tiempo = 0.0, x = 0.0, y = 0.0),
            PuntoTrayectoria(tiempo = 0.5, x = 0.0, y = 5.0),
            PuntoTrayectoria(tiempo = 1.0, x = 0.0, y = 10.0),
            PuntoTrayectoria(tiempo = 1.5, x = 0.0, y = 15.0),
            PuntoTrayectoria(tiempo = 2.0, x = 0.0, y = 20.0),
            PuntoTrayectoria(tiempo = 2.5, x = 0.0, y = 15.0),
            PuntoTrayectoria(tiempo = 3.0, x = 0.0, y = 10.0),
            PuntoTrayectoria(tiempo = 3.5, x = 0.0, y = 5.0),
            PuntoTrayectoria(tiempo = 4.0, x = 0.0, y = 0.0)
        )

        val lienzo = graficador.renderizar(puntos)

        assertEquals(5, lienzo.size)
        // El resultado visual para tiro vertical debe ser una única línea vertical de asteriscos '*' en la columna 0
        for ((indice, fila) in lienzo.withIndex()) {
            assertEquals('*', fila[0], "La fila $indice debe tener un asterisco en la columna 0")
            assertTrue(fila.substring(1).all { it == '.' }, "Las columnas > 0 en fila $indice deben ser solo fondo")
        }
    }

    @Test
    fun testTiroVerticalConSimuladorAngulo90GeneraLineaVertical() {
        val simulador = Simulador(Tierra())
        val resultado = simulador.calcularTrayectoria(velocidadInicial = 20.0, anguloGrados = 90.0)
        val graficador = GraficadorConsola(filas = 10, columnas = 20, caracterProyectil = '*', caracterFondo = '.')
        val lienzo = graficador.renderizar(resultado.trayectoria)

        assertEquals(10, lienzo.size)
        // A 90°, a pesar de imprecisiones de punto flotante en cos(90°), debe renderizar una única línea vertical
        for ((indice, fila) in lienzo.withIndex()) {
            assertEquals('*', fila[0], "La fila $indice debe contener un asterisco en la columna 0")
            assertTrue(fila.substring(1).all { it == '.' }, "La fila $indice no debe proyectar asteriscos en columnas > 0 (sin parábola Altura vs Tiempo)")
        }
    }

    @Test
    fun testMapearCoordenadaXAlcanceCeroOIgualMinMax() {
        val graficador = GraficadorConsola(columnas = 50)
        // Cuando maxX == minX
        assertEquals(0, graficador.mapearCoordenadaX(0.0, 0.0, 0.0))
        assertEquals(0, graficador.mapearCoordenadaX(10.0, 10.0, 10.0))

        // Cuando maxX <= minX
        assertEquals(0, graficador.mapearCoordenadaX(5.0, 10.0, 2.0))

        // Cuando la diferencia maxX - minX es menor o igual a EPSILON (imprecisión en cos(90°))
        assertEquals(0, graficador.mapearCoordenadaX(1e-16, 0.0, 1e-15))
        assertEquals(0, graficador.mapearCoordenadaX(0.0, 0.0, GraficadorConsola.EPSILON))
    }

    @Test
    fun testTiroPuntoUnicoAngulo0() {
        val graficador = GraficadorConsola(filas = 5, columnas = 5, caracterProyectil = '*', caracterFondo = '.')
        val puntos = listOf(
            PuntoTrayectoria(tiempo = 0.0, x = 0.0, y = 0.0)
        )

        val lienzo = graficador.renderizar(puntos)

        assertEquals(5, lienzo.size)
        // Con un único punto en (0,0), debe estar en la fila inferior (4), columna 0
        assertEquals('*', lienzo[4][0])
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
        // Salidas contiene el título + 4 filas
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
