package ar.com.engesoft.rediswebconsole.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.redis.connection.RedisPassword;

import java.io.IOException;

/**
 * Custom deserializer for {@link RedisPassword}
 */
@JsonComponent
class RedisPasswordJsonComponents {

    public static class RedisPasswordDeserializer extends StdDeserializer<RedisPassword> {
        public RedisPasswordDeserializer() {
            super(RedisPassword.class);
        }

        @Override
        public RedisPassword deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
            String value = parser.readValueAsTree().toString(); // Adjust this according to your JSON structure
            return RedisPassword.of(value);
        }
    }

    public static class RedisPasswordSerializer extends StdSerializer<RedisPassword> {
        public RedisPasswordSerializer() {
            super(RedisPassword.class);
        }

        @Override
        public void serialize(RedisPassword value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (value == null || !value.isPresent()) {
                gen.writeString("");
            } else {
                // Serialize RedisPassword as a JSON string
                gen.writeString(String.valueOf(value.get()));
            }
        }
    }

}
