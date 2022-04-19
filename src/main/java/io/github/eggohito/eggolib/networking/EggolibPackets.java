package io.github.eggohito.eggolib.networking;

import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.util.Identifier;

public class EggolibPackets {

    public static Identifier CLOSE_GUI_CLIENT = Eggolib.identifier("close_gui_client");

    public static Identifier SET_PERSPECTIVE_CLIENT = Eggolib.identifier("set_perspective_client");

    public static Identifier GET_CURRENT_SCREEN_SERVER = Eggolib.identifier("get_current_screen_server");

}
