package ui

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Cursor
import java.awt.Dimension
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Componente visual interactivo que dibuja una brújula / círculo unitario con puntos cardinales
 * (N, S, E, O) y permite seleccionar el ángulo del viento (0° - 360°) con el ratón.
 */
class PanelBrujulaViento(
    anguloInicial: Double = 0.0,
    private val alCambiarAngulo: (Double) -> Unit = {}
) : JPanel() {

    private var anguloGrados: Double = normalizarAngulo(anguloInicial)

    init {
        isOpaque = false
        preferredSize = Dimension(240, 240)
        cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)
        configurarInteraccionRaton()
    }

    /**
     * Retorna el ángulo del viento actualmente seleccionado en el rango [0.0, 360.0].
     */
    fun obtenerAnguloGrados(): Double = anguloGrados

    /**
     * Actualiza programáticamente el ángulo de la brújula y repinta el componente.
     */
    fun establecerAnguloGrados(nuevoAngulo: Double) {
        anguloGrados = normalizarAngulo(nuevoAngulo)
        alCambiarAngulo(anguloGrados)
        repaint()
    }

    private fun configurarInteraccionRaton() {
        val adaptador = object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                actualizarAnguloDesdeCoordenadas(e.x, e.y)
            }

            override fun mouseDragged(e: MouseEvent) {
                actualizarAnguloDesdeCoordenadas(e.x, e.y)
            }
        }
        addMouseListener(adaptador)
        addMouseMotionListener(adaptador)
    }

    private fun actualizarAnguloDesdeCoordenadas(mouseX: Int, mouseY: Int) {
        val centroX = width / 2.0
        val centroY = height / 2.0
        val dx = mouseX - centroX
        val dy = mouseY - centroY
        val grados = Math.toDegrees(atan2(dx, -dy))
        establecerAnguloGrados((grados + 360.0) % 360.0)
    }

    private fun normalizarAngulo(angulo: Double): Double {
        val modulo = ((angulo % 360.0) + 360.0) % 360.0
        return (modulo * 10.0).roundToInt() / 10.0
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        val cx = width / 2
        val cy = height / 2
        val radio = max(30, min(width, height) / 2 - 34)
        dibujarCirculoUnitario(g2d, cx, cy, radio)
        dibujarPuntosCardinales(g2d, cx, cy, radio)
        dibujarAgujaViento(g2d, cx, cy, radio)
        dibujarLecturaGrados(g2d, cx, cy + radio + 26)
    }

    private fun dibujarCirculoUnitario(g2d: Graphics2D, cx: Int, cy: Int, radio: Int) {
        g2d.color = Color(15, 23, 42)
        g2d.fillOval(cx - radio, cy - radio, radio * 2, radio * 2)
        g2d.color = Color(51, 65, 85)
        g2d.stroke = BasicStroke(1.0f)
        g2d.drawLine(cx - radio, cy, cx + radio, cy)
        g2d.drawLine(cx, cy - radio, cx, cy + radio)
        g2d.color = Color(148, 163, 184)
        g2d.stroke = BasicStroke(2.5f)
        g2d.drawOval(cx - radio, cy - radio, radio * 2, radio * 2)
    }

    private fun dibujarPuntosCardinales(g2d: Graphics2D, cx: Int, cy: Int, radio: Int) {
        g2d.font = Font("SansSerif", Font.BOLD, 14)
        g2d.color = Color(248, 250, 252)
        g2d.drawString("N", cx - 5, cy - radio - 8)
        g2d.drawString("S", cx - 5, cy + radio + 18)
        g2d.drawString("E", cx + radio + 8, cy + 5)
        g2d.drawString("O", cx - radio - 18, cy + 5)
    }

    private fun dibujarAgujaViento(g2d: Graphics2D, cx: Int, cy: Int, radio: Int) {
        val rad = Math.toRadians(anguloGrados)
        val finX = cx + (sin(rad) * (radio - 8)).toInt()
        val finY = cy - (cos(rad) * (radio - 8)).toInt()
        g2d.color = Color(56, 189, 248)
        g2d.stroke = BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
        g2d.drawLine(cx, cy, finX, finY)
        g2d.color = Color(244, 63, 94)
        g2d.fillOval(finX - 6, finY - 6, 12, 12)
        g2d.color = Color.WHITE
        g2d.fillOval(cx - 4, cy - 4, 8, 8)
    }

    private fun dibujarLecturaGrados(g2d: Graphics2D, cx: Int, posY: Int) {
        val texto = String.format("Ángulo: %.1f°", anguloGrados).replace(',', '.')
        g2d.font = Font("SansSerif", Font.BOLD, 14)
        g2d.color = Color(56, 189, 248)
        val anchoTexto = g2d.fontMetrics.stringWidth(texto)
        g2d.drawString(texto, cx - anchoTexto / 2, min(height - 4, posY))
    }
}
