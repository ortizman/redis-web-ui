package ar.com.engesoft.rediswebconsole.services;

import ar.com.engesoft.rediswebconsole.exceptions.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
public class RedisConnectionServiceTest {

    @InjectMocks
    private RedisConnectionService redisConnectionService;

    @Mock
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    public void testCreateConnection() throws ServiceException, IOException {

    }

    @Test
    public void testGetAll() {

    }
}
