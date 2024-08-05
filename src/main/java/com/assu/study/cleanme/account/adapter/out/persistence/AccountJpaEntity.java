package com.assu.study.cleanme.account.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account_1")
@Data
@AllArgsConstructor
@NoArgsConstructor
class AccountJpaEntity {
  @Id @GeneratedValue private Long id;
}
