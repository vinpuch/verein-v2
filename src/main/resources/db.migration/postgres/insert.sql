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

--  docker compose exec postgres bash
--  psql --dbname=verein --username=verein --file=/scripts/insert.sql

INSERT INTO login (id, username, password)
VALUES ('30000000-0000-0000-0000-000000000000', 'admin',
        '{argon2id}$argon2id$v=19$m=16384,t=3,p=1$QHb5SxDhddjUiGboXTc9S9yCmRoPsBejIvW/dw50DKg$WXZDFJowwMX5xsOun2BT2R3hv2aA9TSpnx3hZ3px59sTW0ObtqBwX7Sem6ACdpycArUHfxmFfv9Z49e7I+TI/g');
INSERT INTO login (id, username, password)
VALUES ('30000000-0000-0000-0000-000000000001', 'alpha',
        '{argon2id}$argon2id$v=19$m=16384,t=3,p=1$QHb5SxDhddjUiGboXTc9S9yCmRoPsBejIvW/dw50DKg$WXZDFJowwMX5xsOun2BT2R3hv2aA9TSpnx3hZ3px59sTW0ObtqBwX7Sem6ACdpycArUHfxmFfv9Z49e7I+TI/g');
INSERT INTO login (id, username, password)
VALUES ('30000000-0000-0000-0000-000000000002', 'alpha2',
        '{argon2id}$argon2id$v=19$m=16384,t=3,p=1$QHb5SxDhddjUiGboXTc9S9yCmRoPsBejIvW/dw50DKg$WXZDFJowwMX5xsOun2BT2R3hv2aA9TSpnx3hZ3px59sTW0ObtqBwX7Sem6ACdpycArUHfxmFfv9Z49e7I+TI/g');
INSERT INTO login (id, username, password)
VALUES ('30000000-0000-0000-0000-000000000030', 'alpha3',
        '{argon2id}$argon2id$v=19$m=16384,t=3,p=1$QHb5SxDhddjUiGboXTc9S9yCmRoPsBejIvW/dw50DKg$WXZDFJowwMX5xsOun2BT2R3hv2aA9TSpnx3hZ3px59sTW0ObtqBwX7Sem6ACdpycArUHfxmFfv9Z49e7I+TI/g');
INSERT INTO login (id, username, password)
VALUES ('30000000-0000-0000-0000-000000000040', 'delta',
        '{argon2id}$argon2id$v=19$m=16384,t=3,p=1$QHb5SxDhddjUiGboXTc9S9yCmRoPsBejIvW/dw50DKg$WXZDFJowwMX5xsOun2BT2R3hv2aA9TSpnx3hZ3px59sTW0ObtqBwX7Sem6ACdpycArUHfxmFfv9Z49e7I+TI/g');
INSERT INTO login (id, username, password)
VALUES ('30000000-0000-0000-0000-000000000050', 'epsilon',
        '{argon2id}$argon2id$v=19$m=16384,t=3,p=1$QHb5SxDhddjUiGboXTc9S9yCmRoPsBejIvW/dw50DKg$WXZDFJowwMX5xsOun2BT2R3hv2aA9TSpnx3hZ3px59sTW0ObtqBwX7Sem6ACdpycArUHfxmFfv9Z49e7I+TI/g');
INSERT INTO login (id, username, password)
VALUES ('30000000-0000-0000-0000-000000000060', 'phi',
        '{argon2id}$argon2id$v=19$m=16384,t=3,p=1$QHb5SxDhddjUiGboXTc9S9yCmRoPsBejIvW/dw50DKg$WXZDFJowwMX5xsOun2BT2R3hv2aA9TSpnx3hZ3px59sTW0ObtqBwX7Sem6ACdpycArUHfxmFfv9Z49e7I+TI/g');

INSERT INTO login_rollen (login_id, rolle)
VALUES ('30000000-0000-0000-0000-000000000000', 'ADMIN');
INSERT INTO login_rollen (login_id, rolle)
VALUES ('30000000-0000-0000-0000-000000000000', 'VEREIN');
INSERT INTO login_rollen (login_id, rolle)
VALUES ('30000000-0000-0000-0000-000000000000', 'ACTUATOR');
INSERT INTO login_rollen (login_id, rolle)
VALUES ('30000000-0000-0000-0000-000000000001', 'VEREIN');
INSERT INTO login_rollen (login_id, rolle)
VALUES ('30000000-0000-0000-0000-000000000002', 'VEREIN');
INSERT INTO login_rollen (login_id, rolle)
VALUES ('30000000-0000-0000-0000-000000000030', 'VEREIN');
INSERT INTO login_rollen (login_id, rolle)
VALUES ('30000000-0000-0000-0000-000000000040', 'VEREIN');
INSERT INTO login_rollen (login_id, rolle)
VALUES ('30000000-0000-0000-0000-000000000050', 'VEREIN');
INSERT INTO login_rollen (login_id, rolle)
VALUES ('30000000-0000-0000-0000-000000000060', 'VEREIN');

