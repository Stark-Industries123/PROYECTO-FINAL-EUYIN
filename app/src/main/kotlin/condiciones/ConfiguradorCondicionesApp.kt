package condiciones

import condiciones.dominio.CondicionesIniciales
import condiciones.io.EscritorConsola
import condiciones.io.LectorConsola
import condiciones.servicio.EnsambladorCondiciones
import condiciones.servicio.EnsambladorCondicionesService
import condiciones.ui.SesionCondiciones
import condiciones.ui.SesionConsolaCondiciones

/**
 * Orquestador principal de la aplicación para configurar las condiciones iniciales de un tiro parabólico.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Coordina exclusivamente la secuencia de alto nivel entre
 *   la sesión de consola ([SesionCondiciones]) y el ensamblador de dominio ([EnsambladorCondiciones]).
 * - Alta Cohesión y Bajo Acoplamiento: No mezcla reglas de validación, cálculo trigonométrico ni formato de texto.
 */
class ConfiguradorCondicionesApp(
    private val sesion: SesionCondiciones = SesionConsolaCondiciones(),
    private val ensamblador: EnsambladorCondiciones = EnsambladorCondicionesService()
) {

    /**
     * Constructor secundario compatible con pruebas de integración que inyectan canales de consola.
     */
    constructor(
        lector: LectorConsola,
        escritor: EscritorConsola
    ) : this(
        sesion = SesionConsolaCondiciones(lector, escritor),
        ensamblador = EnsambladorCondicionesService()
    )

    /**
     * Ejecuta el flujo completo de presentación, captura, ensamblaje de dominio y reporte final.
     */
    fun ejecutar(): CondicionesIniciales {
        sesion.mostrarGuiaRosaDeLosVientos()
        val condiciones = capturarYEnsamblar()
        sesion.mostrarReporte(condiciones)
        return condiciones
    }

    private fun capturarYEnsamblar(): CondicionesIniciales {
        val datos = sesion.capturarParametrosCondiciones()
        return ensamblador.ensamblar(
            direccionGrados = datos[0],
            velocidadViento = datos[1],
            pesoProyectil = datos[2]
        )
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String> = emptyArray()) {
            ConfiguradorCondicionesApp().ejecutar()
        }
    }
}
