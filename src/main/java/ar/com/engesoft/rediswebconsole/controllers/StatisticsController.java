package ar.com.engesoft.rediswebconsole.controllers;

import ar.com.engesoft.rediswebconsole.services.ConnectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Properties;

@RestController
@RequestMapping("${rest.context-path}/client/{id}/statistics")
public class StatisticsController {

    @Autowired
    private ConnectionsService connectionService;

    @GetMapping
    public Properties getStatistics(@PathVariable("id") String id) throws IOException {
        return connectionService.getStatisticsForSingle(id);
    }

}
