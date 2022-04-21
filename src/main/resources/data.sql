INSERT INTO users (username, password)
VALUES ('Alexey', '$2a$10$cWAicrqs0V5ge4nIaMbgjehUBIYngrXXgj6yFcBiEvW51UDFyU9Ui'),
       ('Petr', '$2a$10$cWAicrqs0V5ge4nIaMbgjehUBIYngrXXgj6yFcBiEvW51UDFyU9Ui'),
       ('Ivan', '$2a$10$cWAicrqs0V5ge4nIaMbgjehUBIYngrXXgj6yFcBiEvW51UDFyU9Ui');

INSERT INTO tokens (user_id, value, date_of_expire)
VALUES (2, 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJQZXRyIiwiZXhwIjoxMDk2NTA0NzE2MzZ9.xInGlbdxbIAc2Lii9bkAD_erkxEV-tDjfoGAP3App5Y',
        '5444-09-07 19:20:36');

INSERT INTO messages (datetime_of_message, value, author_id)
VALUES ('2022-04-01 12:01:00', 'Alexey message 1', 1),
       ('2022-04-01 12:02:00', 'Alexey message 2', 1),
       ('2022-04-01 12:03:00', 'Alexey message 3', 1),
       ('2022-04-01 12:04:00', 'Alexey message 4', 1),
       ('2022-04-01 12:05:00', 'Alexey message 5', 1),
       ('2022-04-01 12:06:00', 'Alexey message 6', 1),
       ('2022-04-01 12:07:00', 'Alexey message 7', 1),
       ('2022-04-01 12:08:00', 'Alexey message 8', 1),
       ('2022-04-01 12:09:00', 'Alexey message 9', 1),
       ('2022-04-01 12:10:00', 'Alexey message 10', 1),
       ('2022-04-01 12:11:00', 'Alexey message 11', 1),
       ('2022-04-01 12:12:00', 'Alexey message 12', 1),

       ('2022-04-01 13:01:00', 'Petr message 1', 2),
       ('2022-04-01 13:02:00', 'Petr message 2', 2),
       ('2022-04-01 13:03:00', 'Petr message 3', 2),
       ('2022-04-01 13:04:00', 'Petr message 4', 2),
       ('2022-04-01 13:05:00', 'Petr message 5', 2),
       ('2022-04-01 13:06:00', 'Petr message 6', 2),
       ('2022-04-01 13:07:00', 'Petr message 7', 2),
       ('2022-04-01 13:08:00', 'Petr message 8', 2),
       ('2022-04-01 13:09:00', 'Petr message 9', 2),
       ('2022-04-01 13:10:00', 'Petr message 10', 2),
       ('2022-04-01 13:11:00', 'Petr message 11', 2),
       ('2022-04-01 13:12:00', 'Petr message 12', 2),
       ('2022-04-01 13:13:00', 'Petr message 13', 2),

       ('2022-04-01 14:01:00', 'Ivan message 1', 3),
       ('2022-04-01 14:02:00', 'Ivan message 2', 3),
       ('2022-04-01 14:03:00', 'Ivan message 3', 3),
       ('2022-04-01 14:04:00', 'Ivan message 4', 3),
       ('2022-04-01 14:05:00', 'Ivan message 5', 3),
       ('2022-04-01 14:06:00', 'Ivan message 6', 3),
       ('2022-04-01 14:07:00', 'Ivan message 7', 3),
       ('2022-04-01 14:08:00', 'Ivan message 8', 3),
       ('2022-04-01 14:09:00', 'Ivan message 9', 3),
       ('2022-04-01 14:10:00', 'Ivan message 10', 3),
       ('2022-04-01 14:11:00', 'Ivan message 11', 3),
       ('2022-04-01 14:12:00', 'Ivan message 12', 3),
       ('2022-04-01 14:13:00', 'Ivan message 13', 3),
       ('2022-04-01 14:14:00', 'Ivan message 14', 3);

