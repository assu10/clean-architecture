insert into account (id)
values (1);
insert into account (id)
values (2);

insert into activity (id, timestamp, owner_account_id, source_account_id, target_account_id, amount)
values (1, '2023-08-08 08:00:00.0', 1, 1, 2, 500);

insert into activity (id, timestamp, owner_account_id, source_account_id, target_account_id, amount)
values (2, '2023-08-08 08:00:00.0', 2, 1, 2, 500);

insert into activity (id, timestamp, owner_account_id, source_account_id, target_account_id, amount)
values (3, '2023-08-09 10:00:00.0', 1, 2, 1, 1000);

insert into activity (id, timestamp, owner_account_id, source_account_id, target_account_id, amount)
values (4, '2023-08-09 10:00:00.0', 2, 2, 1, 1000);

insert into activity (id, timestamp, owner_account_id, source_account_id, target_account_id, amount)
values (5, '2024-08-09 09:00:00.0', 1, 1, 2, 1000);

insert into activity (id, timestamp, owner_account_id, source_account_id, target_account_id, amount)
values (6, '2024-08-09 09:00:00.0', 2, 1, 2, 1000);

insert into activity (id, timestamp, owner_account_id, source_account_id, target_account_id, amount)
values (7, '2024-08-09 10:00:00.0', 1, 2, 1, 1000);

insert into activity (id, timestamp, owner_account_id, source_account_id, target_account_id, amount)
values (8, '2024-08-09 10:00:00.0', 2, 2, 1, 1000);