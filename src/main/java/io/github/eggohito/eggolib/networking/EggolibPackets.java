package io.github.eggohito.eggolib.networking;

import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.util.Identifier;

public class EggolibPackets {

    public static class Client {

        public static Identifier SET_SCREEN = Eggolib.identifier("client.set_screen");

        public static Identifier SET_PERSPECTIVE = Eggolib.identifier("client.set_perspective");

        public static Identifier GET_SCREEN = Eggolib.identifier("client.get_screen");

        public static Identifier GET_PERSPECTIVE = Eggolib.identifier("client.get_perspective");

    }

    public static class Server {

        public static Identifier GET_SCREEN = Eggolib.identifier("server.get_screen");

        public static Identifier GET_PERSPECTIVE = Eggolib.identifier("server.get_perspective");

    }


}
