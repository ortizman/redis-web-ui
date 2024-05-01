package ar.com.engesoft.rediswebconsole.repositories;

import ar.com.engesoft.rediswebconsole.entities.ClientRedis;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDataRepository extends CrudRepository<ClientRedis, String> {

    @Modifying
    @Query("UPDATE ClientRedis SET name = :name WHERE id = :id")
    boolean updateNameById(@Param("id") Long id, @Param("name") String name);
}