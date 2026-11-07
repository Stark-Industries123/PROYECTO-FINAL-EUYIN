<!-- evaluacion:inicio -->
# Evaluación — Stark-Industries123/PROYECTO-FINAL-EUYIN

> Documento **generado automáticamente** por la cátedra de Programación II (RepoSense + análisis de calidad POO/SOLID). **No editar a mano**: se regenera en cada evaluación.
> Rama evaluada: `main`. Análisis **heurístico** (regex + métricas): es una criba para orientar la revisión, **no una nota**.
> Generado: 2026-11-06.

## 1. Veredicto

| Aspecto | Resultado |
|---|---|
| Índice de calidad POO/SOLID | **40/100** |
| Código (LOC) | 5,129 |
| Archivos de código | 97 |
| Clases (bien usadas) | 99 (33 OK) |
| Código dentro de clases (%POO) | 95% |
| Funciones fuera de clases | 4 |
| Duplicación lógica | 1.0% |
| Señales S/O/L/I/D | 6/0/0/0/34 |
| Tope duro aplicado | `SUELTAS>=3` → índice ≤ 40 |

> Este informe **describe** lo que el análisis midió con los criterios ya establecidos de la materia. No es una nota.

### Semáforo de aprobación

Criterios de `CRITERIOS-CALIDAD.md`. **Aprobar** se juega con el perfil `estandar`; **destacar**, con el `exigente` (meta de excelencia).

| Semáforo | Resultado |
|---|---|
| Aprobar (`estandar`) | **no cumple** — 3/4 |
| Destacar (`exigente`) | **no cumple** — 1/5 |

**Criterios para aprobar**

| Criterio | Requisito | Tu resultado | Cumple |
|---|---|---|---|
| Código dentro de clases (%POO) | ≥ 85% | 95% | sí |
| Duplicación lógica | ≤ 10% | 1.0% | sí |
| Funciones fuera de clases | ≤ 1 | 4 | no |
| Señales POO/SOLID críticas | ninguna | ninguna | sí |

**Criterios para destacar**

| Criterio | Requisito | Tu resultado | Cumple |
|---|---|---|---|
| Código dentro de clases (%POO) | ≥ 95% | 95% | no |
| Duplicación lógica | ≤ 5% | 1.0% | sí |
| Funciones fuera de clases | ≤ 1 | 4 | no |
| Señales POO/SOLID críticas | ninguna | Baja cohesión (LCOM), Acoplamiento alto (CBO) | no |
| Índice de calidad POO/SOLID | ≥ 60 | 40/100 | no |

### Cómo se calcula el %POO (y por qué no sube solo)

`%POO = líneas de lógica dentro de clases ÷ líneas de lógica totales`.

Se cuentan las líneas de **lógica y campos**; no cuentan `package`/`import`/`using`/anotaciones ni comentarios, Javadoc o docstrings. Es una **proporción**: si agregás código nuevo dentro de una clase, el numerador y el denominador crecen juntos y el porcentaje casi no cambia.

Sube cuando el código que hoy está **fuera** de clases pasa a ser método de una clase (o desaparece): funciones sueltas, `main`, constantes de módulo, scripts y lógica de archivos sueltos. También importa que ese trabajo esté **integrado en `main`**: la evaluación siempre mide `main`, así que lo que sigue en otra rama no se refleja todavía.

## 2. Autoría (RepoSense)

| Autor | Commits | Código (LOC) | Tests (LOC) | Docs (LOC) | Días activos | % del código |
|---|---:|---:|---:|---:|---:|---:|
| Eugenio Mosso | 19 | 3,896 | 2,252 | 2 | 11 | 100% |

> _El LOC de autoría lo calcula RepoSense sobre el historial y puede diferir del LOC de código del análisis de calidad (que mira solo el estado de `main`)._

## 3. Fallas detectadas

Reúne lo que el análisis marcó y lo que todavía falta para cada semáforo. Cuando la señal se puede asociar a archivos, se indican para orientar la revisión. No es una nota.

