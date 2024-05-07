package ar.com.engesoft.rediswebconsole.controllers;

import ar.com.engesoft.rediswebconsole.entities.ClientRedis;
import ar.com.engesoft.rediswebconsole.services.ConnectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("${rest.context-path}/client")
public class ConnectionsController {

    @Autowired
    private ConnectionsService connectionService;

    /**
     * Create new clinet for the given configuration.
     *
     * @param redisConfiguration
     * @return {id} unique id
     */
    @PostMapping("/standalone")
    public String create(@RequestBody RedisStandaloneConfiguration redisConfiguration) throws IOException {
        return this.connectionService.createStandalone(redisConfiguration);
    }

    @PutMapping("/{id}")
    public void updateConnection(@PathVariable("id") String id,
                                        @RequestBody RedisConfiguration redisConfiguration) throws IOException {
        this.connectionService.updateConnection(id, redisConfiguration);
    }

    @GetMapping("/")
    public Collection<ClientRedis> getAllConnections() {
        return connectionService.getAll();
    }

    @GetMapping("/{id}")
    public ClientRedis findById(@PathVariable("id") String id) {
        return connectionService.findById(id);
    }

    @GetMapping(value = "/{id}/redisConfiguration", produces = "application/json")
    public String getConfigYamlById(@PathVariable("id") String id) {
        return connectionService.findById(id).getRedisConfigurationJson();
    }

    @PatchMapping("/{id}/name")
    public ClientRedis setName(@PathVariable("id") String id, @RequestBody String name) {
        return this.connectionService.setConnectionAlias(id, name);
    }

}
