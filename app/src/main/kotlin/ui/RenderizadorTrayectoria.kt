package ui

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.RenderingHints
import kotlin.math.max

/**
 * Encargado estrictamente del renderizado visual 2D mediante [Graphics2D]: dibuja el fondo del
 * planeta, la regla vertical de altura HUD, la estela del proyectil, los impactos de rebote y
 * el cuerpo del proyectil.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Se limita al trazado visual sobre [Graphics2D],
 *   consumiendo coordenadas ya proyectadas por [EstadoAnimacionTrayectoria] e imágenes desde
 *   [CargadorFondoPlaneta].
 */
class RenderizadorTrayectoria(
    private val cargadorFondo: CargadorFondoPlaneta = CargadorFondoPlaneta(),
    private val colorFondoFallback: Color = Color(15, 23, 42),
    private val colorEstela: Color = Color(250, 204, 21, 190),
    private val colorRebote: Color = Color(249, 115, 22, 210),
    private val colorProyectil: Color = Color(239, 68, 68),
    private val colorHudTexto: Color = Color.WHITE,
    private val colorHudSombra: Color = Color(0, 0, 0, 185),
    private val radioProyectil: Int = 7
) {

    /**
     * Actualiza el planeta cuyo fondo visual se renderizará en el escenario.
     */
    fun establecerPlaneta(planeta: String) {
        cargadorFondo.establecerPlaneta(planeta)
    }

    /**
     * Retorna el nombre del planeta configurado en el cargador de fondos.
     */
    fun obtenerPlanetaActual(): String = cargadorFondo.obtenerNombreActual()

    /**
     * Renderiza el escenario completo (fondo, regla HUD y trayectoria visible) en [g2d].
     */
    fun dibujarEscenario(g2d: Graphics2D, estado: EstadoAnimacionTrayectoria, ancho: Int, alto: Int) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        val imagen = cargadorFondo.obtenerImagenFondo()
        if (imagen != null) {
            g2d.drawImage(imagen, 0, 0, ancho, alto, null)
        } else {
            g2d.color = colorFondoFallback
            g2d.fillRect(0, 0, ancho, alto)
        }
        g2d.color = colorHudTexto
        dibujarReglaAltura(g2d, estado.calcularMarcasRegla(ancho, alto), alto)
        if (estado.tienePuntosVisibles()) {
            dibujarTrayectoria(g2d, estado.calcularProyeccionVisible(ancho, alto))
        }
    }

    private fun dibujarReglaAltura(g2d: Graphics2D, marcas: List<MarcaAlturaHud>, alto: Int) {
        val nivelSueloY = (alto * EstadoAnimacionTrayectoria.PROPORCION_SUELO).toInt()
        val topeReglaY = max(
            EstadoAnimacionTrayectoria.MARGEN_SUPERIOR_REGLA_Y,
            (nivelSueloY - alto * EstadoAnimacionTrayectoria.PROPORCION_ALTO_UTIL).toInt()
        )
        if (nivelSueloY <= topeReglaY) return
        g2d.color = colorHudSombra
        g2d.stroke = BasicStroke(3.0f)
        g2d.drawLine(X_EJE_REGLA + 1, nivelSueloY, X_EJE_REGLA + 1, topeReglaY)
        g2d.color = Color(colorHudTexto.red, colorHudTexto.green, colorHudTexto.blue, 220)
        g2d.stroke = BasicStroke(1.8f)
        g2d.drawLine(X_EJE_REGLA, nivelSueloY, X_EJE_REGLA, topeReglaY)
        g2d.font = Font("SansSerif", Font.BOLD, 11)
        marcas.forEach { dibujarMarcaAltura(g2d, it) }
    }

    private fun dibujarMarcaAltura(g2d: Graphics2D, marca: MarcaAlturaHud) {
        val py = marca.posicionY
        val etiqueta = marca.etiqueta()
        g2d.color = colorHudSombra
        g2d.stroke = BasicStroke(2.5f)
        g2d.drawLine(X_INICIO_MARCA + 1, py + 1, X_FIN_MARCA + 1, py + 1)
        g2d.color = colorHudTexto
        g2d.stroke = BasicStroke(2.0f)
        g2d.drawLine(X_INICIO_MARCA, py, X_FIN_MARCA, py)
        val textoX = X_FIN_MARCA + 6
        val anchoTexto = g2d.fontMetrics.stringWidth(etiqueta)
        g2d.color = Color(colorFondoFallback.red, colorFondoFallback.green, colorFondoFallback.blue, 175)
        g2d.fillRoundRect(textoX - 4, py - 9, anchoTexto + 8, 16, 6, 6)
        g2d.color = colorHudSombra
        g2d.drawString(etiqueta, textoX + 1, py + 5)
        g2d.color = colorHudTexto
        g2d.drawString(etiqueta, textoX, py + 4)
    }

    private fun dibujarTrayectoria(g2d: Graphics2D, puntos: List<PuntoProyectado>) {
        var anterior: PuntoProyectado? = null
        for (actual in puntos) {
            if (anterior != null) {
                g2d.color = colorEstela
                g2d.stroke = BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
                g2d.drawLine(anterior.x, anterior.y, actual.x, actual.y)
            }
            if (actual.esRebote) {
                g2d.color = colorRebote
                g2d.fillOval(actual.x - 5, actual.y - 5, 10, 10)
            }
            anterior = actual
        }
        val cabeza = anterior ?: return
        g2d.color = colorProyectil
        g2d.fillOval(cabeza.x - radioProyectil, cabeza.y - radioProyectil, radioProyectil * 2, radioProyectil * 2)
        g2d.color = colorHudTexto
        g2d.stroke = BasicStroke(1.5f)
        g2d.drawOval(cabeza.x - radioProyectil, cabeza.y - radioProyectil, radioProyectil * 2, radioProyectil * 2)
    }

    companion object {
        private const val X_EJE_REGLA: Int = 14
        private const val X_INICIO_MARCA: Int = 10
        private const val X_FIN_MARCA: Int = 22
    }
}
