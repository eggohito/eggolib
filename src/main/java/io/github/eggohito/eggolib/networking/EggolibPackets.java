package io.github.eggohito.eggolib.networking;

import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.util.Identifier;

public class EggolibPackets {

    public static Identifier CLOSE_SCREEN_CLIENT = Eggolib.identifier("close_screen_client");

    public static Identifier CHANGE_PERSPECTIVE_CLIENT = Eggolib.identifier("change_perspective_client");


    public static Identifier CHECK_SCREEN_CLIENT = Eggolib.identifier("get_screen_client");

    public static Identifier CHECK_PERSPECTIVE_CLIENT = Eggolib.identifier("get_perspective_client");

    public static Identifier CHECK_SCREEN_SERVER = Eggolib.identifier("get_screen_server");

    public static Identifier CHECK_PERSPECTIVE_SERVER = Eggolib.identifier("get_perspective_server");

}
