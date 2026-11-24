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
import javax.swing.JLabel
import javax.swing.JTextField

/**
 * Fábrica de componentes Swing estilizados bajo la paleta visual y tipografía del simulador.
 *
 * Centraliza la creación de botones, etiquetas y campos de texto para mantener consistencia
 * visual entre vistas sin duplicar lógica de estilo.
 */
class FabricaComponentesGui(
    private val familiaFuente: String = "SansSerif",
    val colorFondoOscuro: Color = Color(15, 23, 42),
    val colorPanelInterno: Color = Color(30, 41, 59),
    val colorPrimario: Color = Color(37, 99, 235),
    val colorPrimarioHover: Color = Color(59, 130, 246),
    val colorSecundario: Color = Color(71, 85, 105),
    val colorSecundarioHover: Color = Color(100, 116, 139)
) {

    /**
     * Crea un botón principal con color primario y efecto hover.
     */
    fun crearBotonPrimario(texto: String, ancho: Int = 280, alto: Int = 56, accion: () -> Unit): JButton {
        val boton = crearBotonConHover(texto, colorPrimario, colorPrimarioHover, Dimension(ancho, alto))
        boton.font = Font(familiaFuente, Font.BOLD, 18)
        boton.addActionListener { accion() }
        return boton
    }

    /**
     * Crea un botón secundario con color neutro y efecto hover.
     */
    fun crearBotonSecundario(texto: String, ancho: Int = 280, alto: Int = 56, accion: () -> Unit): JButton {
        val boton = crearBotonConHover(texto, colorSecundario, colorSecundarioHover, Dimension(ancho, alto))
        boton.font = Font(familiaFuente, Font.BOLD, 18)
        boton.addActionListener { accion() }
        return boton
    }

    /**
     * Crea un botón compacto para la barra superior del HUD durante la simulación.
     */
    fun crearBotonCompacto(texto: String, accion: () -> Unit): JButton {
        return JButton(texto).apply {
            font = Font(familiaFuente, Font.BOLD, 12)
            foreground = Color.WHITE
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
     * Crea una etiqueta de título centrada con el tamaño de fuente especificado.
     */
    fun crearEtiquetaTitulo(texto: String, tamano: Int): JLabel {
        return JLabel(texto).apply {
            alignmentX = Component.CENTER_ALIGNMENT
            font = Font(familiaFuente, Font.BOLD, tamano)
            foreground = Color(248, 250, 252)
        }
    }

    /**
     * Crea una etiqueta de subtítulo centrada para el menú principal.
     */
    fun crearEtiquetaSubtitulo(texto: String): JLabel {
        return JLabel(texto).apply {
            alignmentX = Component.CENTER_ALIGNMENT
            font = Font(familiaFuente, Font.PLAIN, 16)
            foreground = Color(148, 163, 184)
        }
    }

    /**
     * Crea una etiqueta descriptiva para un campo de formulario.
     */
    fun crearEtiquetaCampo(texto: String): JLabel {
        return JLabel(texto).apply {
            font = Font(familiaFuente, Font.BOLD, 14)
            foreground = Color(226, 232, 240)
        }
    }

    /**
     * Aplica el estilo visual estándar del simulador a un campo de texto editable.
     */
    fun estilizarCampoTexto(campo: JTextField) {
        campo.font = Font(familiaFuente, Font.PLAIN, 15)
        campo.background = colorPanelInterno
        campo.foreground = Color.WHITE
        campo.caretColor = Color.WHITE
        campo.border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorSecundario, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        )
    }

    /**
     * Retorna la fuente estándar para selectores y entradas de formulario.
     */
    fun crearFuenteCampo(): Font = Font(familiaFuente, Font.PLAIN, 15)

    private fun crearBotonConHover(
        texto: String,
        colorBase: Color,
        colorHover: Color,
        dimensiones: Dimension
    ): JButton {
        val boton = JButton(texto).apply {
            alignmentX = Component.CENTER_ALIGNMENT
            font = Font(familiaFuente, Font.BOLD, 18)
            foreground = Color.WHITE
            background = colorBase
            isFocusPainted = false
            isBorderPainted = false
            isContentAreaFilled = true
            isOpaque = true
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            preferredSize = dimensiones
            maximumSize = dimensiones
        }
        boton.addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(e: MouseEvent) {
                boton.background = colorHover
            }

            override fun mouseExited(e: MouseEvent) {
                boton.background = colorBase
            }
        })
        return boton
    }
}
