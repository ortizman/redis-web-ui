package ar.com.engesoft.rediswebconsole.services;

import ar.com.engesoft.rediswebconsole.entities.ClientRedis;
import ar.com.engesoft.rediswebconsole.exceptions.ServiceException;
import org.redisson.api.RKeys;
import org.redisson.api.RType;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisClientService {

    @Autowired
    private RedisConnectionService connectionService;

    public Iterable<String> loadKeys(String connectionId) {
        return keys(connectionId).getKeys();
    }

    public Iterable<String> getKeys(String connectionId, int count) {
        return keys(connectionId).getKeys(count);
    }

    public Iterable<String> getKeysByPattern(String connectionId, String pattern, int count) {
        return keys(connectionId).getKeysByPattern(pattern, count);
    }
    public long delete(String connectionId, List<String> keys) {
        return keys(connectionId).delete(keys.toArray(new String[0]));
    }

    public long deleteByPattern(String connectionId, String pattern) {
        return keys(connectionId).deleteByPattern(pattern);
    }

    public long count(String connectionId) {
        return keys(connectionId).count();
    }

    private RKeys keys(String connectionId) {
        return connectionService
                .findById(connectionId)
                .getClient().getKeys();
    }

    public void expire(String connectionId, String key, long seconds) {
        final boolean expired = keys(connectionId).expire(key, seconds, TimeUnit.SECONDS);
        if (!expired) {
            throw new ServiceException("NOT_SET_EXPIRATION", "Could not set the expiration time to the key: " + key);
        }
    }

    public void clearExpire(String connectionId, String key) {
        final boolean expired = keys(connectionId).clearExpire(key);
        if (!expired) {
            throw new ServiceException("NOT_CLEAN_EXPIRATION", "Could not clean expiration time to the key: " + key);
        }
    }

    public RType getType(String connectionId, String key) {
        return keys(connectionId).getType(key);
    }

    public void migrate(String connectionId, String key, String connectionTarget) {
        final Config targetConfig = connectionService.findById(connectionTarget).getConfig();
        final String address = targetConfig.useSingleServer().getAddress();
        final int db = targetConfig.useSingleServer().getDatabase();
        int port = Integer.parseInt(address.split(":")[0]);

        keys(connectionId).migrate(key, address, port, db, 10_000);
    }
}
