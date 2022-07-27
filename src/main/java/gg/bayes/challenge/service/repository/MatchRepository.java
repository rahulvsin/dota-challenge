package gg.bayes.challenge.service.repository;

import gg.bayes.challenge.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {

}
