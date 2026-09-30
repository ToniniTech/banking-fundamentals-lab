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

### Unidad 3: JPA, Hibernate y PostgreSQL

- entidad de persistencia separada del dominio;
- adaptador JPA para el puerto `AccountRepository`;
- migraciones versionadas con Flyway;
- persistence context e identidad de objetos administrados;
- estados managed y detached;
- dirty checking y `flush`;
- pruebas reales contra PostgreSQL con Testcontainers.

### Unidad 4: Transacciones de Spring

- transferencia atomica entre dos cuentas;
- `@Transactional` como frontera de aplicacion;
- commit, rollback y diferencia frente a `flush`;
- rollback por defecto para `RuntimeException`;
- laboratorio de excepciones checked y `rollbackFor`;
- proxy transaccional y self-invocation.

La aplicacion todavia no define limites transaccionales en los servicios, niveles de
aislamiento, locking, seguridad ni mensajeria. Esos temas pertenecen a unidades posteriores.

## Requisitos

- Java 21;
- PowerShell o una terminal compatible con Maven Wrapper;
- Docker Desktop para PostgreSQL local y los laboratorios de persistencia.

## Iniciar PostgreSQL

```powershell
docker compose up -d
```

## Ejecutar la aplicacion

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Para repasar la Unidad 2 sin base de datos:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=in-memory"
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

## Ejecutar las pruebas de la Unidad 3

Con Docker Desktop iniciado:

```powershell
.\mvnw.cmd "-Dtest=AccountJpaEntityMappingTest,PostgresAccountRepositoryTest,PersistenceContextLabTest" test
```

Antes de ejecutarlas, completa las predicciones de [`docs/unit-03-jpa-hibernate.md`](docs/unit-03-jpa-hibernate.md).

## Ejecutar las pruebas de la Unidad 4

Con Docker Desktop iniciado:

```powershell
.\mvnw.cmd "-Dtest=TransferTransactionIntegrationTest" test
```

Sigue el orden y las predicciones de [`docs/unit-04-transactions.md`](docs/unit-04-transactions.md).

## Estructura actual

```text
src/main/java/com/toninitech/banking/
├── BankingApplication.java
├── account/
│   ├── api/                 HTTP, DTO y respuestas
│   ├── application/         casos de uso y puertos
│   ├── domain/              Java puro de la Unidad 1
│   └── infrastructure/      adaptadores en memoria y JPA
└── shared/
    ├── api/                 traduccion centralizada de errores
    └── money/               value object de la Unidad 1

src/test/java/com/toninitech/banking/
├── account/                 pruebas por nivel
└── learninglabs/            experimentos aislados
```

## Regla de avance

La Unidad 4 introduce limites transaccionales en un unico caso de uso: la
transferencia. Antes de avanzar a concurrencia, debes poder explicar el
persistence context, dirty checking, `flush`, commit y rollback.

