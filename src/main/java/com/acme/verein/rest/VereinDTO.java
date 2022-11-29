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
package com.acme.verein.rest;

import com.acme.verein.entity.Adresse;
import com.acme.verein.entity.Verein;
import com.acme.verein.entity.Umsatz;

import java.net.URL;
import java.time.LocalDate;

/**
 * ValueObject für das Neuanlegen und Ändern eines neuen Vereins. Beim Lesen wird die Klasse VereinModel für die Ausgabe
 * verwendet.
 *
 * @param name            Gültiger Nachname eines Vereins, d.h. mit einem geeigneten Muster.
 * @param email           E-Mail eines Vereins.
 * @param gruendungsdatum Das Gruendungsdatum eines Vereins.
 * @param homepage        Die Homepage eines Vereins.
 * @param umsatz          Der Umsatz eines Vereins.
 * @param adresse         Die Adresse eines Vereins.
 */
@SuppressWarnings("RecordComponentNumber")
record VereinDTO(
    String name,
    String email,
    LocalDate gruendungsdatum,
    URL homepage,
    Umsatz umsatz,
    Adresse adresse
) {
    /**
     * Konvertierung in ein Objekt des Anwendungskerns.
     *
     * @return Vereinobjekt für den Anwendungskern
     */
    Verein toVerein() {
        return Verein
            .builder()
            .id(null)
            .name(name)
            .email(email)
            .gruendungsdatum(gruendungsdatum)
            .homepage(homepage)
            .umsatz(umsatz)
            .adresse(adresse)
            .build();
    }
}
