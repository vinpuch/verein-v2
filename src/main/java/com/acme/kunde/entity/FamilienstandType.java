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
package com.acme.kunde.entity;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum für Familienstand. Dazu kann auf der Clientseite z.B. ein Dropdown-Menü realisiert werden.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
public enum FamilienstandType {
    /**
    * _Ledig_ mit dem internen Wert `L` für z.B. das Mapping in einem JSON-Datensatz oder das Abspeichern in einer DB.
    */
    LEDIG("L"),

    /**
    * _Verheiratet_ mit dem internen Wert `VH` für z.B. das Mapping in einem JSON-Datensatz oder
    * das Abspeichern in einer DB.
    */
    VERHEIRATET("VH"),

    /**
    * _Geschieden_ mit dem internen Wert `G` für z.B. das Mapping in einem JSON-Datensatz oder
    * das Abspeichern in einer DB.
    */
    GESCHIEDEN("G"),

    /**
    * _Verwitwet_ mit dem internen Wert `VW` für z.B. das Mapping in einem JSON-Datensatz oder
    * das Abspeichern in einer DB.
    */
    VERWITWET("VW");

    private final String value;

    FamilienstandType(final String value) {
        this.value = value;
    }

    /**
    * Einen enum-Wert als String mit dem internen Wert ausgeben.
    * Dieser Wert wird durch Jackson in einem JSON-Datensatz verwendet.
    * [<a href="https://github.com/FasterXML/jackson-databind/wiki">Wiki-Seiten</a>]
    *
    * @return Der interne Wert.
    */
    @JsonValue
    @Override
    public String toString() {
        return value;
    }
}
