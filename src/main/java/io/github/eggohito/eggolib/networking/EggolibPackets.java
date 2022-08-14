package io.github.eggohito.eggolib.networking;

import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.util.Identifier;

public class EggolibPackets {

    public static final Identifier HANDSHAKE = Eggolib.identifier("handshake");

    public static final Identifier CLOSE_SCREEN = Eggolib.identifier("close_screen");

    public static final Identifier IS_IN_SCREEN = Eggolib.identifier("is_in_screen");

    public static final Identifier SET_PERSPECTIVE = Eggolib.identifier("set_perspective");

    public static final Identifier GET_PERSPECTIVE = Eggolib.identifier("get_perspective");

    public static final Identifier SYNC_KEY_PRESS = Eggolib.identifier("sync_key_press");

    public static final Identifier TRIGGER_KEY_SEQUENCE = Eggolib.identifier("trigger_key_sequence");

}
