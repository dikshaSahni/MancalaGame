package com.bol.game.kalaha.respository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.bol.game.kalaha.model.KalahaGame;

@Repository
public interface KalahaGameRepository extends CassandraRepository<KalahaGame, String> {

}
