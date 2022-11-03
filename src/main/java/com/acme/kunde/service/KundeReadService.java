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
package com.acme.kunde.service;

import com.acme.kunde.entity.Kunde;
import com.acme.kunde.repository.KundeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
//import java.util.List;
//import java.util.Map;
import java.util.UUID;

/**
 * Anwendungslogik für Kunden.
 * <img src="../../../../../asciidoc/KundeReadService.svg" alt="Klassendiagramm">
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public final class KundeReadService {
    private final KundeRepository repo;

    /**
     * Einen Kunden anhand seiner ID suchen.
     *
     * @param id Die Id des gesuchten Kunden
     * @return Der gefundene Kunde
     * @throws NotFoundException Falls kein Kunde gefunden wurde
     */
    public @NonNull Kunde findById(final UUID id) {
        log.debug("findById: id={}", id);
        final var kunde = repo.findById(id)
            .orElseThrow(() -> new NotFoundException(id));
        log.debug("findById: {}", kunde);
        return kunde;
    }

    public Collection<Kunde> findAll() {
// Implementierung des Use Case
// abschliessender DB-Zugriff
        return repo.findAll();
    }
}
