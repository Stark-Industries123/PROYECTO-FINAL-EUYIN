package fisicas.motor

/**
 * Fábrica encargada de ensamblar una instancia completa de [IntegradorCinematica3D]
 * conectando su configuración ([ParametrosIntegracion]) con la fábrica concreta de
 * estado cinemático ([FabricaEstadoCinematica3D]).
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Centraliza el ensamblaje de colaboradores
 *   concretos del integrador 3D, manteniendo desacoplados tanto a [Simulador] como a
 *   [IntegradorCinematica3D].
 */
class FabricaIntegradorCinematica3D {

    /**
     * Crea un [IntegradorCinematica] tridimensional configurado a partir de [parametros].
     */
    fun crear(parametros: ParametrosIntegracion = ParametrosIntegracion()): IntegradorCinematica {
        return IntegradorCinematica3D(
            parametros = parametros,
            fabricaEstado = FabricaEstadoCinematica3D()
        )
    }

    /**
     * Crea un [IntegradorCinematica] tridimensional construyendo [ParametrosIntegracion]
     * desde los parámetros numéricos básicos.
     */
    fun crear(
        coeficienteRestitucion: Double = ParametrosIntegracion.DEFAULT_COEFICIENTE_RESTITUCION,
        coeficienteArrastre: Double = ParametrosIntegracion.DEFAULT_COEFICIENTE_ARRASTRE,
        pasoTiempoMuestreo: Double = ParametrosIntegracion.DEFAULT_PASO_TIEMPO_MUESTREO
    ): IntegradorCinematica {
        val parametros = ParametrosIntegracion(
            coeficienteRestitucion = coeficienteRestitucion,
            coeficienteArrastre = coeficienteArrastre,
            pasoTiempoMuestreo = pasoTiempoMuestreo
        )
        return crear(parametros)
    }
}
