# Banking Fundamentals Lab

Proyecto educativo incremental para estudiar fundamentos de Java y Spring mediante un dominio bancario pequeno. Cada unidad agrega una sola familia de mecanismos y conserva el dominio libre de dependencias innecesarias.

## Alcance actual

### Unidad 1: Java Core

- `Money`, value object inmutable basado en `BigDecimal` y `Currency`;
- `BankAccount`, entidad con identidad estable e invariantes de saldo;
- excepciones de dominio;
- laboratorios sobre igualdad, hashing, mutabilidad y dinero.

### Unidad 2: Spring Boot basico

- Application Context y component scanning;
- beans singleton;
- inyeccion por constructor;
- controller, servicio de aplicacion y repositorio en memoria;
- DTO y Bean Validation;
- manejo centralizado de errores HTTP;
- pruebas unitarias, `@WebMvcTest` y `@SpringBootTest`;
- laboratorios de dependencias inexistentes y ambiguas.

La aplicacion todavia no contiene JPA, PostgreSQL, transacciones, seguridad ni mensajeria.

## Requisitos

- Java 21;
- PowerShell o una terminal compatible con Maven Wrapper.

## Ejecutar la aplicacion

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Abrir una cuenta desde otra terminal:

```powershell
$body = @{
    openingBalance = 100.00
    currency = 'USD'
} | ConvertTo-Json

Invoke-RestMethod `
    -Method Post `
    -Uri 'http://localhost:8080/api/accounts' `
    -ContentType 'application/json' `
    -Body $body
```

## Ejecutar las pruebas de la Unidad 2

```powershell
.\mvnw.cmd "-Dtest=AccountApplicationServiceTest,AccountControllerTest,AccountHttpIntegrationTest,*SpringBeanIdentityLabTest,*MissingBeanLabTest,*MultipleBeanCandidatesLabTest" test
```

Antes de ejecutar los laboratorios, lee y responde las preguntas de [`docs/unit-02-spring-basics.md`](docs/unit-02-spring-basics.md).

## Estructura actual

```text
src/main/java/com/toninitech/banking/
├── BankingApplication.java
├── account/
│   ├── api/                 HTTP, DTO y respuestas
│   ├── application/         casos de uso y puertos
│   ├── domain/              Java puro de la Unidad 1
│   └── infrastructure/      repositorio temporal en memoria
└── shared/
    ├── api/                 traduccion centralizada de errores
    └── money/               value object de la Unidad 1

src/test/java/com/toninitech/banking/
├── account/                 pruebas por nivel
└── learninglabs/            experimentos aislados
```

## Regla de avance

La Unidad 3 introducira JPA y PostgreSQL solo cuando se pueda explicar el ciclo completo de una peticion, la creacion de beans y la resolucion de dependencias de la Unidad 2.

