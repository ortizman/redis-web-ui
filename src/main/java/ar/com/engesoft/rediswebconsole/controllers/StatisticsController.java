package ar.com.engesoft.rediswebconsole.controllers;

import ar.com.engesoft.rediswebconsole.services.RedisConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("${rest.context-path}/client/{id}/statistics")
public class StatisticsController {

    @Autowired
    private RedisConnectionService connectionService;

    @GetMapping(params = "type=SINGLE")
    public Map<String, String> getStatistics(@PathVariable("id") String id) throws IOException {
        return connectionService.getStatisticsForSingle(id);
    }

}
