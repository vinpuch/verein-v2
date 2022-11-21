/*
 * Copyright (C) 2022 - present Juergen Zimmermann, Hochschule Karlsruhe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.acme.kunde.graphql;

import com.acme.kunde.entity.Kunde;
import com.acme.kunde.service.KundeReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.UUID;

/**
 * Eine Controller-Klasse für das Lesen mit der GraphQL-Schnittstelle und den Typen aus dem GraphQL-Schema.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Controller
@RequiredArgsConstructor
@Slf4j
final class KundeQueryController {
    private final KundeReadService service;

    /**
     * Suche anhand der Kunde-ID.
     *
     * @param id ID des zu suchenden Kunden
     * @return Der gefundene Kunde
     */
    @QueryMapping
    Kunde kunde(@Argument final UUID id) {
        log.debug("kunde: id={}", id);
        final var kunde = service.findById(id);
        log.debug("kunde: {}", kunde);
        return kunde;
    }

    /**
     * Suche mit diversen Suchkriterien.
     *
     * @param input Suchkriterien und ihre Werte, z.B. `nachname` und `Alpha`
     * @return Die gefundenen Kunden als Collection
     */
    @QueryMapping
    Collection<Kunde> kunden(@Argument final Suchkriterien input) {
        log.debug("kunden: suchkriterien={}", input);
        final var kunden = service.find(input.toMap());
        log.debug("kunden: {}", kunden);
        return kunden;
    }
}
