package com.leviathan.servertools.clientrules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RuleNamesTest {
    @Test
    void namesMatchTheClient() {
        assertEquals("ZOOM", RuleNames.canonical("zoom"));
        assertEquals("HUD:ARMOR_STATUS", RuleNames.canonical("hud:armor_status"));
        assertNull(RuleNames.canonical("HUD:"));
        assertNull(RuleNames.canonical("HUD_COORDINATES"));
        assertNull(RuleNames.canonical("COORDINATES"), "widgets need the HUD: prefix");
        assertNull(RuleNames.canonical(null));
        assertEquals(6, RuleNames.MODULES.size());
        assertEquals(10, RuleNames.WIDGETS.size());
    }
}