| Falla | Tu resultado | Requisito | Archivos |
|---|---|---|---|
| Funciones fuera de clases | 4 | Aprobar: ≤ 1<br>Destacar: ≤ 1 | `app/src/main/kotlin/fisicas/motor/ResultadoSimulacion.kt`, `app/src/test/kotlin/fisicas/motor/SimuladorRebotesYVientoTest.kt` |
| Código dentro de clases (%POO) | 95% | Destacar: ≥ 95% | — |
| Señales POO/SOLID críticas | Baja cohesión (LCOM), Acoplamiento alto (CBO) | Destacar: ninguna | `app/src/main/kotlin/fisicas/motor/Simulador.kt` |
| Índice de calidad POO/SOLID | 40/100 | Destacar: ≥ 60 | — |
| Métodos/funciones largos o con muchos parámetros | **4** método(s)/función(es) largos o con muchos parámetros | Sin señales | `app/src/main/kotlin/org/example/App.kt`, `app/src/test/kotlin/fisicas/motor/SimuladorFisicoTest.kt`, `app/src/test/kotlin/ui/GraficadorConsolaMapeoTest.kt` |
| Dependencia de concretos y falta de abstracciones (DIP) | **34** dependencia(s) de concretos sin abstracciones | Sin señales | `app/src/main/kotlin/condiciones/ConfiguradorCondicionesApp.kt`, `app/src/main/kotlin/condiciones/dominio/PuntoRosaVientos.kt`, `app/src/main/kotlin/condiciones/servicio/EnsambladorCondicionesService.kt`, `app/src/main/kotlin/condiciones/ui/ConfiguradorSimulacionConsola.kt`, `app/src/main/kotlin/condiciones/ui/SesionConsolaCondiciones.kt`, `app/src/main/kotlin/condiciones/ui/SolicitadorEntradaService.kt` (+28 más) |
| Clase sin métodos (solo datos) | **5** clase(s) sin métodos (solo datos) | Sin señales | `app/src/main/kotlin/fisicas/motor/MotorFisicaEuler2D.kt`, `app/src/main/kotlin/fisicas/motor/PuntoTrayectoria.kt`, `app/src/test/kotlin/fisicas/fuerzas/CalculadorArrastreAerodinamicoTest.kt`, `app/src/test/kotlin/fisicas/motor/MetricasImpactoTest.kt`, `app/src/test/kotlin/fisicas/motor/MotorFisicaEuler2DTest.kt` |
| Campos públicos (encapsulación) | **17** clase(s) con campos públicos | Sin señales | `app/src/main/kotlin/condiciones/dominio/CuadranteRosa.kt`, `app/src/main/kotlin/condiciones/dominio/PuntoRosaVientos.kt`, `app/src/main/kotlin/condiciones/dominio/Viento.kt`, `app/src/main/kotlin/config/ConfigSimulacion.kt`, `app/src/main/kotlin/config/DireccionVientoSector.kt`, `app/src/main/kotlin/fisicas/config/WindConfig.kt` (+11 más) |

## 4. Aspectos evaluados

Cada aspecto indica **qué mide**, **por qué importa** y **tu resultado**. Cuando el análisis reportó una señal, se agregan preguntas para **investigar la causa** (no hay recetas: entender el porqué es parte de la evaluación).

### 1. Índice de calidad POO/SOLID (indice)

- **Qué mide:** Un resumen numérico (0-100) que combina la riqueza de clases bien usadas con la penalización por señales POO/SOLID y duplicación, y luego aplica topes duros.
- **Por qué importa:** Ordena y prioriza la revisión: un índice bajo indica varias señales que conviene mirar juntas.
- **Tu resultado:** índice **40/100**

### 2. Duplicación lógica (DRY) (duplicacion) — sin señales

- **Qué mide:** Porcentaje de bloques de LÓGICA repetidos. Quedan fuera imports/includes, anotaciones, declaraciones de campos y comentarios/Javadoc/docstrings: esos bloques son boilerplate, no lógica.
- **Por qué importa:** Cada copia de lógica se mantiene por separado: un arreglo hay que repetirlo y los defectos se multiplican.
- **Tu resultado:** **1.0%** de duplicación lógica

### 3. Adopción de POO (% de código en clases) (poo) — sin señales

- **Qué mide:** Proporción de las **líneas de lógica** que viven dentro de una clase. Quedan fuera `package`/`import`/`using`/anotaciones y comentarios/Javadoc/docstrings: no son código que pueda estar dentro de una clase y, si contaran, el porcentaje quedaría topado por el boilerplate de cada archivo.
- **Por qué importa:** El diseño orientado a objetos es el eje de la materia: el comportamiento debería vivir como método de una clase.
- **Tu resultado:** **94.8%** del código dentro de clases
- **Cómo leerlo:** Es una **proporción**, no un acumulado: agregar código dentro de una clase no la sube por sí solo (numerador y denominador crecen juntos). Sube cuando el código que está **fuera** de clases (funciones sueltas, `main`, constantes de módulo, scripts) pasa a ser método de una clase o deja de existir. La medición se hace sobre `main`: el trabajo que todavía vive en otra rama no se refleja hasta que se integra.

