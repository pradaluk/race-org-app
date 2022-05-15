package cz.cvut.kbss.ear.race.dao;

import cz.cvut.kbss.ear.race.model.Championship;
import org.springframework.stereotype.Repository;

@Repository
public class ChampionshipDao extends BaseDao<Championship> {

    public ChampionshipDao() {
        super(Championship.class);
    }
}
