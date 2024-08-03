package com.assu.study.cleanme.account.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface ActivityRepository extends JpaRepository<ActivityJpaEntity, Long> {

  @Query(
      "SELECT a FROM ActivityJpaEntity a "
          + "WHERE a.ownerAccountId = :ownerAccountId "
          + "  AND a.timestamp >= :since")
  List<ActivityJpaEntity> findByOwnerSince(
      @Param("ownerAccountId") Long ownerAccountId, @Param("since") LocalDateTime since);

  @Query(
      "SELECT SUM(a.amount) FROM ActivityJpaEntity a "
          + "WHERE a.targetAccountId = :accountId "
          + "  AND a.ownerAccountId = :accountId "
          + "  AND a.timestamp < :until")
  Long getDepositBalanceUntil(
      @Param("accountId") Long accountId, @Param("until") LocalDateTime until);

  @Query(
      "SELECT sum(a.amount) FROM ActivityJpaEntity a "
          + "WHERE a.sourceAccountId = :accountId "
          + "  AND a.ownerAccountId = :accountId "
          + "  AND a.timestamp < :until")
  Long getWithdrawalBalanceUntil(
      @Param("accountId") Long accountId, @Param("until") LocalDateTime until);
}
