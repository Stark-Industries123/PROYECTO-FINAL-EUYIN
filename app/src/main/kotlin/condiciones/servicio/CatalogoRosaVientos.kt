package condiciones.servicio

import condiciones.dominio.PuntoRosaVientos

/**
 * Contrato de abstracción para el catálogo de puntos de la rosa de los vientos.
 */
interface CatalogoRosaVientos {

    /**
     * Construye y retorna la lista inmutable de los puntos náuticos ordenados.
     */
    fun construirPuntos(): List<PuntoRosaVientos>
}
