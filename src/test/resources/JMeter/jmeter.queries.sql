/*
    * Queries for JMeter tests
    * clean up database after tests
    * reset sequences
    */
*/

select * from users;

select * from lancamento;

delete from lancamento where user_id > 5;
delete from user_roles where user_id > 5;
delete from users where id > 5;
SELECT setval('users_id_seq', 6, true);
SELECT setval('lancamento_seq', 2, true);


SELECT COALESCE(MAX(id), 0) + 1 AS next_id FROM lancamento;


select * from lancamento

select max(id) from lancamento