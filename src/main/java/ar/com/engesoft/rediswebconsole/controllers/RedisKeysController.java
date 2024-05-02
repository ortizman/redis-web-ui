package ar.com.engesoft.rediswebconsole.controllers;

import ar.com.engesoft.rediswebconsole.services.RedisClientService;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("${rest.context-path}/client/{connectionId}/keys")
public class RedisKeysController {

    @Autowired
    private RedisClientService redisClientService;

    @GetMapping
    public Iterable<String> getAllKeys(
            @PathVariable String connectionId,
            @RequestParam(name = "count", defaultValue = "20", required = false) int count) {
        return redisClientService.getKeys(connectionId, count);
    }

    @GetMapping("/scan/{pattern}")
    public Iterable<String> getKeysByPattern(
            @PathVariable String connectionId, @PathVariable String pattern,
            @RequestParam(name = "count", defaultValue = "20", required = false) int count) {
        return redisClientService.getKeysByPattern(connectionId, pattern, count);
    }

    @DeleteMapping("/{key}")
    public void deleteKey(@PathVariable String connectionId, @PathVariable String key) {
        redisClientService.delete(connectionId, List.of(key));
    }

    @DeleteMapping("/pattern/{pattern}")
    public void deleteByPattern(@PathVariable String connectionId, @PathVariable String pattern) {
        redisClientService.deleteByPattern(connectionId, pattern);
    }

    @GetMapping("/count")
    public long getKeysCount(@PathVariable String connectionId) {
        return redisClientService.count(connectionId);
    }

    @PostMapping("/expire/{key}/{seconds}")
    public void setKeyExpiration(@PathVariable String connectionId, @PathVariable String key, @PathVariable long seconds) {
        redisClientService.expire(connectionId, key, seconds);
    }

    @DeleteMapping("/expire/{key}")
    public void cleanKeyExpiration(@PathVariable String connectionId, @PathVariable String key) {
        redisClientService.clearExpire(connectionId, key);
    }

    @GetMapping("/type/{key}")
    public String getKeyType(@PathVariable String connectionId, @PathVariable String key) {
        return redisClientService.getType(connectionId, key).name();
    }

    @GetMapping("/migrate/{key}/{connectionTarget}")
    public void migrateKey(
            @PathVariable String connectionId, @PathVariable String key, @PathVariable String connectionTarget) {
        redisClientService.migrate(connectionId, key, connectionTarget);
    }
}
