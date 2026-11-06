package fisicas.motor

import fisicas.fuerzas.CalculadorArrastreAerodinamico
import fisicas.fuerzas.CalculadorFuerzaAerodinamica

/**
 * Implementación del motor de integración física mediante el método de Euler-Cromer (semi-implícito).
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Se encarga exclusivamente de actualizar las velocidades
 *   y posiciones del proyectil en cada diferencial de tiempo dt, sin mezclar lógica de rebotes ni I/O.
 * - Dependency Inversion Principle (DIP): Depende de la abstracción [CalculadorFuerzaAerodinamica]
 *   inyectada por constructor para determinar las aceleraciones por arrastre y viento.
 *
 * @property calculadorAerodinamico Servicio que calcula la fuerza y aceleración de arrastre aerodinámico.
 */
class MotorFisicaEuler2D(
    private val calculadorAerodinamico: CalculadorFuerzaAerodinamica = CalculadorArrastreAerodinamico()
) : MotorFisica2D {

    fun obtenerCalculadorAerodinamico(): CalculadorFuerzaAerodinamica = calculadorAerodinamico

    override fun integrarPaso(
        estado: PuntoTrayectoria,
        ax: Double,
        ay: Double,
        dt: Double,
        az: Double
    ): PuntoTrayectoria {
        require(dt > 0.0) {
            "El diferencial de tiempo dt debe ser estrictamente mayor a 0.0 (recibido: $dt)."
        }

        // Integración semi-implícita Euler-Cromer: primero velocidades, luego posiciones
        val nuevoVx = estado.vx + ax * dt
        val nuevoVy = estado.vy + ay * dt
        val nuevoVz = estado.vz + az * dt

        val nuevoX = estado.x + nuevoVx * dt
        val nuevoY = estado.y + nuevoVy * dt
        val nuevoZ = estado.z + nuevoVz * dt

        return PuntoTrayectoria(
            tiempo = estado.tiempo + dt,
            x = nuevoX,
            y = nuevoY,
            z = nuevoZ,
            vx = nuevoVx,
            vy = nuevoVy,
            vz = nuevoVz
        )
    }

    override fun avanzarEstado(
        estado: PuntoTrayectoria,
        masa: Double,
        gravedad: Double,
        vientoX: Double,
        dt: Double,
        vientoZ: Double
    ): PuntoTrayectoria {
        require(masa > 0.0) {
            "La masa del proyectil debe ser estrictamente mayor a 0.0 kg (recibido: $masa)."
        }

        val ax = calculadorAerodinamico.calcularAceleracion(vientoX, estado.vx, masa)
        val ay = -gravedad + calculadorAerodinamico.calcularAceleracion(0.0, estado.vy, masa)
        val az = calculadorAerodinamico.calcularAceleracion(vientoZ, estado.vz, masa)

        return integrarPaso(estado, ax, ay, dt, az)
    }
}
