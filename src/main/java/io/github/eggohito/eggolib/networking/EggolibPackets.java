package io.github.eggohito.eggolib.networking;

import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.util.Identifier;

public class EggolibPackets {

    public static Identifier CLOSE_SCREEN_CLIENT = Eggolib.identifier("close_screen_client");

    public static Identifier SET_PERSPECTIVE_CLIENT = Eggolib.identifier("set_perspective_client");


    public static Identifier GET_CURRENT_SCREEN_CLIENT = Eggolib.identifier("get_current_screen_client");

    public static Identifier SYNC_CURRENT_SCREEN_SERVER = Eggolib.identifier("sync_current_screen_server");

    public static Identifier GET_CURRENT_PERSPECTIVE_CLIENT = Eggolib.identifier("get_current_perspective_client");

    public static Identifier SYNC_CURRENT_PERSPECTIVE_SERVER = Eggolib.identifier("sync_current_perspective_server");

}
