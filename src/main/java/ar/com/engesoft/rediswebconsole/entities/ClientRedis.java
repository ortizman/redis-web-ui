package ar.com.engesoft.rediswebconsole.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Slf4j
@Getter
@Setter
@Table("REDIS_CONFIG")
public class ClientRedis {

    @Id
    @Column("ID")
    private String id;

    @JsonRawValue
    @Column("REDIS_CONFIGURATION_JSON")
    private String redisConfigurationJson;

    @Column("ALIAS")
    private String alias;

    @JsonIgnore
    @Transient
    private transient StringRedisTemplate redisTemplate;

    @JsonIgnore
    @Transient
    private transient RedisConfiguration redisConfiguration;

    public ClientRedis() {
        // default constuctor
    }

    public ClientRedis(String uuid, String redisConfiguration, StringRedisTemplate redisTemplate) {
        this.setRedisTemplate(redisTemplate);
        this.setRedisConfigurationJson(redisConfiguration);
        this.setId(uuid);
    }

    public ClientRedis(String uuid,
                       String redisConfigurationJson,
                       StringRedisTemplate redisTemplate,
                       RedisConfiguration redisConfiguration) {
        this.setRedisTemplate(redisTemplate);
        this.setRedisConfigurationJson(redisConfigurationJson);
        this.setRedisConfiguration(redisConfiguration);
        this.setId(uuid);
    }

    @Override
    public String toString() {
        return "ClientRedis{" +
                "id='" + id + '\'' +
                ", redisConfigurationJson='" + redisConfigurationJson + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}
