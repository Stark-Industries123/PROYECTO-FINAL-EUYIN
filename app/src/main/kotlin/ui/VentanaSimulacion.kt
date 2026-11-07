package ui

import config.ConfigSimulacion
import fisicas.gravedad.FuenteGravedad
import fisicas.gravedad.Tierra
import fisicas.motor.CalculadorTrayectoria
import fisicas.motor.PuntoTrayectoria
import fisicas.motor.ResultadoSimulacion
import fisicas.motor.Simulador
import java.awt.BasicStroke
import java.awt.BorderLayout
import java.awt.CardLayout
import java.awt.Color
import java.awt.Cursor
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.RenderingHints
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import java.text.Normalizer
import javax.imageio.ImageIO
import javax.swing.BorderFactory
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.SwingConstants
import javax.swing.SwingUtilities
import javax.swing.Timer
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Ventana principal interactiva tipo videojuego 2D en Java Swing para la simulación física.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Coordina la navegación entre vistas con [CardLayout],
 *   captura los parámetros de entrada del usuario, los valida mediante [ValidadorEntradaContract],
 *   delega el cálculo físico a [CalculadorTrayectoria] y encarga la animación a [PanelTrayectoria].
 * - Dependency Inversion Principle (DIP): Depende de abstracciones ([CalculadorTrayectoria],
 *   [FuenteGravedad], [ValidadorEntradaContract]) inyectadas por constructor.
 * - Open/Closed Principle (OCP): Permite inyectar distintos motores físicos, planetas o
 *   validadores sin modificar la lógica de la interfaz gráfica.
 */
