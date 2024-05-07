package ar.com.engesoft.rediswebconsole.services;

import ar.com.engesoft.rediswebconsole.config.SpringBeanFactory;
import ar.com.engesoft.rediswebconsole.entities.ClientRedis;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.SessionTrackingMode;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
public class ConnectionsService {

    private final ConcurrentMap<String, ClientRedis> clientsMap = new ConcurrentHashMap<>();

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private SpringBeanFactory springBeanFactory;

    public String createStandalone(RedisConfiguration configuration) throws JsonProcessingException {
        final StringRedisTemplate redisTemplate = newRedisTemplate(configuration);

        final String uuid = UUID.randomUUID().toString();
        final String redisConfJson = mapper.writeValueAsString(configuration);
        final ClientRedis clientRedis = new ClientRedis(uuid, redisConfJson, redisTemplate);
        jdbcAggregateTemplate.insert(clientRedis);
        clientsMap.put(uuid, clientRedis);
        return uuid;
    }

    public void updateConnection(String id, RedisConfiguration configuration) throws IOException {
        final LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration);

        final StringRedisTemplate redisTemplate =
                new StringRedisTemplate(connectionFactory);
        final ClientRedis clientRedis = this.clientsMap.get(id);
        clientRedis.setRedisTemplate(redisTemplate);
        final String redisConfJson = mapper.writeValueAsString(configuration);
        clientRedis.setRedisConfigurationJson(redisConfJson);

        jdbcAggregateTemplate.update(clientRedis);
    }

    public ClientRedis findById(String id) {
        return this.clientsMap.get(id);
    }

    public ClientRedis setConnectionAlias(String id, String alias) {
        final ClientRedis clientRedis = this.clientsMap.get(id);
        clientRedis.setAlias(alias);
        jdbcAggregateTemplate.update(clientRedis);
        return clientRedis;
    }


    public Collection<ClientRedis> getAll() {
        return clientsMap.values();
    }

    public Properties getStatisticsForSingle(String id) throws IOException {
        return redisTemplate(id)
                .execute(connection -> connection.serverCommands().info(), false);
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
            try {
                clientRedis.setRedisConfiguration(mapper.readValue(
                        clientRedis.getRedisConfigurationJson(), RedisStandaloneConfiguration.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            clientsMap.put(clientRedis.getId(), clientRedis);
        }
    }

    private StringRedisTemplate redisTemplate(String id) {
        final ClientRedis clientRedis = clientsMap.get(id);
        if (clientRedis.getRedisTemplate() == null) {
            clientRedis.setRedisTemplate(newRedisTemplate(clientRedis.getRedisConfiguration()));
        }

        return clientRedis.getRedisTemplate();

    }

    private StringRedisTemplate newRedisTemplate(RedisConfiguration configuration) {
        final LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration);
        final RedisConnectionFactory redisConnectionFactory =
                this.springBeanFactory.registryBeanConnectionFactory(connectionFactory);

        final StringRedisTemplate redisTemplate = new StringRedisTemplate();
        return this.springBeanFactory.registryBeanRestTemplate(redisTemplate, redisConnectionFactory);
    }
}
