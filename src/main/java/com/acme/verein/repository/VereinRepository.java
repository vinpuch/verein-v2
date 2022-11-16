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
package com.acme.verein.repository;

import com.acme.verein.entity.Verein;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.acme.verein.repository.DB.VEREINE;
import static java.util.UUID.randomUUID;
// import static java.util.UUID.randomUUID;

/**
 * Repository für den DB-Zugriff bei Verein.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Repository
@Slf4j
@SuppressWarnings("PublicConstructor")
public final class VereinRepository {
    /**
     * Ein Verein anhand seiner ID suchen.
     *
     * @param id Die Id des gesuchten Vereins
     * @return Optional mit dem gefundenen Verein oder leeres Optional
     */
    public Optional<Verein> findById(final UUID id) {
        log.debug("findById: id={}", id);
        final var result = VEREINE.stream()
            .filter(verein -> Objects.equals(verein.getId(), id))
            .findFirst();
        log.debug("findById: {}", result);
        return result;
    }

    /**
     * Vereine anhand von Suchkriterien ermitteln.
     * Z.B. mit GET https://localhost:8080/api?name=A&amp;plz=7
     *
     * @param suchkriterien Suchkriterien.
     * @return Gefundene Vereine.
     */
    @SuppressWarnings({"ReturnCount", "JavadocLinkAsPlainText"})
    public @NonNull Collection<Verein> find(final Map<String, String> suchkriterien) {
        log.debug("find: suchkriterien={}", suchkriterien);

        if (suchkriterien.isEmpty()) {
            return findAll();
        }

        // for-Schleife statt Higher-order Function "forEach" wegen return
        for (final var entry : suchkriterien.entrySet()) {
            if (entry.getKey().equals("name")) {
                return findByName(entry.getValue());
            } else {
                log.debug("find: ungueltiges Suchkriterium={}", entry.getKey());
            }
        }

        return Collections.emptyList();
    }

    /**
     * Alle Vereine als Collection ermitteln, wie sie später auch von der DB kommen.
     *
     * @return Alle Vereine
     */
    public @NonNull List<Verein> findAll() {
        return VEREINE;
    }

    /**
     * Vereine anhand des namens suchen.
     *
     * @param name Der (Teil-) Name der gesuchten Vereine
     * @return Die gefundenen Vereine oder eine leere Collection
     */
    public @NonNull Collection<Verein> findByName(final CharSequence name) {
        log.debug("findByName: name={}", name);
        final var vereine = VEREINE.stream()
            .filter(verein -> verein.getName().contains(name))
            .collect(Collectors.toList());
        log.debug("findByName: vereine={}", vereine);
        return vereine;
    }

    /**
     * Einen neuen Verein anlegen.
     *
     * @param verein Das Objekt des neu anzulegenden Vereins.
     * @return Das neu angelegte Verein mit generierter ID.
     */
    public @NonNull Verein create(final @NonNull Verein verein) {
        log.debug("create: {}", verein);
        verein.setId(randomUUID());
        VEREINE.add(verein);
        log.debug("create: {}", verein);
        return verein;
    }

    /**
     * Einen vorhandenen Verein aktualisieren.
     *
     * @param verein Das Objekt mit den neuen Daten
     */
    public void update(final @NonNull Verein verein) {
        log.debug("update: {}", verein);
        final OptionalInt index = IntStream
            .range(0, VEREINE.size())
            .filter(i -> Objects.equals(VEREINE.get(i).getId(), verein.getId()))
            .findFirst();
        log.trace("update: index={}", index);
        if (index.isEmpty()) {
            return;
        }
        VEREINE.set(index.getAsInt(), verein);
        log.debug("update: {}", verein);
    }

    public Optional<Verein> findByEmail(final String email) {
        log.debug("findByEmail: {}", email);
        final var result = VEREINE.stream()
            .filter(verein -> Objects.equals(verein.getEmail(), email))
            .findFirst();
        log.debug("findByEmail: {}", result);
        return result;
    }
}