class VentanaSimulacion(
    private val calculador: CalculadorTrayectoria = Simulador(pasoTiempoMuestreo = 0.016),
    private var fuenteGravedad: FuenteGravedad = Tierra(),
    private val validador: ValidadorEntradaContract = ValidadorEntrada(),
    private val panelTrayectoria: PanelTrayectoria = PanelTrayectoria()
) : JFrame("Simulador 2D de Tiro Parabólico") {

    private val cardLayout = CardLayout()
    private val contenedorVistas = JPanel(cardLayout)

    private val comboPlaneta = JComboBox(NOMBRES_PLANETAS)
    private val campoVelocidad = JTextField("35.0", 10)
    private val campoAngulo = JTextField("45.0", 10)
    private val campoMasa = JTextField("1.0", 10)
    private val campoVelocidadViento = JTextField("10.0", 10)
    private val panelBrujula = PanelBrujulaViento(anguloInicial = 0.0)
    private val etiquetaResumen = JLabel("Listo para jugar.")
    private var ultimaConfiguracion: ConfigSimulacion? = null
    private var ultimoResultado: ResultadoSimulacion? = null

    init {
        panelTrayectoria.establecerAlFinalizarAnimacion { mostrarEstadisticasPostPartida() }
        configurarVentana()
        inicializarVistas()
    }

    private fun configurarVentana() {
        defaultCloseOperation = EXIT_ON_CLOSE
        preferredSize = Dimension(ANCHO_VENTANA, ALTO_VENTANA)
        minimumSize = Dimension(ANCHO_VENTANA, ALTO_VENTANA)
        setSize(ANCHO_VENTANA, ALTO_VENTANA)
        contentPane = contenedorVistas
        setLocationRelativeTo(null)
    }

    private fun inicializarVistas() {
        sincronizarComboConGravedadInicial()
        contenedorVistas.add(crearVistaMenuPrincipal(), VISTA_MENU_PRINCIPAL)
        contenedorVistas.add(crearVistaConfiguracion(), VISTA_CONFIGURACION)
        contenedorVistas.add(crearVistaJuego(), VISTA_JUEGO)
        pack()
        mostrarVista(VISTA_MENU_PRINCIPAL)
    }

    private fun sincronizarComboConGravedadInicial() {
        val nombreActual = fuenteGravedad.obtenerNombre()
        val indice = NOMBRES_PLANETAS.indexOfFirst { it.equals(nombreActual, ignoreCase = true) }
        if (indice >= 0) {
            comboPlaneta.selectedIndex = indice
        }
        panelTrayectoria.establecerPlaneta(nombreActual)
        comboPlaneta.addActionListener {
            fuenteGravedad = resolverPlanetaSeleccionado()
            panelTrayectoria.establecerPlaneta(fuenteGravedad.obtenerNombre())
        }
    }

    /**
     * Cambia la vista activa dentro del [CardLayout] principal.
     */
    fun mostrarVista(nombreVista: String) {
        if (nombreVista != VISTA_JUEGO) {
            panelTrayectoria.detenerAnimacion()
        }
        cardLayout.show(contenedorVistas, nombreVista)
    }

    private fun crearVistaMenuPrincipal(): JPanel {
        return JPanel(GridBagLayout()).apply {
            background = COLOR_FONDO_MENU
            add(crearTarjetaMenuCentral())
        }
    }

    private fun crearTarjetaMenuCentral(): JPanel {
        val btnJugar = crearBotonJuego("JUGAR", COLOR_PRIMARIO, COLOR_PRIMARIO_HOVER).apply {
            addActionListener { iniciarJuego() }
        }
        val btnConfig = crearBotonJuego("CONFIGURACIÓN", COLOR_SECUNDARIO, COLOR_SECUNDARIO_HOVER).apply {
            addActionListener { mostrarVista(VISTA_CONFIGURACION) }
        }
        return JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            isOpaque = false
            add(crearEtiquetaTitulo("JUEGUITO DEL PROYECTIL", 44))
            add(Box.createVerticalStrut(12))
            add(crearEtiquetaSubtitulo("Stark Industries 123 presenta..."))
            add(Box.createVerticalStrut(48))
            add(btnJugar)
            add(Box.createVerticalStrut(20))
            add(btnConfig)
        }
    }

    private fun crearVistaConfiguracion(): JPanel {
        return JPanel(BorderLayout(16, 16)).apply {
            background = COLOR_FONDO_MENU
            border = BorderFactory.createEmptyBorder(24, 36, 24, 36)
            add(crearEncabezadoConfiguracion(), BorderLayout.NORTH)
            add(crearCuerpoConfiguracion(), BorderLayout.CENTER)
            add(crearPieConfiguracion(), BorderLayout.SOUTH)
        }
    }

    private fun crearEncabezadoConfiguracion(): JLabel {
        return crearEtiquetaTitulo("CONFIGURACIÓN DE MISIÓN", 28).apply {
            horizontalAlignment = SwingConstants.CENTER
        }
    }

    private fun crearCuerpoConfiguracion(): JPanel {
        return JPanel(GridBagLayout()).apply {
            isOpaque = false
            val gbc = GridBagConstraints().apply {
                insets = Insets(8, 12, 8, 12)
                fill = GridBagConstraints.BOTH
                weighty = 1.0
            }
            gbc.gridx = 0
            gbc.weightx = 0.55
            add(crearPanelFormularioParametros(), gbc)
            gbc.gridx = 1
            gbc.weightx = 0.45
            add(crearContenedorBrujula(), gbc)
        }
    }

    private fun crearPanelFormularioParametros(): JPanel {
        estilizarCamposFormulario()
        return JPanel(GridBagLayout()).apply {
            background = COLOR_PANEL_INTERNO
            border = BorderFactory.createEmptyBorder(20, 24, 20, 24)
            agregarFilaFormulario(this, 0, "Planeta / Gravedad:", comboPlaneta)
            agregarFilaFormulario(this, 1, "Velocidad inicial (m/s):", campoVelocidad)
            agregarFilaFormulario(this, 2, "Ángulo de disparo (0° - 90°):", campoAngulo)
            agregarFilaFormulario(this, 3, "Masa del proyectil (kg):", campoMasa)
            agregarFilaFormulario(this, 4, "Velocidad del viento (km/h):", campoVelocidadViento)
        }
    }

    private fun estilizarCamposFormulario() {
        val fuenteCampo = Font("SansSerif", Font.PLAIN, 15)
        listOf(campoVelocidad, campoAngulo, campoMasa, campoVelocidadViento).forEach { campo ->
            campo.font = fuenteCampo
            campo.background = Color(30, 41, 59)
            campo.foreground = Color.WHITE
            campo.caretColor = Color.WHITE
            campo.border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color(71, 85, 105), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
            )
        }
        comboPlaneta.font = fuenteCampo
    }

    private fun agregarFilaFormulario(panel: JPanel, fila: Int, texto: String, componente: JComponent) {
        val gbcEtiqueta = GridBagConstraints().apply {
            gridx = 0
            gridy = fila
            anchor = GridBagConstraints.WEST
            insets = Insets(10, 6, 10, 12)
            weightx = 0.45
        }
        val gbcCampo = GridBagConstraints().apply {
            gridx = 1
            gridy = fila
            fill = GridBagConstraints.HORIZONTAL
            insets = Insets(10, 6, 10, 6)
            weightx = 0.55
        }
        panel.add(crearEtiquetaCampo(texto), gbcEtiqueta)
        panel.add(componente, gbcCampo)
    }

    private fun crearContenedorBrujula(): JPanel {
        return JPanel(BorderLayout(8, 8)).apply {
            background = COLOR_PANEL_INTERNO
            border = BorderFactory.createEmptyBorder(16, 16, 16, 16)
            val titulo = crearEtiquetaCampo("Dirección del Viento (Clic o arrastrar):").apply {
                horizontalAlignment = SwingConstants.CENTER
            }
            add(titulo, BorderLayout.NORTH)
            add(panelBrujula, BorderLayout.CENTER)
        }
    }

    private fun crearPieConfiguracion(): JPanel {
        val btnVolver = crearBotonJuego("VOLVER AL MENÚ", COLOR_SECUNDARIO, COLOR_SECUNDARIO_HOVER).apply {
            preferredSize = Dimension(220, 48)
            addActionListener { mostrarVista(VISTA_MENU_PRINCIPAL) }
        }
        val btnJugar = crearBotonJuego("GUARDAR Y JUGAR", COLOR_PRIMARIO, COLOR_PRIMARIO_HOVER).apply {
            preferredSize = Dimension(220, 48)
            addActionListener { iniciarJuego() }
        }
        return JPanel(FlowLayout(FlowLayout.CENTER, 24, 8)).apply {
            isOpaque = false
            add(btnVolver)
            add(btnJugar)
        }
    }

    private fun crearVistaJuego(): JPanel {
        val barraSuperior = JPanel(FlowLayout(FlowLayout.LEFT, 12, 8)).apply {
            isOpaque = false
            val btnMenu = crearBotonCompacto("Menú Principal") { mostrarVista(VISTA_MENU_PRINCIPAL) }
            val btnConfig = crearBotonCompacto("Configuración") { mostrarVista(VISTA_CONFIGURACION) }
            val btnRelanzar = crearBotonCompacto("Repetir Disparo") { iniciarJuego() }
            etiquetaResumen.foreground = Color.WHITE
            etiquetaResumen.font = Font("SansSerif", Font.BOLD, 13)
            add(btnMenu)
            add(btnConfig)
            add(btnRelanzar)
            add(etiquetaResumen)
        }
        return JPanel(BorderLayout()).apply {
            add(panelTrayectoria, BorderLayout.CENTER)
            panelTrayectoria.layout = BorderLayout()
            panelTrayectoria.add(barraSuperior, BorderLayout.NORTH)
        }
    }

    private fun iniciarJuego() {
        val config = construirConfiguracionValida() ?: return
        mostrarVista(VISTA_JUEGO)
        ejecutarSimulacionConConfig(config)
    }

    /**
     * Establece los valores de texto en los campos de entrada principales.
     */
    fun ingresarDatos(
        velocidadTexto: String,
        anguloTexto: String,
        masaTexto: String = campoMasa.text,
        vientoTexto: String = campoVelocidadViento.text,
        direccionVientoGrados: Double = panelBrujula.obtenerAnguloGrados()
    ) {
        campoVelocidad.text = velocidadTexto
        campoAngulo.text = anguloTexto
        campoMasa.text = masaTexto
        campoVelocidadViento.text = vientoTexto
        panelBrujula.establecerAnguloGrados(direccionVientoGrados)
    }

    /**
     * Retorna el mensaje de resumen o error actualmente visible en el HUD de juego.
     */
    fun obtenerTextoResumen(): String = etiquetaResumen.text

    /**
     * Retorna los puntos de la trayectoria cargados en el panel de juego.
     */
    fun obtenerPuntosGraficados(): List<PuntoTrayectoria> = panelTrayectoria.obtenerPuntos()

    /**
     * Valida los datos configurados por el usuario, invoca al motor físico [CalculadorTrayectoria]
     * y lanza la animación en tiempo real en [PanelTrayectoria].
     */
    fun ejecutarSimulacion(): Boolean {
        val config = construirConfiguracionValida() ?: return false
        ejecutarSimulacionConConfig(config)
        return true
    }

    private fun ejecutarSimulacionConConfig(config: ConfigSimulacion) {
        fuenteGravedad = config.fuenteGravedad
        ultimaConfiguracion = config
        panelTrayectoria.establecerPlaneta(fuenteGravedad.obtenerNombre())
        val resultado = calculador.calcularTrayectoria(config)
        ultimoResultado = resultado
        actualizarVista(resultado)
    }

    private fun construirConfiguracionValida(): ConfigSimulacion? {
        val velocidad = validarVelocidadInicial() ?: return null
        val angulo = validador.parsearAngulo(campoAngulo.text).resolver { mostrarError(it) } ?: return null
        val masa = validador.parsearMasa(campoMasa.text).resolver { mostrarError(it) } ?: return null
        val viento = validador.parsearViento(campoVelocidadViento.text).resolver { mostrarError(it) } ?: return null
        val direccion = panelBrujula.obtenerAnguloGrados()
        val planeta = resolverPlanetaSeleccionado()
        return ConfigSimulacion(
            velocidadInicial = velocidad,
            angulo = angulo,
            fuenteGravedad = planeta,
            masaKg = masa,
            velocidadVientoKmH = viento,
            direccionVientoGrados = direccion
        )
    }

    private fun validarVelocidadInicial(): Double? {
        val velocidad = validador.parsearVelocidad(campoVelocidad.text).resolver { mostrarError(it) } ?: return null
        if (!velocidad.isFinite() || velocidad > VELOCIDAD_MAXIMA_MS) {
            mostrarError("Error: La velocidad inicial no puede superar $VELOCIDAD_MAXIMA_MS m/s.")
            return null
        }
        return velocidad
    }

    private fun resolverPlanetaSeleccionado(): FuenteGravedad {
        val opcion = (comboPlaneta.selectedIndex + 1).toString()
        return validador.parsearPlaneta(opcion) ?: fuenteGravedad
    }

    private fun actualizarVista(resultado: ResultadoSimulacion) {
        panelTrayectoria.graficar(resultado.trayectoria)
        etiquetaResumen.text = String.format(
            "Planeta: %s | Tiempo: %.2f s | Alcance: %.2f m | Altura máx: %.2f m | Rebotes: %d",
            fuenteGravedad.obtenerNombre(),
            resultado.tiempoVuelo,
            resultado.distanciaMaxima,
            resultado.alturaMaxima,
            resultado.cantidadRebotes
        ).replace(',', '.')
    }

    /**
     * Construye el listado de resultados finales a partir de [config] y [resultado].
     */
    fun construirTextoEstadisticas(config: ConfigSimulacion, resultado: ResultadoSimulacion): String {
        val sector = config.sectorViento.descripcion
        return listOf(
            "Planeta objetivo: ${config.fuenteGravedad.obtenerNombre()}",
            "Gravedad aplicada: ${config.fuenteGravedad.gravedad} m/s²",
            String.format("Masa del proyectil: %.2f kg", config.masaKg).replace(',', '.'),
            String.format("Viento: %.1f km/h (Sector: %s, %.1f°)", config.velocidadVientoKmH, sector, config.direccionVientoGrados).replace(',', '.'),
            String.format("Origen del viento: Sopla DESDE el %s (%.1f°)", sector, config.direccionVientoGrados).replace(',', '.'),
            String.format("Tiempo de vuelo total: %.2f s", resultado.tiempoVuelo).replace(',', '.'),
            String.format("Alcance máximo frontal (X): %.2f m", resultado.distanciaMaxima).replace(',', '.'),
            "Desviación lateral (Z): 0.00 m",
            String.format("Altura máxima (Y): %.2f m", resultado.alturaMaxima).replace(',', '.'),
            "Cantidad de rebotes: ${resultado.cantidadRebotes}"
        ).joinToString("\n")
    }

    private fun mostrarEstadisticasPostPartida() {
        val config = ultimaConfiguracion ?: return
        val resultado = ultimoResultado ?: return
        val mensaje = construirTextoEstadisticas(config, resultado)
        if (!java.awt.GraphicsEnvironment.isHeadless() && isShowing) {
            JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Estadísticas Finales de la Misión",
                JOptionPane.INFORMATION_MESSAGE
            )
        }
    }

    private fun mostrarError(mensaje: String) {
        etiquetaResumen.text = mensaje
        if (!java.awt.GraphicsEnvironment.isHeadless() && isShowing) {
            JOptionPane.showMessageDialog(this, mensaje, "Error de validación", JOptionPane.ERROR_MESSAGE)
        }
    }

    private fun crearBotonJuego(texto: String, colorBase: Color, colorHover: Color): JButton {
        return JButton(texto).apply {
            alignmentX = CENTER_ALIGNMENT
            font = Font("SansSerif", Font.BOLD, 18)
            foreground = Color.WHITE
            background = colorBase
            isFocusPainted = false
            isBorderPainted = false
            isContentAreaFilled = true
            isOpaque = true
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            preferredSize = Dimension(280, 56)
            maximumSize = Dimension(280, 56)
            instalarEfectoHover(this, colorBase, colorHover)
        }
    }

    private fun crearBotonCompacto(texto: String, accion: () -> Unit): JButton {
        return JButton(texto).apply {
            font = Font("SansSerif", Font.BOLD, 12)
            foreground = Color.WHITE
            background = Color(30, 41, 59, 210)
            isFocusPainted = false
            isBorderPainted = false
            isOpaque = true
            cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            border = BorderFactory.createEmptyBorder(8, 14, 8, 14)
            addActionListener { accion() }
        }
    }

    private fun instalarEfectoHover(boton: JButton, colorBase: Color, colorHover: Color) {
        boton.addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(e: MouseEvent) {
                boton.background = colorHover
            }

            override fun mouseExited(e: MouseEvent) {
                boton.background = colorBase
            }
        })
    }

    private fun crearEtiquetaTitulo(texto: String, tamano: Int): JLabel {
        return JLabel(texto).apply {
            alignmentX = CENTER_ALIGNMENT
            font = Font("SansSerif", Font.BOLD, tamano)
            foreground = Color(248, 250, 252)
        }
    }

    private fun crearEtiquetaSubtitulo(texto: String): JLabel {
        return JLabel(texto).apply {
            alignmentX = CENTER_ALIGNMENT
            font = Font("SansSerif", Font.PLAIN, 16)
            foreground = Color(148, 163, 184)
        }
    }

    private fun crearEtiquetaCampo(texto: String): JLabel {
        return JLabel(texto).apply {
            font = Font("SansSerif", Font.BOLD, 14)
            foreground = Color(226, 232, 240)
        }
    }

    companion object {
        const val ANCHO_VENTANA: Int = 900
        const val ALTO_VENTANA: Int = 650
        const val VISTA_MENU_PRINCIPAL: String = "MenuPrincipal"
        const val VISTA_CONFIGURACION: String = "Configuracion"
        const val VISTA_JUEGO: String = "Juego"

        private const val VELOCIDAD_MAXIMA_MS: Double = 1000.0
        private val NOMBRES_PLANETAS = arrayOf("Tierra", "Luna", "Mercurio", "Júpiter")
        private val COLOR_FONDO_MENU = Color(15, 23, 42)
        private val COLOR_PANEL_INTERNO = Color(30, 41, 59)
        private val COLOR_PRIMARIO = Color(37, 99, 235)
        private val COLOR_PRIMARIO_HOVER = Color(59, 130, 246)
        private val COLOR_SECUNDARIO = Color(71, 85, 105)
        private val COLOR_SECUNDARIO_HOVER = Color(100, 116, 139)

        @JvmStatic
        fun main(args: Array<String> = emptyArray()) {
            SwingUtilities.invokeLater {
                VentanaSimulacion().isVisible = true
            }
        }
    }
}

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

