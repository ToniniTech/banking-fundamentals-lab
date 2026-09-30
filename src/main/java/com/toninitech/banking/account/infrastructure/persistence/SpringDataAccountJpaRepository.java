package com.toninitech.banking.account.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface SpringDataAccountJpaRepository extends JpaRepository<AccountJpaEntity, UUID> {
}
