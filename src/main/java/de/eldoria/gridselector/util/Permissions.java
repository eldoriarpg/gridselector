/*
 *     SPDX-License-Identifier: AGPL-3.0-only
 *
 *     Copyright (C) 2021 EldoriaRPG Team and Contributor
 */

package de.eldoria.gridselector.util;

public final class Permissions {
    private static final String BASE = "gridselector";
    public static final String USE = perm(BASE, "select");

    private Permissions() {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    private static String perm(String... perms) {
        return String.join(".", perms);
    }

    public static final class Save {
        public static final String EXPORT = perm(BASE, "export");
        public static final String GLOBAL = perm(EXPORT, "global");

        private Save() {
            throw new UnsupportedOperationException("This is a utility class.");
        }
    }

    public static final class Cluster {
        private static final String CLUSTER = perm(BASE, "cluster");
        public static final String CREATE = perm(CLUSTER, "create");
        public static final String REMOVE = perm(CLUSTER, "remove");
        public static final String REPAIR = perm(CLUSTER, "repair");

        private Cluster() {
            throw new UnsupportedOperationException("This is a utility class.");
        }
    }
}
