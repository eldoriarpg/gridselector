/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.updates;

import de.eldoria.eldoutilities.updater.AutoUpdateDecision;
import de.eldoria.gridselector.config.elements.UpdateSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdatesTest {
    @Test
    void turningAutoUpdateOnInChatIsKept() {
        var updates = new UpdateSettings();

        Updates.decide(updates, AutoUpdateDecision.ENABLED);

        assertTrue(updates.autoUpdate());
        assertTrue(updates.askForAutoUpdate());
    }

    @Test
    void neverAskingAgainIsKept() {
        var updates = new UpdateSettings();

        Updates.decide(updates, AutoUpdateDecision.NEVER_ASK);

        assertFalse(updates.askForAutoUpdate());
        assertFalse(updates.autoUpdate());
    }
}
