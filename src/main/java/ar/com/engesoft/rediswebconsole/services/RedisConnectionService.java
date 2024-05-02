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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class RedisConnectionService {

    private final ConcurrentMap<String, ClientRedis> clientsMap = new ConcurrentHashMap<>();

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    private final Collection<String> validTypes = List.of("CLUSTER", "MASTER_SLAVE", "SENTINEL_MASTER_SLAVE", "SINGLE");

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

    public ClientRedis updateConnection(String id, String config) throws IOException {
        final ClientRedis clientRedis = this.clientsMap.get(id);
        clientRedis.shutdown();
        clientRedis.setConfigYaml(config);
        return clientRedis.initialize();
    }

    public ClientRedis findById(String id) {
        return this.clientsMap.get(id);
    }

    public ClientRedis setConnectionName(String id, String name) {
        final ClientRedis clientRedis = this.clientsMap.get(id);
        clientRedis.setName(name);
        return clientRedis;
    }

    public ClientRedis setConnectionType(String id, String type) {
        final ClientRedis clientRedis = this.clientsMap.get(id);
        if (!validTypes.contains(type)) {
            throw new ServiceException("INVALID_TYPE", "El tipo de conexión es invalido. " +
                    "Los valores posibles son: " + validTypes);
        }
        clientRedis.setType(type);
        return clientRedis;
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
