# Regras de migração (Liquibase)

Este projeto vai rodar com dados reais de uma clínica. As regras abaixo existem para que uma migração nunca apague dado real por acidente.

## Regra 1 — todo changeset novo é aditivo por padrão

`CREATE TABLE`, `ALTER TABLE ADD COLUMN`, `INSERT` (seed) — sem problema, contexto padrão (roda em dev e prod).

## Regra 2 — changeset destrutivo exige `context:dev-danger`

Qualquer `DROP TABLE`, `TRUNCATE`, `DELETE` em massa ou `ALTER TABLE DROP COLUMN` precisa declarar o contexto `dev-danger` no cabeçalho do changeset:

```sql
--changeset autor:id context:dev-danger
DROP TABLE public.algo_que_nao_precisa_mais;
```

- `application-dev.properties` roda com `spring.liquibase.contexts=development,dev-danger` — o changeset executa normalmente em dev.
- `application-prod.properties` roda com `spring.liquibase.contexts=production` — o Liquibase **pula** qualquer changeset marcado `dev-danger`, mesmo que ele exista no arquivo.

Isso é enforcement técnico, não só combinado: mesmo esquecendo de avisar alguém, o changeset destrutivo fisicamente não roda em produção sem o contexto certo.

## Regra 3 — sempre `pg_dump` antes de aplicar uma migration nova em produção

Antes de subir a API com um changeset novo contra o banco da clínica:

```bash
pg_dump -h <host> -U <user> -d <database> -F c -f backup-pre-migration.dump
```

Custa segundos, é a rede de segurança caso algo saia diferente do esperado.

## Nota sobre o changeset `100-reset-schema-vet`

Esse changeset (em `db.changelog-master.sql`) faz `DROP TABLE ... CASCADE` em todas as tabelas — foi usado para reset em desenvolvimento antes dessas regras existirem. Ele já foi aplicado e está registrado no `DATABASECHANGELOG` do banco, então não roda de novo sozinho. **Não editar/renomear esse changeset** — mexer nele quebra o checksum que o Liquibase guarda e trava o boot da aplicação. Ele fica como está, como peça de histórico; as regras acima valem para tudo daqui em diante.
