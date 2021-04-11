package com.Avinadav.couponsystem.rest.login;

/**
 * - This class is part of the login process.
 * - That class create pojo that include the ID of the client and the time of the last access (use) on the system.
 * <p>
 * - Every 'ClientSession' pojo get a token ,that generate according to the TYPE of the client
 * (in the 'LoginController' class) .
 * <p>
 * Type of client:
 * Admin - 15
 * Company - 10
 * Customer - 5
 * -> The numbers describe the length of the token.
 * <p>
 * - The 'ClientSession' and the token stored in the 'tokenMap' that generate in the class 'RestConfiguration'.
 * <p>
 * - The idea of the 'tokensMap' is to secure the client by log out the client that don't use the server a certain time
 * (that diffing in the 'ClientSessionCleaner' -> 'clean()')
 */

public class ClientSession {

    private final long clientId;
    private long lastAccessMillis;


    private ClientSession(long clientId, long lastAccessMillis) {

        this.clientId = clientId;
        this.lastAccessMillis = lastAccessMillis;
    }


    public static ClientSession create(long clientId) {
        return new ClientSession(clientId, System.currentTimeMillis());
    }

    public long getClientId() {
        return clientId;
    }

    /*Update the last access of the client
     * -> use after every use of the client on the system.*/
    public void access() {
        this.lastAccessMillis = System.currentTimeMillis();
    }

    public long getLastAccessMillis() {
        return lastAccessMillis;
    }

    @Override
    public String toString() {
        return "ClientSession{" +
                "clientId=" + clientId +
                ", lastAccessMillis=" + lastAccessMillis +
                '}';
    }
}
