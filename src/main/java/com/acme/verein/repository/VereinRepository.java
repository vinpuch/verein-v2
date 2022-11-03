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
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
import java.util.Objects;
import java.util.Optional;
//import java.util.OptionalInt;
import java.util.UUID;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;

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
     * Alle Vereine als Collection ermitteln, wie sie später auch von der DB kommen.
     *
     * @return Alle Vereine
     */
    public @NonNull Collection<Verein> findAll() {
        return VEREINE;
    }



}
