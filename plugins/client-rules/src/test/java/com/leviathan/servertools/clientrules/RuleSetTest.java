package com.leviathan.servertools.clientrules;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RuleSetTest {
    @Test
    void buildsTheJsonTheClientReads() {
        List<String> unknown = new ArrayList<>();
        RuleSet rules = RuleSet.of(Arrays.asList("zoom", " HUD:Coordinates ", "FLY", "HUD:XRAY", "ZOOM", null), unknown);
        assertEquals("{\"v\":1,\"disable\":[\"ZOOM\",\"HUD:COORDINATES\"]}", rules.json());
        assertEquals(Arrays.asList("FLY", "HUD:XRAY", "null"), unknown, "unknown names are reported, not sent");
    }

    @Test
    void emptyMeansNothingOffAndLiftsEarlierRules() {
        assertTrue(RuleSet.of(Collections.emptyList(), null).isEmpty());
        assertTrue(RuleSet.of(null, null).isEmpty());
        assertEquals("{\"v\":1,\"disable\":[]}", RuleSet.NONE.json());
    }

    @Test
    void payloadIsAVarIntLengthThenUtf8() {
        RuleSet rules = RuleSet.of(Arrays.asList("ZOOM"), null);
        byte[] json = rules.json().getBytes(StandardCharsets.UTF_8);
        byte[] payload = rules.payload();
        assertEquals(json.length, payload[0], "short lengths fit in one byte");
        assertArrayEquals(json, Arrays.copyOfRange(payload, 1, payload.length));
    }

    @Test
    void varIntMatchesMinecraft() {
        assertArrayEquals(new byte[] {0x00}, varInt(0));
        assertArrayEquals(new byte[] {0x7F}, varInt(127));
        assertArrayEquals(new byte[] {(byte) 0x80, 0x01}, varInt(128));
        assertArrayEquals(new byte[] {(byte) 0xFF, 0x7F}, varInt(16383));
        assertArrayEquals(new byte[] {(byte) 0x80, 0x20}, varInt(4096));
    }

    @Test
    void readsTheClientHello() {
        assertEquals(1, RuleSet.readHello(new byte[] {0x01}));
        assertEquals(300, RuleSet.readHello(new byte[] {(byte) 0xAC, 0x02}));
        assertEquals(-1, RuleSet.readHello(new byte[0]));
        assertEquals(-1, RuleSet.readHello(null));
        assertEquals(-1, RuleSet.readHello(new byte[] {(byte) 0x80}), "unfinished");
        assertEquals(-1, RuleSet.readHello(new byte[] {0x01, 0x02}), "trailing bytes");
        assertEquals(-1, RuleSet.readHello(new byte[6]), "too long");
    }

    @Test
    void entriesAreBoundedAndTheJsonStaysSmall() {
        List<String> many = new ArrayList<>();
        for (int i = 0; i < 500; i++) many.add(i % 2 == 0 ? "ZOOM" : "HUD:POTIONS");
        RuleSet rules = RuleSet.of(many, null);
        assertEquals(2, rules.disabled().size(), "duplicates collapse");
        List<String> all = new ArrayList<>(RuleNames.MODULES);
        for (String w : RuleNames.WIDGETS) all.add("HUD:" + w);
        byte[] payload = RuleSet.of(all, null).payload();
        assertTrue(payload.length < RuleSet.MAX_BYTES, "every name at once fits");
    }

    private static byte[] varInt(int v) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        RuleSet.writeVarInt(out, v);
        return out.toByteArray();
    }
}
