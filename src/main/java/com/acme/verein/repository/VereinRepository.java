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
import static java.util.UUID.randomUUID;

/*
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
*/
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
/*
import java.util.stream.Collectors;
import java.util.stream.IntStream;
*/

import static com.acme.verein.repository.DB.VEREINE;
//import static java.util.UUID.randomUUID;

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
     * Einen Verein anhand seiner ID suchen.
     *
     * @param id Die Id des gesuchten Vereins
     *
     * @return Optional mit dem gefundenen Vereis oder leeres Optional
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
     * Abfrage, ob es einen Verein mit gegebener Emailadresse gibt.
     *
     * @param email Emailadresse für die Suche
     * @return true, falls es einen solchen Verein gibt, sonst false
     */
    public boolean isEmailExisting(final String email) {
        log.debug("isEmailExisting: email={}", email);
        final var count = VEREINE.stream()
            .filter(verein -> Objects.equals(verein.getEmail(), email))
            .count();
        log.debug("isEmailExisting: count={}", count);
        return count > 0L;
    }

    /**
     * E-Mail wird gesucht.
     *
     * @param email des gesuchten Vereins
     *
     * @return Optional mit dem gefundenen Vereis oder leeres Optional
     */
    public Optional<Verein> findByEmail(final String email) {
        log.debug("findByEmail: {}", email);
        final var result = VEREINE.stream()
            .filter(verein -> Objects.equals(verein.getEmail(), email))
            .findFirst();
        log.debug("findByEmail: {}", result);
        return result;
    }

    /**
     * Einen vorhandenen Kunden aktualisieren.
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
    /**
     * Einen neuen Verein anlegen.
     *
     * @param verein Das Objekt des neu anzulegenden Kunden.
     * @return Der neu angelegte Verein mit generierter ID und kleingeschriebener Emailadresse
     */
    public @NonNull Verein create(final @NonNull Verein verein) {
        log.debug("create: {}", verein);
        verein.setId(randomUUID());
        VEREINE.add(verein);
        log.debug("create: {}", verein);
        return verein;
    }

    /**
     * Einen vorhandenen Verein löschen.
     *
     * @param id Die ID des zu löschenden Vereins.
     */
    public void deleteById(final UUID id) {
        log.debug("deleteById: id={}", id);
        final OptionalInt index = IntStream
            .range(0, VEREINE.size())
            .filter(i -> Objects.equals(VEREINE.get(i).getId(), id))
            .findFirst();
        log.trace("deleteById: index={}", index);
        index.ifPresent(VEREINE::remove);
        log.debug("deleteById: #KUNDEN={}", VEREINE.size());
    }
    /**
     * Name wird gesucht.
     *
     * @param name des gesuchten Vereins
     *
     * @return Collection mit dem gefundenen Vereinen
     */
    public @NonNull Collection<Verein> findByName(final CharSequence name) {
        log.debug("findByname: name={}", name);
        final var vereine = VEREINE.stream()
            .filter(verein -> verein.getName().contains(name))
            .collect(Collectors.toList());
        log.debug("findByName: vereine={}", vereine);
        return vereine;
    }


    /**
     * Alle Vereine als Collection ermitteln, wie sie später auch von der DB kommen.
     *
     * @return gibt alle Vereine zurück
     */
    public @NonNull Collection<Verein> findAll() {
        return VEREINE;
    }


    /**
     * Vereine anhand von Suchkriterien ermitteln.
     * Z.B. mit GET https://localhost:8080/api?name=A&amp;plz=7
     *
     *  @param suchkriterien Suchkriterien.
     *
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
            switch (entry.getKey()) {
                case "email" -> {
                    final var result = findByEmail(entry.getValue());
                    //noinspection OptionalIsPresent
                    return result.isPresent() ? List.of(result.get()) : Collections.emptyList();
                }
                case "name" -> {
                    return findByName(entry.getValue());
                }
                default -> log.debug("find: ungueltiges Suchkriterium={}", entry.getKey());
            }
        }

        return Collections.emptyList();
    }
}
