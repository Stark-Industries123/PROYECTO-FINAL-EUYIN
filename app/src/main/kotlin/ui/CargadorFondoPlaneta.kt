package ui

import java.awt.image.BufferedImage
import java.net.URL
import java.text.Normalizer
import javax.imageio.ImageIO

/**
 * Cargador y caché de imágenes de fondo correspondientes a cada planeta del simulador.
 *
 * Responsabilidad única (SRP): normalizar el nombre del planeta, resolver el recurso gráfico
 * disponible en el classpath y cachear el [BufferedImage] cargado.
 */
class CargadorFondoPlaneta(planetaInicial: String = "Tierra") {

    private var nombrePlaneta: String = ""
    private var planetaCargado: String? = null
    private var imagenFondo: BufferedImage? = null

    init {
        establecerPlaneta(planetaInicial)
    }

    /**
     * Actualiza el planeta seleccionado invalidando la caché si cambió el nombre normalizado.
     */
    fun establecerPlaneta(planeta: String) {
        val limpio = planeta.trim()
            .removePrefix("/resources/")
            .removePrefix("/")
            .removePrefix("fondo_")
            .removeSuffix(".png")
            .removeSuffix(".jpg")
        if (nombrePlaneta != limpio || planetaCargado == null) {
            nombrePlaneta = limpio
            planetaCargado = null
            imagenFondo = null
        }
    }

    /**
     * Retorna el nombre limpio del planeta actualmente seleccionado.
     */
    fun obtenerPlanetaActual(): String = nombrePlaneta

    /**
     * Carga perezosamente y retorna la imagen de fondo del planeta actual, o `null` si no existe.
     */
    fun obtenerImagen(): BufferedImage? {
        if (planetaCargado != nombrePlaneta) {
            val recurso = buscarRecursoActual()
            imagenFondo = runCatching {
                recurso?.let { ImageIO.read(it) }
            }.getOrNull()
            planetaCargado = nombrePlaneta
        }
        return imagenFondo
    }

    private fun buscarRecursoActual(): URL? {
        val nombreSinTildes = normalizarPlanetaActual()
        return javaClass.getResource("/" + nombrePlaneta.lowercase() + ".png")
            ?: javaClass.getResource("/" + nombrePlaneta.lowercase() + ".jpg")
            ?: javaClass.getResource("/" + nombreSinTildes.lowercase() + ".png")
            ?: javaClass.getResource("/" + nombreSinTildes.lowercase() + ".jpg")
            ?: javaClass.getResource("/resources/" + nombreSinTildes.lowercase() + ".png")
            ?: javaClass.getResource("/resources/" + nombreSinTildes.lowercase() + ".jpg")
    }

    private fun normalizarPlanetaActual(): String {
        val normalizado = Normalizer.normalize(nombrePlaneta, Normalizer.Form.NFD)
        return normalizado.replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
    }
}
