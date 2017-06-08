package es.uam.oscar_garcia.othello.model;

import java.util.List;

/**
 * Created by oscar on 1/03/17.
 */

public interface RoundRepository {
    void open() throws Exception;
    void close();
    interface LoginRegisterCallback {
        void onLogin(String playerUuid);
        void onError(String error);
    }
    void login(String playername, String password, LoginRegisterCallback callback);
    void register(String playername, String password, LoginRegisterCallback callback);
    interface BooleanCallback {
        void onResponse(boolean ok);
    }
    void getRounds(String playeruuid, String orderByField, String group,
                   RoundsCallback callback);
    void addRound(Round round, BooleanCallback callback);
    void updateRound(Round round, BooleanCallback callback);
    interface RoundsCallback {
        void onResponse(List<Round> rounds);
        void onError(String error);
    }
}