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

    AVATAR_SELECTED,

    CONFIG,

    GAME_STATE,
    GAME_OVER,

    ERROR
}