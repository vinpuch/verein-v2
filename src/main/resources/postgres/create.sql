-- Copyright (C) 2022 - present Juergen Zimmermann, Hochschule Karlsruhe
--
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
--
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License
-- along with this program.  If not, see <https://www.gnu.org/licenses/>.

-- docker compose exec postgres bash
-- psql --dbname=verein --username=verein --file=/scripts/create.sql

-- https://www.postgresql.org/docs/devel/app-psql.html
-- https://www.postgresql.org/docs/current/ddl-schemas.html
-- https://www.postgresql.org/docs/current/ddl-schemas.html#DDL-SCHEMAS-CREATE
-- "user-private schema" (Default-Schema: public)
CREATE SCHEMA IF NOT EXISTS AUTHORIZATION verein;

ALTER ROLE verein SET search_path = 'verein';

-- https://www.postgresql.org/docs/current/sql-createtable.html
-- https://www.postgresql.org/docs/current/datatype.html
-- BEACHTE: user ist ein Schluesselwort
CREATE TABLE IF NOT EXISTS login (
             -- https://www.postgresql.org/docs/current/datatype-uuid.html
             -- https://www.postgresql.org/docs/current/ddl-constraints.html#DDL-CONSTRAINTS-PRIMARY-KEYS
             -- impliziter Index fuer Primary Key
    id       uuid PRIMARY KEY USING INDEX TABLESPACE vereinspace,
    username varchar(20) UNIQUE NOT NULL,
    password varchar(180) NOT NULL
) TABLESPACE vereinspace;

CREATE TABLE IF NOT EXISTS login_rollen (
             -- https://www.postgresql.org/docs/current/ddl-constraints.html#DDL-CONSTRAINTS-FK
    login_id uuid NOT NULL REFERENCES login,
             -- https://www.postgresql.org/docs/current/ddl-constraints.html#id-1.5.4.6.6
             -- https://www.postgresql.org/docs/current/functions-matching.html#FUNCTIONS-POSIX-REGEXP
    rolle    varchar(20) NOT NULL CHECK (rolle ~ 'ADMIN|VEREIN|ACTUATOR'),

    PRIMARY KEY (login_id, rolle) USING INDEX TABLESPACE vereinspace
) TABLESPACE vereinspace;

-- https://www.postgresql.org/docs/docs/sql-createindex.html
CREATE INDEX IF NOT EXISTS login_rollen_idx ON login_rollen(login_id) TABLESPACE vereinspace;

CREATE TABLE IF NOT EXISTS umsatz (
    id        uuid PRIMARY KEY USING INDEX TABLESPACE vereinspace,
              -- https://www.postgresql.org/docs/current/datatype-numeric.html#DATATYPE-NUMERIC-DECIMAL
              -- https://www.postgresql.org/docs/current/datatype-money.html
              -- 10 Stellen, davon 2 Nachkommastellen
    betrag    decimal(10,2) NOT NULL,
    waehrung  char(3) NOT NULL CHECK (waehrung ~ '[A-Z]{3}')
) TABLESPACE vereinspace;

CREATE TABLE IF NOT EXISTS adresse (
    id    uuid PRIMARY KEY USING INDEX TABLESPACE vereinspace,
    plz   char(5) NOT NULL CHECK (plz ~ '\d{5}'),
    ort   varchar(40) NOT NULL
) TABLESPACE vereinspace;

-- default: btree
CREATE INDEX IF NOT EXISTS adresse_plz_idx ON adresse(plz) TABLESPACE vereinspace;

CREATE TABLE IF NOT EXISTS verein (
    id            uuid PRIMARY KEY USING INDEX TABLESPACE vereinspace,
                  -- https://www.postgresql.org/docs/current/datatype-numeric.html#DATATYPE-INT
    version       integer NOT NULL DEFAULT 0,
    name      varchar(40) NOT NULL,
                  -- impliziter Index als B-Baum durch UNIQUE
                  -- https://www.postgresql.org/docs/current/ddl-constraints.html#DDL-CONSTRAINTS-UNIQUE-CONSTRAINTS
    email         varchar(40) NOT NULL UNIQUE USING INDEX TABLESPACE vereinspace,
                  -- https://www.postgresql.org/docs/current/ddl-constraints.html#DDL-CONSTRAINTS-CHECK-CONSTRAINTS
    gruendungsdatum  date CHECK (gruendungsdatum < current_date),
    umsatz_id     uuid REFERENCES umsatz,
    adresse_id    uuid NOT NULL REFERENCES adresse,
    username      varchar(20) NOT NULL REFERENCES login(username),
                  -- https://www.postgresql.org/docs/current/datatype-datetime.html
    erzeugt       timestamp NOT NULL,
    aktualisiert  timestamp NOT NULL
) TABLESPACE vereinspace;

CREATE INDEX IF NOT EXISTS verein_name_idx ON verein(name) TABLESPACE vereinspace;

CREATE TABLE IF NOT EXISTS verein_interessen (
    verein_id  uuid NOT NULL REFERENCES verein,
    interesse char(1) NOT NULL CHECK (interesse ~ 'S|L|R'),

    PRIMARY KEY (verein_id, interesse) USING INDEX TABLESPACE vereinspace
) TABLESPACE vereinspace;

CREATE INDEX IF NOT EXISTS verein_interessen_verein_idx ON verein_interessen(verein_id) TABLESPACE vereinspace;
