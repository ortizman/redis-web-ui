package ar.com.engesoft.rediswebconsole.controllers;

import ar.com.engesoft.rediswebconsole.services.RedisConnectionService;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("${rest.context-path}/client")
public class ClientController {

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

}
