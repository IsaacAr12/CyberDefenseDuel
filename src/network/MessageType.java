package network;

public enum MessageType {
    REGISTER,
    REGISTER_OK,
    REGISTER_FAIL,
    LOGIN,
    LOGIN_OK,
    LOGIN_FAIL,
    MATCH_REQUEST,
    MATCH_WAITING,
    MATCH_FOUND,
    CONFIG,
    GAME_STATE,
    GAME_OVER,
    ERROR
}