### 4. Funciones fuera de clases (sueltas) — con señales

- **Qué mide:** Funciones de nivel de módulo que no pertenecen a ninguna clase (en esta materia el comportamiento debe ser método de una clase).
- **Por qué importa:** Son la forma más directa de código procedural en un proyecto que debería ser orientado a objetos.
- **Tu resultado:** **4** función(es) fuera de clases
- **Para investigar la causa:**
    - ¿Esa función se dejó suelta por urgencia o por desconocer dónde ubicarla?
    - ¿Qué clase debería ser responsable de ese comportamiento?
    - ¿Había una convención sobre dónde poner cada cosa?

### 5. Clase demasiado grande (SRP) (god) — sin señales

- **Qué mide:** Clases con demasiadas líneas o métodos de LÓGICA. No cuentan los getters/setters triviales ni la cantidad de campos por sí sola.
- **Por qué importa:** Una clase que hace de todo cambia por muchos motivos y cuesta entenderla y probarla.
- **Tu resultado:** **0** clase(s) demasiado grande(s)

### 6. Baja cohesión (LCOM) (cohesion) — con señales

- **Qué mide:** Métodos y campos de una clase que casi no se relacionan entre sí.
- **Por qué importa:** Una unidad que cambia por motivos distintos y comparte poca información suele esconder más de una responsabilidad.
- **Tu resultado:** **1** clase(s) con baja cohesión
- **Para investigar la causa:**
    - ¿Qué métodos de la clase no comparten estado con el resto?
    - ¿Cambian por motivos distintos según la tarea?
    - ¿Se pensó su responsabilidad antes de escribirla, o se fue agregando?

### 7. Complejidad alta (complejidad) — sin señales

- **Qué mide:** Complejidad ciclomática: cantidad de caminos posibles dentro de un método o función (condicionales, bucles, casos).
- **Por qué importa:** Muchos caminos = difícil de seguir y de probar; suelen esconderse casos sin cubrir.
- **Tu resultado:** **0** método(s)/función(es) con complejidad alta

### 8. Métodos/funciones largos o con muchos parámetros (tamano) — con señales

- **Qué mide:** Tamaño de los métodos/funciones y cantidad de parámetros.
- **Por qué importa:** Una función que hace varias cosas cuesta leerla, nombrarla y reutilizarla.
- **Tu resultado:** **4** método(s)/función(es) largos o con muchos parámetros
- **Para investigar la causa:**
    - ¿La función creció por acumulación de pasos?
    - ¿Agrupa operaciones que podrían describirse por separado?
    - ¿Los parámetros representan una misma idea (un objeto) o cosas sueltas?

### 9. Acoplamiento alto (CBO) (acoplamiento) — con señales

- **Qué mide:** Cuánto depende una clase de otros tipos del proyecto. No cuentan los tipos de la plataforma (biblioteca estándar, DOM, motor Godot) ni los métodos/propiedades propias.
- **Por qué importa:** Piezas muy enlazadas hacen que un cambio se propague y que sea difícil reemplazar partes.
- **Tu resultado:** **1** clase(s) muy acoplada(s)
- **Para investigar la causa:**
    - ¿La clase conoce detalles internos de muchas otras?
    - ¿Se puede cambiar una parte sin tocar las demás?
    - ¿Los colaboradores llegan como parámetros o se crean por dentro?

### 10. Herencia profunda (DIT) (herencia) — sin señales

- **Qué mide:** Profundidad de la cadena de herencia de una clase.
- **Por qué importa:** Jerarquías muy profundas vuelven difícil saber de dónde viene cada comportamiento.
- **Tu resultado:** **0** clase(s) con herencia profunda

### 11. Switch sobre tipos (OCP) (ocp) — sin señales

