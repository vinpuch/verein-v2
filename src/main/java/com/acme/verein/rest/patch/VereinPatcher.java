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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.acme.verein.rest.patch;

import com.acme.verein.entity.InteresseType;
import com.acme.verein.entity.Verein;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.acme.verein.rest.patch.PatchOperationType.ADD;
import static com.acme.verein.rest.patch.PatchOperationType.REMOVE;
import static com.acme.verein.rest.patch.PatchOperationType.REPLACE;

/**
 * Klasse, um PATCH-Operationen auf Verein-Objekte anzuwenden.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Component
@Slf4j
public final class VereinPatcher {
    VereinPatcher() {
    }

    /**
     * PATCH-Operationen werden auf ein Verein-Objekt angewandt.
     *
     * @param verein Das zu modifizierende Verein-Objekt.
     * @param operations Die anzuwendenden Operationen.
     * @throws InvalidPatchOperationException Falls die Patch-Operation nicht korrekt ist.
     */
    public void patch(final Verein verein, final Collection<PatchOperation> operations) {
        final var replaceOps = operations.stream()
            .filter(op -> op.op() == REPLACE)
            .collect(Collectors.toList());
        log.debug("patch: replaceOps={}", replaceOps);
        replaceOps(verein, replaceOps);

        final var addOps = operations.stream()
            .filter(op -> op.op() == ADD)
            .collect(Collectors.toList());
        log.debug("patch: addOps={}", addOps);
        addInteressen(verein, addOps);

        final var removeOps = operations.stream()
            .filter(op -> op.op() == REMOVE)
            .collect(Collectors.toList());
        log.debug("patch: removeOps={}", removeOps);
        removeInteressen(verein, removeOps);
    }

    private void replaceOps(final Verein verein, final Iterable<PatchOperation> ops) {
        ops.forEach(op -> {
            switch (op.path()) {
                case "/name" -> verein.setName(op.value());
                case "/email" -> verein.setEmail(op.value());
                default -> throw new InvalidPatchOperationException();
            }
        });
        log.trace("replaceOps: verein={}", verein);
    }

    private void addInteressen(final Verein verein, final Collection<PatchOperation> ops) {
        if (ops.isEmpty()) {
            return;
        }
        ops.stream()
            .filter(op -> Objects.equals("/interessen", op.path()))
            .forEach(op -> addInteresse(verein, op));
        log.trace("addInteressen: verein={}", verein);
    }

    private void addInteresse(final Verein verein, final PatchOperation op) {
        final var value = op.value();
        final var interesseOpt = InteresseType.fromValue(value);
        if (interesseOpt.isEmpty()) {
            throw new InvalidPatchOperationException();
        }
        final var interesse = interesseOpt.get();
        final var interessen = verein.getInteressen() == null
            ? new ArrayList<InteresseType>(InteresseType.values().length)
            : new ArrayList<>(verein.getInteressen());
        if (interessen.contains(interesse)) {
            throw new InvalidPatchOperationException();
        }
        interessen.add(interesse);
        log.trace("addInteresse: op={}, interessen={}", op, interessen);
        verein.setInteressen(interessen);
    }

    private void removeInteressen(final Verein verein, final Collection<PatchOperation> ops) {
        if (verein.getInteressen() == null) {
            throw new InvalidPatchOperationException();
        }
        if (ops.isEmpty()) {
            return;
        }
        ops.stream()
            .filter(op -> Objects.equals("/interessen", op.path()))
            .forEach(op -> removeInteresse(verein, op));
    }

    private void removeInteresse(final Verein verein, final PatchOperation op) {
        final var interesseValue = op.value();
        final var interesseRemoveOpt = InteresseType.fromValue(interesseValue);
        if (interesseRemoveOpt.isEmpty()) {
            throw new InvalidPatchOperationException();
        }
        final var interesseRemove = interesseRemoveOpt.get();
        final var interessen = verein.getInteressen()
            .stream()
            .filter(interesse -> interesse != interesseRemove)
            .collect(Collectors.toList());
        verein.setInteressen(interessen);
    }
}
