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
package com.acme.verein.graphql;

import com.acme.verein.entity.Adresse;
import com.acme.verein.entity.FamilienstandType;
import com.acme.verein.entity.GeschlechtType;
import com.acme.verein.entity.InteresseType;
import com.acme.verein.entity.Verein;
import com.acme.verein.entity.Umsatz;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

/**
 * Eine Value-Klasse für Eingabedaten passend zu VereinInput aus dem GraphQL-Schema.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 * @param nachname Nachname
 * @param email Emailadresse
 * @param kategorie Kategorie
 * @param hasNewsletter Newsletter-Abonnement
 * @param geburtsdatum Geburtsdatum
 * @param homepage URL der Homepage
 * @param geschlecht Geschlecht
 * @param familienstand Familienstand
 * @param interessen Interessen als Liste
 * @param umsatz Umsatz
 * @param adresse Adresse
 */
@SuppressWarnings("RecordComponentNumber")
record VereinInput(
    String nachname,
    String email,
    int kategorie,
    boolean hasNewsletter,
    String geburtsdatum,
    URL homepage,
    UmsatzInput umsatz,
    AdresseInput adresse
) {
    /**
     * Konvertierung in ein Objekt der Entity-Klasse Verein.
     *
     * @return Das konvertierte Verein-Objekt
     */
    Verein toVerein() {
        final LocalDate geburtsdatumTmp;
        geburtsdatumTmp = LocalDate.parse(geburtsdatum);
        Umsatz umsatzTmp = null;
        if (umsatz != null) {
            umsatzTmp = Umsatz.builder().betrag(umsatz.betrag()).waehrung(umsatz.waehrung()).build();
        }
        final var adresseTmp = Adresse.builder().plz(adresse.plz()).ort(adresse.ort()).build();

        return Verein
            .builder()
            .id(null)
            .nachname(nachname)
            .email(email)
            .kategorie(kategorie)
            .hasNewsletter(hasNewsletter)
            .geburtsdatum(geburtsdatumTmp)
            .homepage(homepage)
            .geschlecht(geschlecht)
            .familienstand(familienstand)
            .interessen(interessen)
            .umsatz(umsatzTmp)
            .adresse(adresseTmp)
            .build();
    }
}
