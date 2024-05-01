package ar.com.engesoft.rediswebconsole.services;

import ar.com.engesoft.rediswebconsole.entities.ClientRedis;
import ar.com.engesoft.rediswebconsole.exceptions.ServiceException;
import jakarta.annotation.PostConstruct;
import org.redisson.api.redisnode.RedisMaster;
import org.redisson.api.redisnode.RedisNode;
import org.redisson.api.redisnode.RedisNodes;
import org.redisson.client.RedisConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class RedisConnectionService {

    private final ConcurrentMap<String, ClientRedis> clientsMap = new ConcurrentHashMap<>();

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    public String createConnection(String configYAML) throws IOException {
        try {
            final String uuid = UUID.randomUUID().toString();
            final ClientRedis clientRedis = new ClientRedis(configYAML, uuid).initialize();
            jdbcAggregateTemplate.insert(clientRedis);
            clientsMap.put(uuid, clientRedis);
            return uuid;
        } catch (RedisConnectionException rce) {
            throw new ServiceException("REDIS_CONNECTION_ERROR", rce.getCause().getMessage());
        }
    }

    public Collection<ClientRedis> getAll() {
        return clientsMap.values();
    }

    public Map<String, String> getStatisticsForSingle(String id) throws IOException {
        final RedisMaster instance = clientsMap.get(id)
                .getClient()
                .getRedisNodes(RedisNodes.SINGLE)
                .getInstance();
        final Map<String, String> statistics = instance.getMemoryStatistics();
        statistics.putAll(instance.info(RedisNode.InfoSection.ALL));

        return statistics;

    }

    // Carga los ClientRedis de la base de datos
    @PostConstruct
    public void init() {
        loadClientsFromDatabase();
    }

    private void loadClientsFromDatabase() {
        // Utilizar JdbcTemplate para obtener los resultados
        final Iterable<ClientRedis> clientDataList = jdbcAggregateTemplate.findAll(ClientRedis.class);

        // Se itera sobre la lista de ClientRedis y se carga en el mapa concurrente
        for (ClientRedis clientRedis : clientDataList) {
            clientsMap.put(clientRedis.getId(), clientRedis);
        }
    }

}
