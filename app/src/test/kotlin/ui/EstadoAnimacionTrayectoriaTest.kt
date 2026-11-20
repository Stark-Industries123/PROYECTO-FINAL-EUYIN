package ui

import fisicas.motor.PuntoTrayectoria
import java.awt.image.BufferedImage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EstadoAnimacionTrayectoriaTest {

    private fun crearTrayectoriaMuestra(): List<PuntoTrayectoria> = listOf(
        PuntoTrayectoria(0.0, 0.0, 0.0),
        PuntoTrayectoria(0.5, 10.0, 15.0),
        PuntoTrayectoria(1.0, 20.0, 20.0),
        PuntoTrayectoria(1.5, 30.0, 15.0),
        PuntoTrayectoria(2.0, 40.0, 0.0)
    )

    @Test
    fun testAvanceProgresivoYFinalizacionDeAnimacion() {
        val estado = EstadoAnimacionTrayectoria(puntosPorFrame = 2)
        estado.cargarTrayectoria(crearTrayectoriaMuestra())

        assertTrue(estado.estaEnCurso())
        assertEquals(1, estado.calcularProyeccionVisible(800, 600).size)

        assertFalse(estado.avanzarFrame())
        assertEquals(3, estado.calcularProyeccionVisible(800, 600).size)

        assertTrue(estado.avanzarFrame())
        assertFalse(estado.estaEnCurso())
        assertEquals(5, estado.calcularProyeccionVisible(800, 600).size)
    }

    @Test
    fun testCompletarYDetenerPreservanPuntos() {
        val estado = EstadoAnimacionTrayectoria(puntosPorFrame = 1)
        estado.cargarTrayectoria(crearTrayectoriaMuestra())

        estado.detener()
        assertFalse(estado.estaEnCurso())

        assertTrue(estado.completar())
        val proyectados = estado.calcularProyeccionVisible(900, 650)
        assertEquals(5, proyectados.size)
        assertTrue(proyectados.first().esRebote)
        assertTrue(proyectados.last().esRebote)
    }

    @Test
    fun testCalculoMarcasReglaHudYRenderizadoSinErrores() {
        val estado = EstadoAnimacionTrayectoria()
        estado.cargarTrayectoria(crearTrayectoriaMuestra())
        estado.completar()

        val marcas = estado.calcularMarcasRegla(900, 650)
        assertTrue(marcas.isNotEmpty())
        assertEquals("0 m", marcas.first().etiqueta())

        val renderizador = RenderizadorTrayectoria(CargadorFondoPlaneta("Júpiter"))
        assertEquals("Júpiter", renderizador.obtenerPlanetaActual())

        val imagen = BufferedImage(900, 650, BufferedImage.TYPE_INT_ARGB)
        val g2d = imagen.createGraphics()
        renderizador.dibujarEscenario(g2d, estado, 900, 650)
        g2d.dispose()
    }

    @Test
    fun testPanelTrayectoriaDelegaEstadoYCallbackFinalizacion() {
        var notificado = false
        val panel = PanelTrayectoria(planetaInicial = "Luna", puntosPorFrame = 2) {
            notificado = true
        }
        assertEquals("Luna", panel.obtenerPlanetaActual())

        panel.graficar(crearTrayectoriaMuestra())
        assertEquals(5, panel.obtenerPuntos().size)

        panel.completarAnimacion()
        assertTrue(notificado)
    }
}