INSERT INTO umsatz (id, betrag, waehrung)
VALUES ('10000000-0000-0000-0000-000000000000', 0, 'EUR');
INSERT INTO umsatz (id, betrag, waehrung)
VALUES ('10000000-0000-0000-0000-000000000001', 10, 'EUR');
INSERT INTO umsatz (id, betrag, waehrung)
VALUES ('10000000-0000-0000-0000-000000000002', 20, 'USD');
INSERT INTO umsatz (id, betrag, waehrung)
VALUES ('10000000-0000-0000-0000-000000000030', 30, 'CHF');
INSERT INTO umsatz (id, betrag, waehrung)
VALUES ('10000000-0000-0000-0000-000000000040', 40, 'GBP');

INSERT INTO adresse (id, plz, ort)
VALUES ('20000000-0000-0000-0000-000000000000', '00000', 'Aachen');
INSERT INTO adresse (id, plz, ort)
VALUES ('20000000-0000-0000-0000-000000000001', '11111', 'Augsburg');
INSERT INTO adresse (id, plz, ort)
VALUES ('20000000-0000-0000-0000-000000000002', '22222', 'Aalen');
INSERT INTO adresse (id, plz, ort)
VALUES ('20000000-0000-0000-0000-000000000030', '33333', 'Ahlen');
INSERT INTO adresse (id, plz, ort)
VALUES ('20000000-0000-0000-0000-000000000040', '44444', 'Dortmund');
INSERT INTO adresse (id, plz, ort)
VALUES ('20000000-0000-0000-0000-000000000050', '55555', 'Essen');
INSERT INTO adresse (id, plz, ort)
VALUES ('20000000-0000-0000-0000-000000000060', '66666', 'Freiburg');

-- admin
INSERT INTO verein (id, version, name, email, gruendungsdatum, homepage, umsatz_id, adresse_id, username, erzeugt,
                    aktualisiert)
VALUES ('00000000-0000-0000-0000-000000000000', 0, 'Admin', 'admin@acme.com','2022-01-31',
        'https://www.acme.com',  '10000000-0000-0000-0000-000000000000',
        '20000000-0000-0000-0000-000000000000', 'admin', '2022-01-31 00:00:00', '2022-01-31 00:00:00');
-- HTTP GET
INSERT INTO verein (id, version, name, email, gruendungsdatum, homepage, umsatz_id, adresse_id, username, erzeugt,
                    aktualisiert)
VALUES ('00000000-0000-0000-0000-000000000001', 0, 'Alpha', 'alpha@acme.de',  '2022-01-01',
        'https://www.acme.de',  '10000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000001',
        'alpha', '2022-01-01 00:00:00', '2022-01-01 00:00:00');
INSERT INTO verein (id, version, name, email, gruendungsdatum, homepage, umsatz_id, adresse_id, username, erzeugt,
                    aktualisiert)
VALUES ('00000000-0000-0000-0000-000000000002', 0, 'Alpha', 'alpha@acme.edu', '2022-01-02',
        'https://www.acme.edu', '10000000-0000-0000-0000-000000000002',
        '20000000-0000-0000-0000-000000000002', 'alpha2', '2022-01-02 00:00:00', '2022-01-02 00:00:00');
-- HTTP PUT
INSERT INTO verein (id, version, name, email, gruendungsdatum, homepage, umsatz_id, adresse_id, username, erzeugt,
                    aktualisiert)
VALUES ('00000000-0000-0000-0000-000000000030', 0, 'Alpha', 'alpha@acme.ch', '2022-01-03',
        'https://www.acme.ch', '10000000-0000-0000-0000-000000000030',
        '20000000-0000-0000-0000-000000000030', 'alpha3', '2022-01-03 00:00:00', '2022-01-03 00:00:00');
-- HTTP PATCH
INSERT INTO verein (id, version, name, email, gruendungsdatum, homepage, umsatz_id, adresse_id, username, erzeugt,
                    aktualisiert)
VALUES ('00000000-0000-0000-0000-000000000040', 0, 'Delta', 'delta@acme.uk',  '2022-01-04',
        'https://www.acme.uk',  '10000000-0000-0000-0000-000000000040',
        '20000000-0000-0000-0000-000000000040', 'delta', '2022-01-04 00:00:00', '2022-01-04 00:00:00');
-- HTTP DELETE
INSERT INTO verein (id, version, name, email, gruendungsdatum, homepage, umsatz_id, adresse_id, username, erzeugt,
                    aktualisiert)
VALUES ('00000000-0000-0000-0000-000000000050', 0, 'Epsilon', 'epsilon@acme.jp', '2022-01-05',
        'https://www.acme.jp', null, '20000000-0000-0000-0000-000000000050', 'epsilon', '2022-01-05 00:00:00',
        '2022-01-05 00:00:00');
-- zur freien Verfuegung
INSERT INTO verein (id, version, name, email, gruendungsdatum, homepage, umsatz_id, adresse_id, username, erzeugt,
                    aktualisiert)
VALUES ('00000000-0000-0000-0000-000000000060', 0, 'Phi', 'phi@acme.cn',  '2022-01-06', 'https://www.acme.cn',
         '20000000-0000-0000-0000-000000000060', 'phi', '2022-01-06 00:00:00', '2022-01-06 00:00:00');
