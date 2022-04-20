package io.github.eggohito.eggolib.networking;

import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.util.Identifier;

public class EggolibPackets {

    public static Identifier CLOSE_SCREEN_CLIENT = Eggolib.identifier("close_screen_client");

    public static Identifier SET_PERSPECTIVE_CLIENT = Eggolib.identifier("set_perspective_client");

    public static Identifier SEND_CURRENT_SCREEN_SERVER = Eggolib.identifier("send_current_screen_server");

    public static Identifier SEND_CURRENT_PERSPECTIVE_SERVER = Eggolib.identifier("send_current_perspective_server");

}
