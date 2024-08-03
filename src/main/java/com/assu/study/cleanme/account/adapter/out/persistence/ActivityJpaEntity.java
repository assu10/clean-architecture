package com.assu.study.cleanme.account.adapter.out.persistence;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 특정 계좌에 대한 모든 활동을 저장하는 엔티티
@Entity
@Table(name = "activity")
@Data
@AllArgsConstructor
@NoArgsConstructor
class ActivityJpaEntity {
  @Id @GeneratedValue private Long id;

  @Column private LocalDateTime timestamp;

  @Column private Long ownerAccountId;

  @Column private Long sourceAccountId;

  @Column private Long targetAccountId;

  @Column private Long amount;
}
