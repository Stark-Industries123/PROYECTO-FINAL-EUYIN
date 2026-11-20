package ui

import java.awt.image.BufferedImage
import java.net.URL
import java.text.Normalizer
import javax.imageio.ImageIO

/**
 * Gestiona la normalización del nombre del planeta y la carga diferida con caché de su imagen
 * de fondo desde los recursos del classpath.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Separa la resolución de recursos de imagen y su caché
 *   del estado de animación y del renderizado con `Graphics2D`.
 */
class CargadorFondoPlaneta(planetaInicial: String = "Tierra") {

    private var nombrePlaneta: String = ""
    private var planetaCargado: String? = null
    private var imagenCache: BufferedImage? = null

    init {
        establecerPlaneta(planetaInicial)
    }

    /**
     * Actualiza el planeta activo e invalida la caché si el nombre normalizado cambió.
     */
    fun establecerPlaneta(planeta: String): Boolean {
        val limpio = planeta.trim()
            .removePrefix("/resources/")
            .removePrefix("/")
            .removePrefix("fondo_")
            .removeSuffix(".png")
            .removeSuffix(".jpg")
        val cambio = nombrePlaneta != limpio || planetaCargado == null
        if (cambio) {
            nombrePlaneta = limpio
            planetaCargado = null
            imagenCache = null
        }
        return cambio
    }

    /**
     * Retorna el nombre normalizado del planeta actualmente seleccionado.
     */
    fun obtenerNombreActual(): String = nombrePlaneta

    /**
     * Carga y cachea la imagen de fondo del planeta activo, retornando `null` si no existe recurso.
     */
    fun obtenerImagenFondo(): BufferedImage? {
        if (planetaCargado != nombrePlaneta) {
            val recurso = buscarRecursoImagen()
            imagenCache = runCatching {
                recurso?.let { ImageIO.read(it) }
            }.getOrNull()
            planetaCargado = nombrePlaneta
        }
        return imagenCache
    }

    private fun buscarRecursoImagen(): URL? {
        val sinTildes = Normalizer.normalize(nombrePlaneta, Normalizer.Form.NFD)
            .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        val baseOriginal = nombrePlaneta.lowercase()
        val baseLimpia = sinTildes.lowercase()
        val rutas = listOf(
            "/$baseOriginal.png",
            "/$baseOriginal.jpg",
            "/$baseLimpia.png",
            "/$baseLimpia.jpg",
            "/resources/$baseLimpia.png",
            "/resources/$baseLimpia.jpg"
        )
        return rutas.firstNotNullOfOrNull { javaClass.getResource(it) }
    }
}
