# Unidad 2: Spring Boot basico

## Pregunta central

Como recibe Spring una peticion HTTP y como encuentra, crea y conecta los objetos que deben procesarla?

## Flujo que debes poder explicar

```text
JSON HTTP
   |
HttpMessageConverter crea OpenAccountRequest
   |
Bean Validation verifica el DTO
   |
AccountController
   |
AccountApplicationService
   |
AccountRepository (interfaz)
   |
InMemoryAccountRepository
   |
BankAccount y Money (dominio Java puro)
```

`ConcurrentHashMap` protege la estructura del mapa, pero no vuelve thread-safe la
mutacion de cada `BankAccount`. En esta unidad las peticiones concurrentes quedan
deliberadamente fuera del alcance; esa limitacion se estudiara y demostrara con
pruebas en la unidad de concurrencia.

## Orden de estudio

1. Abre `BankingApplication` e identifica el paquete raiz del component scan.
2. Sigue el constructor desde `AccountController` hasta `InMemoryAccountRepository`.
3. Dibuja que objetos crea Spring y cuales crea el codigo de negocio.
4. Ejecuta `AccountApplicationServiceTest`: comprueba que el servicio puede existir sin Spring.
5. Ejecuta `AccountControllerTest`: observa que solo se inicia la capa web.
6. Ejecuta `AccountHttpIntegrationTest`: sigue el recorrido completo en memoria.
7. Realiza los laboratorios del contenedor uno por uno.

## Laboratorio 1: identidad de beans

Archivo: `SpringBeanIdentityLabTest`

Antes de ejecutarlo, predice:

- Si dos llamadas a `context.getBean(AccountApplicationService.class)` retornan la misma referencia.
- Por que es posible solicitar `AccountRepository.class` si el bean concreto es `InMemoryAccountRepository`.
- Donde vive el mapa que conserva las cuentas entre dos solicitudes.

## Laboratorio 2: dependencia inexistente

Archivo: `MissingBeanLabTest`

Antes de ejecutarlo, predice:

- Si Spring puede crear `AccountApplicationService` sin un `AccountRepository`.
- En que momento falla: compilacion, arranque o primera peticion.
- Por que Java permite compilar el codigo aunque Spring no pueda iniciar el contexto.

## Laboratorio 3: dependencia ambigua

Archivo: `MultipleBeanCandidatesLabTest`

Antes de ejecutarlo, predice:

- Que sucede si existen dos beans que implementan `AccountRepository`.
- Por que el tipo de la interfaz ya no basta para escoger.
- Que alternativas ofrece Spring para resolverlo y que trade-off introduce cada una.

## Ejercicio de trazado

Para `POST /api/accounts`, identifica quien crea cada objeto:

| Objeto | Creador esperado |
| --- | --- |
| `AccountController` | Spring |
| `AccountApplicationService` | Spring |
| `InMemoryAccountRepository` | Spring |
| `OpenAccountRequest` | Convertidor JSON de Spring MVC |
| `OpenAccountCommand` | Controller |
| `Money` | Servicio de aplicacion |
| `BankAccount` | Factory method del dominio |

No memorices la tabla. Verificala colocando breakpoints en los constructores y factories.

## Fallos dirigidos posteriores

Despues de comprender el recorrido correcto:

1. Quitar temporalmente `@Service`.
2. Mover `InMemoryAccountRepository` fuera del package scan.
3. Quitar `@Valid` del controller.
4. Crear un servicio manualmente con `new` y compararlo con el bean del contexto.
5. Agregar estado de una peticion a un singleton y observar como sobrevive a la siguiente.

Estos cambios se realizan de uno en uno y se revierten despues de explicar el resultado.

## Criterio de salida

Debes poder explicar sin abrir el codigo:

1. Que es un bean y quien lo crea.
2. Que contiene el Application Context.
3. Como funciona la inyeccion por constructor.
4. Que hace component scanning.
5. Que ocurre con cero, uno o dos candidatos para una dependencia.
6. Por que el scope singleton requiere evitar estado mutable de peticion.
7. Diferencia entre DTO, servicio de aplicacion y entidad de dominio.
8. Diferencia entre validacion HTTP e invariante de dominio.
9. Diferencia entre una prueba Java, `@WebMvcTest` y `@SpringBootTest`.
10. El recorrido completo desde JSON hasta `BankAccount` y de regreso a JSON.