- **Qué mide:** Cadenas de dispatch por tipo (`else if ... is` / `else if ... instanceof`, `switch` sobre tipo o `typeof`). Un `is Tipo variable` aislado (pattern matching / cast seguro) NO cuenta como switch.
- **Por qué importa:** Cada tipo nuevo obliga a tocar varios lugares, en vez de extender con una implementación nueva.
- **Tu resultado:** **0** clase(s) con switch sobre tipos

### 12. Interfaz con demasiados métodos (ISP) (isp) — sin señales

- **Qué mide:** Interfaces o clases abstractas con muchos métodos declarados.
- **Por qué importa:** Obliga a implementar operaciones que no se usan y acopla a los clientes a métodos que no necesitan.
- **Tu resultado:** **0** interfaz(es) con demasiados métodos

### 13. Override vacío o 'no implementado' (LSP) (lsp) — sin señales

- **Qué mide:** Métodos sobrescritos que quedan vacíos o lanzan 'no implementado'.
- **Por qué importa:** Suele indicar que la jerarquía no representa bien el problema y rompe la sustitución entre clases.
- **Tu resultado:** **0** override(s) vacío(s) o 'no implementado'

### 14. Dependencia de concretos y falta de abstracciones (DIP) (dip) — con señales

- **Qué mide:** Instanciación directa de clases **propias** concretas y ausencia de interfaces o clases abstractas pese al acoplamiento. No cuentan los structs ni los nodos/tipos del motor.
- **Por qué importa:** Depender de implementaciones concretas dificulta cambiar o probar una parte sin arrastrar las demás.
- **Tu resultado:** **34** dependencia(s) de concretos sin abstracciones
- **Para investigar la causa:**
    - ¿La clase crea por dentro a sus colaboradores en vez de recibirlos?
    - ¿Hay puntos de variación que convendría expresar como abstracción?
    - ¿Cómo se probaría esa clase cambiando una implementación?

### 15. Clase sin métodos (solo datos) (data) — con señales

- **Qué mide:** Clases que solo acumulan campos y no tienen comportamiento.
- **Por qué importa:** Separar datos de comportamiento reparte por el proyecto la lógica que debería estar cerca de esos datos.
- **Tu resultado:** **5** clase(s) sin métodos (solo datos)
- **Para investigar la causa:**
    - ¿Dónde vive el comportamiento que opera sobre esos datos?
    - ¿La clase representa un concepto del dominio o un contenedor técnico?
    - ¿Se evita la lógica de negocio dentro de la clase por comodidad?

### 16. Campos públicos (encapsulación) (encapsulacion) — con señales

- **Qué mide:** Clases que exponen campos sin control de acceso.
- **Por qué importa:** Expone el estado interno y hace que cualquier cambio dependa de quién lo modifica desde afuera.
- **Tu resultado:** **17** clase(s) con campos públicos
- **Para investigar la causa:**
    - ¿Se accede al campo desde otras clases para evitar un método?
    - ¿Qué reglas deberían respetarse al modificar ese estado?
    - ¿Los campos públicos se usan más como configuración que como estado?

### 17. Clase solo con métodos estáticos (utilitaria) (static) — sin señales

- **Qué mide:** Clases donde todos los métodos son estáticos.
- **Por qué importa:** Es comportamiento procedural disfrazado de clase: no se instancia ni participa del modelo de objetos.
- **Tu resultado:** **0** clase(s) solo con métodos estáticos

## 5. Evolución por commit

Métricas calculadas sobre el estado de `main` en **cada commit** (13 commits con trabajo), del primero al último. La calidad usa el perfil `exigente`; el aporte de código lo mide RepoSense. El índice puede **bajar** al crecer el proyecto: se acumulan señales.

```mermaid
xychart-beta
    title "Índice POO/SOLID a lo largo de los commits"
    x-axis ["5e46215", "8beeedf", "3be7fbe", "7284531", "1fcb049", "1a12f7b", "88970ff", "554234e", "f7ce108", "20e1594", "c50dc6f", "0933d54", "aa67ffe"]
    y-axis "Índice" 0 --> 100
    line [0, 0, 38, 33, 33, 28, 37, 32, 23, 13, 5, 5, 5]
```

