package com.Avinadav.couponsystem.schedued;

import com.Avinadav.couponsystem.rest.login.ClientSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * - This class respawn of the tokens map to be clean from user that don't use the server.
 * - The unused time will be set in the 'clean' method.
 */

@Component
public class ClientSessionCleaner {
    private Map<String, ClientSession> tokensMap;

    public ClientSessionCleaner() {
        /*Empty*/
    }

    @Autowired
    public ClientSessionCleaner(@Qualifier("tokens") Map<String, ClientSession> tokensMap) {
        this.tokensMap = tokensMap;
    }


    @Scheduled(cron = "* * * * * ? ")
    public void clean() {

        tokensMap.entrySet().stream()
                .filter(e -> {
                    long lastAccess = e.getValue().getLastAccessMillis();
                    return System.currentTimeMillis() - lastAccess >= 50_000;
                })
                .map(Map.Entry::getKey)
                .filter(Objects::nonNull)
                .iterator()
                .forEachRemaining(e -> tokensMap.remove(e));
    }

}


