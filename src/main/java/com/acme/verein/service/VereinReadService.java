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
package com.acme.verein.service;

import com.acme.verein.entity.Verein;
import com.acme.verein.repository.VereinRepository;
import com.acme.verein.repository.SpecBuilder;
import com.acme.verein.security.Rolle;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Anwendungslogik für Vereine.
 * <img src="../../../../../asciidoc/VereinReadService.svg" alt="Klassendiagramm">
 * Schreiboperationen werden mit Transaktionen durchgeführt und Lese-Operationen mit Readonly-Transaktionen:
 * <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#transactions">siehe Dokumentation</a>.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#transactions
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class VereinReadService {
    private final VereinRepository repo;
    private final SpecBuilder specBuilder;

    /**
     * Einen Vereine anhand seiner ID suchen.
     *
     * @param id Die Id des gesuchten Vereine
     * @return Der gefundene Verein
     * @throws NotFoundException Falls kein Verein gefunden wurde
     */
    public @NonNull Verein findById(final UUID id) {
        log.debug("findById: id={}", id);
        final var vereinOpt = repo.findById(id);

        // admin: Vereinndaten evtl. nicht gefunden
        final var verein = vereinOpt.orElseThrow(() -> new NotFoundException(id));
        log.debug("findById: {}", verein);
        return verein;
    }

    /**
     * Vereine anhand von Suchkriterien als Collection suchen.
     *
     * @param suchkriterien Die Suchkriterien
     * @return Die gefundenen Vereine oder eine leere Liste
     * @throws NotFoundException Falls keine Vereine gefunden wurden
     */
    @SuppressWarnings({"ReturnCount", "NestedIfDepth", "CyclomaticComplexity"})
    public @NonNull Collection<Verein> find(@NonNull final Map<String, List<String>> suchkriterien) {
        log.debug("find: suchkriterien={}", suchkriterien);

        if (suchkriterien.isEmpty()) {
            return repo.findAll();
        }

        if (suchkriterien.size() == 1) {
            final var namen = suchkriterien.get("name");
            if (namen != null && namen.size() == 1) {
                final var vereine = repo.findByName(namen.get(0));
                if (vereine.isEmpty()) {
                    throw new NotFoundException(suchkriterien);
                }
                log.debug("find (name): {}", vereine);
                return vereine;
            }

            final var emails = suchkriterien.get("email");
            if (emails != null && emails.size() == 1) {
                final var verein = repo.findByEmail(emails.get(0));
                if (verein.isEmpty()) {
                    throw new NotFoundException(suchkriterien);
                }
                final var vereine = List.of(verein.get());
                log.debug("find (email): {}", vereine);
                return vereine;
            }
        }

        final var spec = specBuilder
            .build(suchkriterien)
            .orElseThrow(() -> new NotFoundException(suchkriterien));
        final var vereine = repo.findAll(spec);
        if (vereine.isEmpty()) {
            throw new NotFoundException(suchkriterien);
        }
        log.debug("find: {}", vereine);
        return vereine;
    }

    /**
     * Abfrage, welche Namen es zu einem Präfix gibt.
     *
     * @param prefix Name-Präfix.
     * @return Die passenden Namen.
     * @throws NotFoundException Falls keine Namen gefunden wurden.
     */
    public @NonNull Collection<String> findNamenByPrefix(final String prefix) {
        log.debug("findNamenByPrefix: {}", prefix);
        final var namen = repo.findNamenByPrefix(prefix);
        if (namen.isEmpty()) {
            throw new NotFoundException();
        }
        log.debug("findNamenByPrefix: {}", namen);
        return namen;
    }
}
