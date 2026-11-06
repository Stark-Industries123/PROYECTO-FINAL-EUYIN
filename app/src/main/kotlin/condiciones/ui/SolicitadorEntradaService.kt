package condiciones.ui

import condiciones.io.EscritorConsola
import condiciones.io.LectorConsola
import fisicas.gravedad.FuenteGravedad

/**
 * Servicio coordinador de la solicitud de parámetros de entrada para la simulación.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Coordina qué parámetro se solicita en cada paso,
 *   delegando el bucle de lectura y control de errores en [LectorInteractivoConsola] y las
 *   reglas de validación de rangos en [ValidadorParametrosSimulacion].
 * - Alta Cohesión: Todos sus métodos operan sobre [interactivo] y [validador].
 */
class SolicitadorEntradaService(
    private val interactivo: LectorInteractivoConsola = LectorInteractivoConsola(),
    private val validador: ValidadorParametrosSimulacion = ValidadorParametrosSimulacion()
) {

    /**
     * Constructor secundario que construye el servicio a partir de [LectorConsola] y [EscritorConsola].
     */
    constructor(
        lector: LectorConsola,
        escritor: EscritorConsola
    ) : this(
        interactivo = LectorInteractivoConsola(lector, escritor),
        validador = ValidadorParametrosSimulacion()
    )

    fun solicitarPlaneta(): FuenteGravedad {
        return interactivo.solicitarPlaneta { entrada -> validador.parsearPlaneta(entrada) }
    }

    fun solicitarVelocidadInicial(): Double {
        return interactivo.solicitarDoubleValido("Ingrese la velocidad inicial (m/s, > 0): ") { valor ->
            validador.validarVelocidadInicial(valor)
        }
    }

    fun solicitarAnguloDisparo(): Double {
        return interactivo.solicitarDoubleValido("Ingrese el ángulo de disparo (grados, 0-90): ") { valor ->
            validador.validarAnguloDisparo(valor)
        }
    }

    fun solicitarAngulo(): Double {
        return interactivo.solicitarDoubleValido("Ingrese el ángulo de disparo (grados, 0-90): ") { valor ->
            validador.validarAnguloDisparo(valor)
        }
    }

    fun solicitarDireccionVientoConGuia(presentador: PresentadorRosaVientosService? = null): Double {
        if (presentador != null) {
            presentador.mostrarGuiaGeneralViento()
        } else {
            interactivo.mostrarGuiaCardinalDefecto()
        }
        return interactivo.solicitarDoubleValido("Ingrese la dirección del viento en grados (0-360): ") { valor ->
            validador.validarDireccionViento(valor)
        }
    }

    fun solicitarDireccionViento(): Double {
        return interactivo.solicitarDoubleValido("Ingrese la dirección del viento en grados (0-360): ") { valor ->
            validador.validarDireccionViento(valor)
        }
    }

    fun solicitarVelocidadViento(): Double {
        return interactivo.solicitarDoubleValido("Ingrese la velocidad del viento (km/h, >= 0): ") { valor ->
            validador.validarVelocidadViento(valor)
        }
    }

    fun solicitarPesoProyectil(): Double {
        return interactivo.solicitarDoubleValido("Ingrese el peso del proyectil en kg (> 0): ") { valor ->
            validador.validarPesoProyectil(valor)
        }
    }
}
