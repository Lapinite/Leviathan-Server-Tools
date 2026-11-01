package com.leviathan.servertools.clientrules;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * The module and HUD widget names the Leviathan Client understands
 * (see Leviathan-Client docs/server-rules.md, protocol 1).
 */
public final class RuleNames {
    public static final Set<String> MODULES = Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(
            "HUD_OVERLAY", "FPS_COUNTER", "PING_DISPLAY", "ZOOM", "TOGGLE_SPRINT", "TOGGLE_SNEAK")));
    public static final Set<String> WIDGETS = Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(
            "FPS", "PING", "CPS", "COORDINATES", "CLOCK", "KEYSTROKES", "ARMOR_STATUS", "POTIONS",
            "SPRINT_STATE", "SESSION_TIME")));

    private RuleNames() {}

    /** The canonical name ("ZOOM", "HUD:COORDINATES"), or null if the client doesn't know it. */
    public static String canonical(String raw) {
        if (raw == null) return null;
        String name = raw.trim().toUpperCase(Locale.ROOT);
        if (name.startsWith("HUD:")) return WIDGETS.contains(name.substring(4)) ? name : null;
        return MODULES.contains(name) ? name : null;
    }
}
