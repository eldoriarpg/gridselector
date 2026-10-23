/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.config.elements.cluster;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PlotTest {
    private final Plot plot = Plot.of(0, 0, 10, 10);

    @Test
    void plotsSharingAnEdgeOverlap() {
        Assertions.assertTrue(plot.overlaps(Plot.of(10, 0, 20, 10)));
        Assertions.assertTrue(plot.overlaps(Plot.of(0, 10, 10, 20)));
        Assertions.assertTrue(plot.overlaps(Plot.of(10, 10, 20, 20)));
    }

    @Test
    void plotsNextToEachOtherDoNotOverlap() {
        Assertions.assertFalse(plot.overlaps(Plot.of(11, 0, 20, 10)));
        Assertions.assertFalse(plot.overlaps(Plot.of(-10, 0, -1, 10)));
        Assertions.assertFalse(plot.overlaps(Plot.of(0, 11, 10, 20)));
    }

    @Test
    void containedPlotOverlaps() {
        Assertions.assertTrue(plot.overlaps(Plot.of(2, 2, 3, 3)));
        Assertions.assertTrue(Plot.of(2, 2, 3, 3).overlaps(plot));
    }
}
