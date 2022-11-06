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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
//import java.util.List;
//import java.util.Map;
import java.util.UUID;

/**
 * Anwendungslogik für Verein.
 * <img src="../../../../../asciidoc/VereinReadService.svg" alt="Klassendiagramm">
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public final class VereinReadService {
    private final VereinRepository repo;

    /**
     * Einen Verein anhand seiner ID suchen.
     *
     * @param id Die Id des gesuchten Vereins
     * @return Der gefundene Verein
     * @throws NotFoundException Falls kein Verein gefunden wurde
     */
    public @NonNull Verein findById(final UUID id) {
        log.debug("findById: id={}", id);
        final var verein = repo.findById(id)
            .orElseThrow(() -> new NotFoundException(id));
        log.debug("findById: {}", verein);
        return verein;
    }

    /**
     *  Methode um alle Vereine zu finden.
     *
     *  @return findet alle Vereine
     */
    public Collection<Verein> findAll() {

        return repo.findAll();
    }
}
