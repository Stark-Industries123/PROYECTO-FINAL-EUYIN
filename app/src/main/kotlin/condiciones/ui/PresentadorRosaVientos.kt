package condiciones.ui

import condiciones.dominio.CondicionesIniciales

/**
 * Contrato de abstracción para la presentación de la rosa de los vientos y reportes de condiciones.
 */
interface PresentadorRosaVientos {

    fun mostrarGuiaGeneralViento()

    fun mostrarRosaDeLosVientos()

    fun mostrarReporteFinal(condiciones: CondicionesIniciales)
}
