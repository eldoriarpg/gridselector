/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config;

import com.fasterxml.jackson.annotation.JsonSetter;
import de.eldoria.gridselector.config.elements.General;
import de.eldoria.gridselector.config.elements.Highlight;
import de.eldoria.gridselector.config.elements.UpdateSettings;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class ConfigFile {
    private UpdateSettings updates = new UpdateSettings();
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

    /**
     * @return whether the server is told about new versions and whether they are downloaded
     */
    public UpdateSettings updates() {
        return updates;
    }

    /**
     * Reads the update switch of configs written before the update settings existed. It becomes the start value of
     * {@link UpdateSettings#notice()} and is not written again.
     *
     * @param checkUpdates whether the old config looked for new versions
     */
    @JsonSetter("checkUpdates")
    private void legacyCheckUpdates(boolean checkUpdates) {
        updates.notice(checkUpdates);
    }
}
