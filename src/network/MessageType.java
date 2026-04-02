package network;

public enum MessageType {
    LOGIN,
    REGISTER,
    LOGIN_OK,
    LOGIN_FAIL,

    MATCH_REQUEST,
    MATCH_FOUND,

    MAP_CHOICE,
    MAP_SELECTED,

    GAME_STATE,
    GAME_OVER,

    CONFIG,
    PING,
    PONG,
    ERROR
}