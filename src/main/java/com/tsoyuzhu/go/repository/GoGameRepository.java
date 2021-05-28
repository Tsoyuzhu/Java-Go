package com.tsoyuzhu.go.repository;

import com.tsoyuzhu.go.domain.gameLogic.GoGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GoGameRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void createGoGame(GoGame goGame) {
        mongoTemplate.save(goGame);
    }

    public Optional<GoGame> findGoGameById(String gameId) {
        GoGame goGame = mongoTemplate.findById(gameId, GoGame.class);
        if (goGame == null) {
            return Optional.empty();
        } else {
            return Optional.of(goGame);
        }
    }

    public void updateGoGame(GoGame goGame) {
        // Lazy update for now but needs to be optimised later
        mongoTemplate.save(goGame);
    }
}
