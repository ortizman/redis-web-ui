package ar.com.engesoft.rediswebconsole.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.redisnode.BaseRedisNodes;
import org.redisson.api.redisnode.RedisNodes;
import org.redisson.config.Config;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.IOException;

@Getter
@Setter
@Table("CLIENT_REDIS")
public class ClientRedis {


    @Id
    @Column("ID")
    private String id;

    @JsonIgnore
    @Transient
    private transient RedissonClient client;

    @Column("CONFIG_YAML")
    private String configYaml;

    @JsonIgnore
    @Transient
    private transient Config config;

    @Column("TYPE")
    private String type = "SINGLE";

    @Column("NAME")
    private String name;

    public ClientRedis() {
        // default constuctor
    }

    public ClientRedis(String configYAML, String uuid) {
        this.configYaml = configYAML;
        this.setId(uuid);
    }

    public RedisNodes<? extends BaseRedisNodes> getRedisNodes() {
        return switch (this.type) {
            case "CLUSTER" -> RedisNodes.CLUSTER;
            case "MASTER_SLAVE" -> RedisNodes.MASTER_SLAVE;
            case "SENTINEL_MASTER_SLAVE" -> RedisNodes.SENTINEL_MASTER_SLAVE;
            default -> RedisNodes.SINGLE;
        };
    }

    public void setConfigYaml(String configYaml) throws IOException {
        this.configYaml = configYaml;
        this.config = Config.fromYAML(configYaml);
        setDefaultValues(config);
    }

    public RedissonClient getClient() throws IOException {

        if (client == null || client.isShutdown() || client.isShuttingDown()) {
            this.client = this.initialize().getClient();
        }

        return client;
    }

    public ClientRedis initialize() throws IOException {
        this.setConfigYaml(this.configYaml);
        final RedissonClient redissonClient = Redisson.create(config);
        this.setClient(redissonClient);
        return this;
    }

    private void setDefaultValues(Config config) {
        if (config.isClusterConfig()) {
            config.useClusterServers().setMasterConnectionMinimumIdleSize(1);
            config.useClusterServers().setMasterConnectionPoolSize(1);
            config.useClusterServers().setSlaveConnectionMinimumIdleSize(1);
            config.useClusterServers().setSlaveConnectionPoolSize(1);
        } else if (config.isSentinelConfig()) {
            config.useSentinelServers().setSlaveConnectionMinimumIdleSize(1);
            config.useSentinelServers().setSlaveConnectionPoolSize(1);
            config.useSentinelServers().setSlaveConnectionPoolSize(1);
            config.useSentinelServers().setSlaveConnectionMinimumIdleSize(1);
        } else if (config.isSingleConfig()) {
            config.useSingleServer().setConnectionPoolSize(1);
            config.useSingleServer().setConnectionMinimumIdleSize(1);
        }

        config.setNettyThreads(3);
        config.setThreads(3);
    }
}
