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
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * Anwendungslogik für Vereine auch mit Bean Validation.
 * ![Klassendiagramm](../../../images/VereinWriteService.svg)
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public final class VereinWriteService {
    private final VereinRepository repo;

    // https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#validation-beanvalidation
    private final Validator validator;

    /**
     * Einen neuen Verein anlegen.
     *
     * @param verein Das Objekt des neu anzulegenden Verein.
     * @return Der neu angelegte Verein mit generierter ID
     * @throws com.acme.verein.service.ConstraintViolationsException Falls mindestens ein Constraint verletzt ist.
     * @throws com.acme.verein.service.EmailExistsException Es gibt bereits einen Verein mit der Emailadresse.
     */
    public Verein create(@Valid final Verein verein) {
        log.debug("create: {}", verein);

        final var violations = validator.validate(verein);
        if (!violations.isEmpty()) {
            log.debug("create: violations={}", violations);
            throw new com.acme.verein.service.ConstraintViolationsException(violations);
        }

        if (repo.isEmailExisting(verein.getEmail())) {
            throw new com.acme.verein.service.EmailExistsException(verein.getEmail());
        }

        final var vereinDB = repo.create(verein);
        log.debug("create: {}", vereinDB);
        return vereinDB;
    }

    /**
     * Einen vorhandenen Verein aktualisieren.
     *
     * @param verein Das Objekt mit den neuen Daten (ohne ID)
     * @param id ID des zu aktualisierenden Vereins
     * @throws com.acme.verein.service.ConstraintViolationsException Falls mindestens ein Constraint verletzt ist.
     * @throws NotFoundException Kein Verein zur ID vorhanden.
     * @throws com.acme.verein.service.EmailExistsException Es gibt bereits einen Verein mit der Emailadresse.
     */
    public void update(final Verein verein, final UUID id) {
        log.debug("update: {}", verein);
        log.debug("update: id={}", id);

        final var violations = validator.validate(verein);
        if (!violations.isEmpty()) {
            log.debug("update: violations={}", violations);
            throw new com.acme.verein.service.ConstraintViolationsException(violations);
        }

        final var vereinDbOptional = repo.findById(id);
        if (vereinDbOptional.isEmpty()) {
            throw new NotFoundException(id);
        }

        final var email = verein.getEmail();
        final var vereinDb = vereinDbOptional.get();
        // Ist die neue Email bei einem *ANDEREN* Verein vorhanden?
        if (!Objects.equals(email, vereinDb.getEmail()) && repo.isEmailExisting(email)) {
            log.debug("update: email {} existiert", email);
            throw new com.acme.verein.service.EmailExistsException(email);
        }

        verein.setId(id);
        repo.update(verein);
    }

    /**
     * Einen vorhandenen Verein löschen.
     *
     * @param id Die ID des zu löschenden Vereins.
     */
    public void deleteById(final UUID id) {
        log.debug("deleteById: id={}", id);
        repo.deleteById(id);
    }
}
