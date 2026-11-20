package ui

import java.awt.Color
import java.awt.Component
import java.awt.Cursor
import java.awt.Dimension
import java.awt.Font
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JTextField

/**
 * Paleta visual y fábrica cohesiva de componentes Swing con estética consistente para la GUI 2D.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Centraliza el estilizado y la creación de controles
 *   visuales reutilizables (botones, etiquetas y campos), evitando duplicación entre pantallas.
 * - Alta Cohesión: Todos los métodos operan sobre la configuración tipográfica y cromática de la instancia.
 */
class EstiloUI(
    val colorFondo: Color = Color(15, 23, 42),
    val colorPanelInterno: Color = Color(30, 41, 59),
    private val colorPrimario: Color = Color(37, 99, 235),
    private val colorPrimarioHover: Color = Color(59, 130, 246),
    private val colorSecundario: Color = Color(71, 85, 105),
    private val colorSecundarioHover: Color = Color(100, 116, 139),
    private val colorTextoPrincipal: Color = Color(248, 250, 252),
    private val colorTextoSecundario: Color = Color(148, 163, 184),
    private val nombreFuente: String = "SansSerif"
) {

    /**
     * Crea un botón destacado de acción principal con efecto hover y acción asociada.
     */
    fun crearBotonPrimario(texto: String, ancho: Int = 280, alto: Int = 56, accion: () -> Unit): JButton {
        return crearBotonJuego(texto, Dimension(ancho, alto), colorPrimario, colorPrimarioHover).apply {
            addActionListener { accion() }
        }
    }

    /**
     * Crea un botón de acción secundaria con efecto hover y acción asociada.
     */
    fun crearBotonSecundario(texto: String, ancho: Int = 280, alto: Int = 56, accion: () -> Unit): JButton {
        return crearBotonJuego(texto, Dimension(ancho, alto), colorSecundario, colorSecundarioHover).apply {
            addActionListener { accion() }
        }
    }

    /**
     * Crea un botón compacto pensado para la barra HUD superior del escenario de juego.
     */
    fun crearBotonCompacto(texto: String, accion: () -> Unit): JButton {
        return JButton(texto).apply {
            font = Font(nombreFuente, Font.BOLD, 12)
            foreground = colorTextoPrincipal
            background = Color(colorPanelInterno.red, colorPanelInterno.green, colorPanelInterno.blue, 210)
            isFocusPainted = false
            isBorderPainted = false
            isOpaque = true
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            border = BorderFactory.createEmptyBorder(8, 14, 8, 14)
            addActionListener { accion() }
        }
    }

    /**
     * Crea una etiqueta de título principal con el tamaño tipográfico indicado.
     */
    fun crearEtiquetaTitulo(texto: String, tamano: Int): JLabel {
        return JLabel(texto).apply {
            alignmentX = Component.CENTER_ALIGNMENT
            font = Font(nombreFuente, Font.BOLD, tamano)
            foreground = colorTextoPrincipal
        }
    }

    /**
     * Crea una etiqueta de subtítulo descriptivo para encabezados.
     */
    fun crearEtiquetaSubtitulo(texto: String): JLabel {
        return JLabel(texto).apply {
            alignmentX = Component.CENTER_ALIGNMENT
            font = Font(nombreFuente, Font.PLAIN, 16)
            foreground = colorTextoSecundario
        }
    }

    /**
     * Crea una etiqueta descriptiva para campos de formulario.
     */
    fun crearEtiquetaCampo(texto: String): JLabel {
        return JLabel(texto).apply {
            font = Font(nombreFuente, Font.BOLD, 14)
            foreground = colorTextoPrincipal
        }
    }

    /**
     * Crea una etiqueta de resumen para mostrar métricas en el HUD del juego.
     */
    fun crearEtiquetaHud(textoInicial: String): JLabel {
        return JLabel(textoInicial).apply {
            foreground = colorTextoPrincipal
            font = Font(nombreFuente, Font.BOLD, 13)
        }
    }

    /**
     * Aplica el estilo visual estándar del tema oscuro a un campo de texto editable.
     */
    fun estilizarCampoTexto(campo: JTextField) {
        campo.font = Font(nombreFuente, Font.PLAIN, 15)
        campo.background = colorPanelInterno
        campo.foreground = colorTextoPrincipal
        campo.caretColor = colorTextoPrincipal
        campo.border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorSecundario, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        )
    }

    /**
     * Aplica la tipografía del tema a un desplegable de selección.
     */
    fun estilizarCombo(combo: JComboBox<String>) {
        combo.font = Font(nombreFuente, Font.PLAIN, 15)
    }

    private fun crearBotonJuego(texto: String, dimension: Dimension, colorBase: Color, colorHover: Color): JButton {
        return JButton(texto).apply {
            alignmentX = Component.CENTER_ALIGNMENT
            font = Font(nombreFuente, Font.BOLD, 18)
            foreground = colorTextoPrincipal
            background = colorBase
            isFocusPainted = false
            isBorderPainted = false
            isContentAreaFilled = true
            isOpaque = true
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            preferredSize = dimension
            maximumSize = dimension
            addMouseListener(object : MouseAdapter() {
                override fun mouseEntered(e: MouseEvent) {
                    background = colorHover
                }

                override fun mouseExited(e: MouseEvent) {
                    background = colorBase
                }
            })
        }
    }
}