| # | Fecha | Commit | Autor | LOC | Índice | %POO | Duplic. | Sueltas |
|---|---|---|---|---:|---:|---:|---:|---:|
| 1 | 2026-08-21 | `5e46215` | euge | 0 | 0 | 0% | 0.0% | 0 |
| 2 | 2026-08-24 | `8beeedf` | dqmdz-etec | 0 | 0 | 0% | 0.0% | 0 |
| 3 | 2026-09-28 | `3be7fbe` | euyin | 105 | 38 | 50% | 0.0% | 1 |
| 4 | 2026-09-28 | `7284531` | euyin | 174 | 33 | 34% | 0.0% | 3 |
| 5 | 2026-10-09 | `1fcb049` | euyin | 231 | 33 | 43% | 0.0% | 3 |
| 6 | 2026-10-09 | `1a12f7b` | euyin | 430 | 28 | 70% | 0.0% | 3 |
| 7 | 2026-10-09 | `88970ff` | euyin | 393 | 37 | 80% | 0.0% | 1 |
| 8 | 2026-10-16 | `554234e` | euyin | 445 | 32 | 82% | 0.0% | 1 |
| 9 | 2026-10-16 | `f7ce108` | euyin | 718 | 23 | 88% | 0.0% | 1 |
| 10 | 2026-10-19 | `20e1594` | euyin | 1,845 | 13 | 97% | 1.3% | 0 |
| 11 | 2026-10-30 | `c50dc6f` | euyin | 4,652 | 5 | 99% | 5.8% | 0 |
| 12 | 2026-10-30 | `0933d54` | euyin | 4,795 | 5 | 99% | 5.6% | 0 |
| 13 | 2026-10-30 | `aa67ffe` | euyin | 5,217 | 5 | 99% | 5.2% | 0 |

<!-- actividad-clases:inicio -->
## Actividad en días de clase

> Período **2026-08-14 → 2026-10-29** · **17 clases** (lunes, viernes) · calendario oficial de la materia.
>
> `Commits` = commits de trabajo (no-merge); `Merges` = merges de PR (también son push). Los días sin actividad dicen **sin actividad**. Se usan las fechas de clase del calendario oficial; los días fuera del calendario no se incluyen.
>
> Se recorre `git log --all` sobre un mirror (todas las ramas y `refs/pull/*`), con deduplicación por commit. Fecha = fecha de autor en UTC-3. Los commits locales no pusheados no se ven. Si el trabajo fue en pareja y el commit es de otra persona, figura a nombre del autor del commit; para reflejarlo, usar `Co-authored-by` o una nota manual.
>
> Sección informativa: no modifica el semáforo de aprobación.

### Eugenio Mosso

**9/17 clases con actividad** · 12 commits · 8 merges

| Fecha | Marca | Repositorios | Commits / merges |
|---|:--:|---|--:|
| 2026-08-14 | No | sin actividad | 0 / 0 |
| 2026-08-21 | **Sí** | PROYECTO-FINAL-EUYIN | 1 / 0 |
| 2026-08-24 | No | sin actividad | 0 / 0 |
| 2026-08-28 | No | sin actividad | 0 / 0 |
| 2026-08-31 | **Sí** | PROYECTO-FINAL-EUYIN | 1 / 0 |
| 2026-09-04 | No | sin actividad | 0 / 0 |
| 2026-09-07 | No | sin actividad | 0 / 0 |
| 2026-09-14 | No | sin actividad | 0 / 0 |
| 2026-09-25 | **Sí** | PROYECTO-FINAL-EUYIN | 1 / 0 |
| 2026-09-28 | **Sí** | PROYECTO-FINAL-EUYIN | 1 / 2 |
| 2026-10-02 | No | sin actividad | 0 / 0 |
| 2026-10-05 | No | sin actividad | 0 / 0 |
| 2026-10-09 | **Sí** | PROYECTO-FINAL-EUYIN | 3 / 3 |
| 2026-10-16 | **Sí** | PROYECTO-FINAL-EUYIN | 2 / 2 |
| 2026-10-19 | **Sí** | PROYECTO-FINAL-EUYIN | 1 / 1 |
| 2026-10-23 | **Sí** | PROYECTO-FINAL-EUYIN | 1 / 0 |
| 2026-10-26 | **Sí** | PROYECTO-FINAL-EUYIN | 1 / 0 |

<!-- actividad-clases:fin -->

---
_Informe generado por la cátedra. Los criterios y umbrales de medición corresponden al perfil `exigente` de `calidad.py`; el semáforo de aprobación, a `CRITERIOS-CALIDAD.md` (ver `README.md`). Ante dudas, revisá el archivo señalado._
<!-- evaluacion:fin -->