/**
 * Lienzo del escenario de juego encargado de renderizar el fondo a pantalla completa y animar
 * en tiempo real (60 FPS) el desplazamiento y los rebotes del proyectil sin dibujar ejes X/Y.
 *
 * Implementa el contrato [Graficador] respetando el Principio de Responsabilidad Única (SRP).
 */
class PanelTrayectoria(
    planetaInicial: String = "Tierra",
    private val puntosPorFrame: Int = PUNTOS_POR_FRAME_DEFAULT,
    private var alFinalizarAnimacion: () -> Unit = {}
) : JPanel(), Graficador {

    private var nombrePlaneta: String = limpiarNombrePlaneta(planetaInicial)
    private var planetaCargado: String? = null
    private var puntos: List<PuntoTrayectoria> = emptyList()
    private var indiceFrameActual: Int = 0
    private var imagenFondo: BufferedImage? = null
    private val timerAnimacion: Timer = Timer(INTERVALO_FRAME_MS) { avanzarFrame() }.apply {
        initialDelay = 0
        isCoalesce = true
    }

    init {
        isOpaque = true
        establecerPlaneta(planetaInicial)
    }

    /**
     * Registra un callback que se ejecuta cuando el temporizador termina de dibujar el último punto.
     */
    fun establecerAlFinalizarAnimacion(callback: () -> Unit) {
        alFinalizarAnimacion = callback
    }

    /**
     * Establece el nombre del planeta actual cuyo fondo se renderizará en [paintComponent].
     */
    fun establecerPlaneta(planeta: String) {
        val limpio = limpiarNombrePlaneta(planeta)
        if (nombrePlaneta != limpio || planetaCargado == null) {
            nombrePlaneta = limpio
            planetaCargado = null
            imagenFondo = null
        }
        repaint()
    }

    /**
     * Retorna el nombre del planeta actualmente seleccionado en el panel.
     */
    fun obtenerPlanetaActual(): String = nombrePlaneta

    /**
     * Actualiza el planeta o ruta del recurso de fondo y solicita repintado.
     */
    fun cargarFondo(rutaOPlaneta: String) {
        establecerPlaneta(rutaOPlaneta)
    }

    /**
     * Recibe la lista completa de [PuntoTrayectoria] del motor físico e inicia la animación
     * progresiva a 60 FPS avanzando en bloques de puntos por cada tick del temporizador.
     */
    override fun graficar(trayectoria: List<PuntoTrayectoria>) {
        timerAnimacion.stop()
        puntos = trayectoria
        indiceFrameActual = if (trayectoria.isEmpty()) 0 else 1
        if (trayectoria.size > 1) {
            timerAnimacion.start()
            repaint()
        } else if (trayectoria.size == 1) {
            repaint()
            notificarFinAnimacion()
        } else {
            repaint()
        }
    }

    /**
     * Detiene el temporizador de animación activo.
     */
    fun detenerAnimacion() {
        timerAnimacion.stop()
    }

    /**
     * Retorna la colección completa de puntos cargada para la simulación actual.
     */
    fun obtenerPuntos(): List<PuntoTrayectoria> = puntos

    private fun avanzarFrame() {
        if (indiceFrameActual < puntos.size) {
            val salto = max(1, puntosPorFrame)
            indiceFrameActual = min(puntos.size, indiceFrameActual + salto)
            repaint()
            if (indiceFrameActual >= puntos.size) {
                timerAnimacion.stop()
                notificarFinAnimacion()
            }
        } else {
            timerAnimacion.stop()
        }
    }

    private fun notificarFinAnimacion() {
        if (isShowing && width > 0 && height > 0) {
            paintImmediately(0, 0, width, height)
        }
        alFinalizarAnimacion()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        val imagen = cargarImagenFondoPlaneta()
        if (imagen != null) {
            g2d.drawImage(imagen, 0, 0, width, height, this)
        } else {
            g2d.color = COLOR_FONDO_SOLIDO_FALLBACK
            g2d.fillRect(0, 0, width, height)
        }
        if (puntos.isNotEmpty() && indiceFrameActual > 0) {
            dibujarProyectilYEstela(g2d)
        }
    }

    private fun cargarImagenFondoPlaneta(): BufferedImage? {
        if (planetaCargado != nombrePlaneta) {
            val nombreSinTildes = normalizarSinTildes(nombrePlaneta)
            val recurso = javaClass.getResource("/" + nombrePlaneta.lowercase() + ".png")
                ?: javaClass.getResource("/" + nombrePlaneta.lowercase() + ".jpg")
                ?: javaClass.getResource("/" + nombreSinTildes.lowercase() + ".png")
                ?: javaClass.getResource("/" + nombreSinTildes.lowercase() + ".jpg")
                ?: javaClass.getResource("/resources/" + nombreSinTildes.lowercase() + ".png")
                ?: javaClass.getResource("/resources/" + nombreSinTildes.lowercase() + ".jpg")
            imagenFondo = runCatching {
                recurso?.let { ImageIO.read(it) }
            }.getOrNull()
            planetaCargado = nombrePlaneta
        }
        return imagenFondo
    }

    private fun limpiarNombrePlaneta(entrada: String): String {
        return entrada.trim()
            .removePrefix("/resources/")
            .removePrefix("/")
            .removePrefix("fondo_")
            .removeSuffix(".png")
            .removeSuffix(".jpg")
    }

    private fun normalizarSinTildes(texto: String): String {
        val normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD)
        return normalizado.replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
    }

    private fun dibujarProyectilYEstela(g2d: Graphics2D) {
        val escalas = calcularEscalasEscenario()
        val limite = min(indiceFrameActual, puntos.size)
        var anterior: IntArray? = null
        for (i in 0 until limite) {
            val actual = proyectarPuntoEnPantalla(puntos[i], escalas)
            dibujarSegmentoEstela(g2d, anterior, actual)
            anterior = actual
        }
        anterior?.let { dibujarCuerpoProyectil(g2d, it) }
    }

    private fun calcularEscalasEscenario(): DoubleArray {
        val minX = min(0.0, puntos.minOf { it.x })
        val maxX = max(1.0, puntos.maxOf { it.x })
        val rangoX = max(1.0, maxX - minX)
        val rangoY = max(1.0, puntos.maxOf { it.y })
        val anchoUtil = max(1.0, width * 0.84)
        val altoUtil = max(1.0, height * 0.70)
        val escala = min(anchoUtil / rangoX, altoUtil / rangoY)
        return doubleArrayOf(minX, escala, escala)
    }

    private fun proyectarPuntoEnPantalla(punto: PuntoTrayectoria, escalas: DoubleArray): IntArray {
        val origenX = (width * 0.08).toInt()
        val nivelSueloY = (height * PROPORCION_SUELO).toInt()
        val escala = escalas[1]
        val px = origenX + ((punto.x - escalas[0]) * escala).toInt()
        val py = nivelSueloY - (punto.y * escala).toInt()
        return intArrayOf(px, py, if (punto.estaEnSuelo()) 1 else 0)
    }

    private fun dibujarSegmentoEstela(g2d: Graphics2D, anterior: IntArray?, actual: IntArray) {
        if (anterior != null) {
            g2d.color = Color(250, 204, 21, 190)
            g2d.stroke = BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
            g2d.drawLine(anterior[0], anterior[1], actual[0], actual[1])
        }
        if (actual[2] == 1) {
            g2d.color = Color(249, 115, 22, 210)
            g2d.fillOval(actual[0] - 5, actual[1] - 5, 10, 10)
        }
    }

    private fun dibujarCuerpoProyectil(g2d: Graphics2D, posicion: IntArray) {
        g2d.color = Color(239, 68, 68)
        g2d.fillOval(posicion[0] - RADIO_PROYECTIL, posicion[1] - RADIO_PROYECTIL, RADIO_PROYECTIL * 2, RADIO_PROYECTIL * 2)
        g2d.color = Color.WHITE
        g2d.stroke = BasicStroke(1.5f)
        g2d.drawOval(posicion[0] - RADIO_PROYECTIL, posicion[1] - RADIO_PROYECTIL, RADIO_PROYECTIL * 2, RADIO_PROYECTIL * 2)
    }

    companion object {
        private const val INTERVALO_FRAME_MS: Int = 16
        private const val PUNTOS_POR_FRAME_DEFAULT: Int = 12
        private const val PROPORCION_SUELO: Double = 0.86
        private const val RADIO_PROYECTIL: Int = 7
        private val COLOR_FONDO_SOLIDO_FALLBACK = Color(15, 23, 42)
    }
}
