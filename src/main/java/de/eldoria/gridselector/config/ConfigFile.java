/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config;

import de.eldoria.gridselector.config.elements.General;
import de.eldoria.gridselector.config.elements.Highlight;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class ConfigFile {
    private boolean checkUpdates = true;
    private General general = new General();
    private Highlight highlight = new Highlight();

    public General general() {
        return general;
    }

    public void general(General general) {
        this.general = general;
    }

    public Highlight highlight() {
        return highlight;
    }

    public void highlight(Highlight highlight) {
        this.highlight = highlight;
    }

    public boolean checkUpdates() {
        return checkUpdates;
    }
}
