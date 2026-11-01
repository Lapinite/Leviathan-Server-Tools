package com.leviathan.servertools.clientrules;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * What to turn off for players in one place, and how it goes over the wire.
 *
 * The client only ever turns things off, so an empty rule set means "nothing
 * off": sending it lifts earlier rules.
 */
public final class RuleSet {
    public static final int MAX_ENTRIES = 64;
    public static final int MAX_BYTES = 4096;
    public static final RuleSet NONE = new RuleSet(Collections.<String>emptySet());

    private final Set<String> disabled;

    private RuleSet(Set<String> disabled) {
        this.disabled = Collections.unmodifiableSet(disabled);
    }

    /** Builds a rule set from config entries. Unknown names are returned in {@code unknown}, not kept. */
    public static RuleSet of(List<?> entries, List<String> unknown) {
        Set<String> out = new LinkedHashSet<>();
        if (entries != null) {
            for (Object entry : entries) {
                String name = RuleNames.canonical(entry == null ? null : String.valueOf(entry));
                if (name == null) {
                    if (unknown != null) unknown.add(String.valueOf(entry));
                } else if (out.size() < MAX_ENTRIES) {
                    out.add(name);
                }
            }
        }
        return out.isEmpty() ? NONE : new RuleSet(out);
    }

    public Set<String> disabled() {
        return disabled;
    }

    public boolean isEmpty() {
        return disabled.isEmpty();
    }

    /** {"v":1,"disable":[...]} — names are from a fixed list, so no escaping is ever needed. */
    public String json() {
        StringBuilder sb = new StringBuilder("{\"v\":1,\"disable\":[");
        boolean first = true;
        for (String name : disabled) {
            if (!first) sb.append(',');
            sb.append('"').append(name).append('"');
            first = false;
        }
        return sb.append("]}").toString();
    }

    /** The plugin-message payload: a Minecraft VarInt byte length, then the UTF-8 JSON. */
    public byte[] payload() {
        byte[] json = json().getBytes(StandardCharsets.UTF_8);
        if (json.length > MAX_BYTES) throw new IllegalStateException("Rules exceed " + MAX_BYTES + " bytes");
        ByteArrayOutputStream out = new ByteArrayOutputStream(json.length + 5);
        writeVarInt(out, json.length);
        out.write(json, 0, json.length);
        return out.toByteArray();
    }

    /** A Minecraft VarInt: 7 bits per byte, high bit set when more bytes follow. */
    static void writeVarInt(ByteArrayOutputStream out, int value) {
        int v = value;
        while ((v & ~0x7F) != 0) {
            out.write((v & 0x7F) | 0x80);
            v >>>= 7;
        }
        out.write(v);
    }

    /** Reads the protocol version a client sent on leviathan:hello, or -1 if malformed. */
    public static int readHello(byte[] data) {
        if (data == null || data.length == 0 || data.length > 5) return -1;
        int value = 0;
        for (int i = 0; i < data.length; i++) {
            int b = data[i] & 0xFF;
            value |= (b & 0x7F) << (7 * i);
            if ((b & 0x80) == 0) return i == data.length - 1 ? value : -1;
        }
        return -1;
    }

    /** "Zoom, HUD:Coordinates"-style list for staff messages. */
    public List<String> names() {
        return new ArrayList<>(disabled);
    }
}
