CREATE TABLE `account_1`
(
    `id` bigint NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `activity_1`
(
    `id`                bigint NOT NULL AUTO_INCREMENT,
    `timestamp`         datetime DEFAULT NULL,
    `owner_account_id`  bigint   DEFAULT NULL,
    `source_account_id` bigint   DEFAULT NULL,
    `target_account_id` bigint   DEFAULT NULL,
    `amount`            bigint   DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

insert into account_1 (id)
values (1);
insert into account_1 (id)
values (2);