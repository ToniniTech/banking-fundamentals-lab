# Unidad 4: Transacciones de Spring

## Pregunta central

Como garantizamos que una transferencia debite una cuenta y acredite otra como una sola operacion?

```text
transfer()
    |
    +-- debito origen
    +-- credito destino
    |
    +-- ambos cambios confirman, o ambos se revierten
```

## El modelo minimo

La Unidad 3 respondia: que entidades vigila Hibernate?

La Unidad 4 responde: que cambios confirma o revierte la base de datos como una unidad?

```text
Persistence context
    Hibernate vigila AccountJpaEntity y prepara SQL.

Transaction
    Decide si los cambios SQL se confirman con commit o se descartan con rollback.
```

`flush()` no es `commit`.

## Flujo de una transferencia

```text
Controller o caller externo
        |
        v
Proxy de Spring
        |
        | abre transaccion
        v
TransferApplicationService.transfer()
        |
        +-- debita origen
        +-- acredita destino
        |
        | termina normalmente
        v
commit
```

Si `target.credit()` lanza una `RuntimeException` porque la cuenta destino esta bloqueada:

```text
debito origen ejecutado en memoria
        |
target.credit() falla
        |
proxy de Spring marca rollback
        |
ningun cambio de saldo queda confirmado
```

## Codigo que estudiar

- `TransferApplicationService`: frontera transaccional real con `@Transactional`.
- `TransferTransactionIntegrationTest`: prueba commit, rollback, excepciones y proxy.
- `CheckedExceptionRollbackLabService`: muestra la regla por defecto para excepciones checked.
- `SelfInvocationLabService`: muestra el limite del proxy.

## Orden de aprendizaje

1. Lee `TransferApplicationService.transfer` y predice por que `@Transactional` esta en el servicio y no en `BankAccount`.
2. Ejecuta solamente `successfulTransferCommitsBothBalanceChanges`.
3. Ejecuta `runtimeExceptionRollsBackTheEarlierDebit` y predice los dos saldos antes de mirar los asserts.
4. Quita temporalmente `@Transactional` de `transfer`, ejecuta el segundo test y observa el debito parcial. Reviertelo despues.
5. Ejecuta los dos tests de excepciones checked. Explica por que uno deja saldo `90.00` y el otro conserva `100.00`.
6. Ejecuta los dos tests de self-invocation. Compara llamada externa mediante proxy con llamada interna usando `this`.

## Regla de rollback por defecto

Spring hace rollback automaticamente para `RuntimeException` y `Error`.

Una excepcion checked no provoca rollback por defecto. Si quieres que lo provoque, declara la decision:

```java
// @Transactional(rollbackFor = TransferReviewRequiredException.class)
```

No es una regla de Java; es la politica por defecto de Spring Transaction Management.

## Self-invocation

Esta llamada llega desde fuera del bean y cruza el proxy:

```java
// selfInvocationLab.transactionIsActiveWhenCalledThroughProxy();
```

Esta llamada ocurre dentro del mismo objeto y no cruza el proxy:

```java
// this.transactionIsActiveWhenCalledThroughProxy();
```

Por ello una anotacion `@Transactional` no se aplica automaticamente a una llamada interna.

## Criterio de salida

Puedes cerrar la Unidad 4 cuando puedas explicar, sin mirar el codigo:

1. Por que una transferencia debe tener una frontera transaccional.
2. Por que `@Transactional` esta en `TransferApplicationService.transfer`.
3. Diferencia entre `flush`, `commit` y `rollback`.
4. Por que una excepcion de cuenta bloqueada revierte tambien el debito.
5. Diferencia entre una excepcion checked y una `RuntimeException` para el rollback por defecto.
6. Por que `rollbackFor` cambia esa regla.
7. Que es el proxy transaccional y por que self-invocation lo evita.

Todavia no estudies propagacion, aislamiento, locking, reintentos, idempotencia ni outbox.
