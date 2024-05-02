package ar.com.engesoft.rediswebconsole.controllers;

import ar.com.engesoft.rediswebconsole.entities.ClientRedis;
import ar.com.engesoft.rediswebconsole.services.RedisConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("${rest.context-path}/client")
public class ConnectionsController {

    @Autowired
    private RedisConnectionService connectionService;

    /**
     * Create new clinet for the given configuration.
     *
     * @param config
     * @return {id} unique id
     */
    @PostMapping("/")
    public String create(@RequestBody String config) throws IOException {
        return this.connectionService.createConnection(config);
    }

    @PutMapping("/{id}")
    public ClientRedis updateConnection(@PathVariable("id") String id, @RequestBody String config) throws IOException {
        return this.connectionService.updateConnection(id, config);
    }

    @GetMapping("/")
    public Collection<ClientRedis> getAllConnections() {
        return connectionService.getAll();
    }

    @GetMapping("/{id}")
    public ClientRedis findById(@PathVariable("id") String id) {
        return connectionService.findById(id);
    }

    @GetMapping(value = "/{id}/configYaml", produces = "application/x-yaml")
    public String getConfigYamlById(@PathVariable("id") String id) {
        return connectionService.findById(id).getConfigYaml();
    }

    @PatchMapping("/{id}/name")
    public ClientRedis setName(@PathVariable("id") String id, @RequestBody String name) {
        return this.connectionService.setConnectionName(id, name);
    }

    @PatchMapping("/{id}/type")
    public ClientRedis setType(@PathVariable("id") String id, @RequestBody String type) {
        return this.connectionService.setConnectionType(id, type);
    }

}
