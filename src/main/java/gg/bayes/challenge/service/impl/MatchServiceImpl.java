package gg.bayes.challenge.service.impl;

import gg.bayes.challenge.entity.*;
import gg.bayes.challenge.rest.model.HeroDamage;
import gg.bayes.challenge.rest.model.HeroItems;
import gg.bayes.challenge.rest.model.HeroKills;
import gg.bayes.challenge.rest.model.HeroSpells;
import gg.bayes.challenge.service.MatchService;
import gg.bayes.challenge.service.repository.*;
import gg.bayes.challenge.util.service.ParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;

    private final HeroKillsRepository heroKillsRepository;

    private final HeroItemRepository heroItemRepository;

    private final HeroSpellRepository heroSpellRepository;

    private final HeroDamageRepository heroDamageRepository;

    private final ParserService parserService;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository, ParserService parserService,
                            HeroKillsRepository heroKillsRepository, HeroSpellRepository heroSpellRepository,
                            HeroItemRepository heroItemRepository, HeroDamageRepository heroDamageRepository) {
        this.matchRepository = matchRepository;
        this.parserService = parserService;
        this.heroKillsRepository = heroKillsRepository;
        this.heroSpellRepository = heroSpellRepository;
        this.heroItemRepository = heroItemRepository;
        this.heroDamageRepository = heroDamageRepository;
    }

    @Override
    public Long ingestMatch(String payload) {
        MatchEntity match = new MatchEntity();
        match = matchRepository.save(match);
        parserService.readLines(payload, match.getId());
        return match.getId();
    }

    @Override
    public List<HeroKills> getKills(Long matchId) {
        List<HeroKills> heroKills = new ArrayList<>();
        List<HeroKillsEntity> heroKillsEntities = heroKillsRepository.findByMatchId(matchId);
        if (Objects.nonNull(heroKillsEntities)) {
            Map<String, List<HeroKillsEntity>> killsMap = heroKillsEntities.stream()
                    .collect(Collectors.groupingBy(k -> k.getHero()));
            killsMap.forEach((k, v) -> heroKills.add(new HeroKills(k, v.size())));
        }
        return heroKills;
    }

    @Override
    public List<HeroItems> getItems(Long matchId, String hero) {
        List<HeroItemEntity> heroItemEntities = heroItemRepository.findByMatchIdAndHero(matchId, hero);
        List<HeroItems> heroItems = new ArrayList<>();
        if (Objects.nonNull(heroItemEntities)) {
            Map<Long, HeroItemEntity> itemsMap = heroItemEntities.stream()
                    .collect(Collectors.toMap(k -> k.getTimestamp(), Function.identity()));
            itemsMap.forEach((k, v) -> heroItems.add(new HeroItems(v.getItem(), k)));
        }
        return heroItems;
    }

    @Override
    public List<HeroSpells> getSpells(Long matchId, String hero) {
        List<HeroSpellEntity> heroSpellsEntities = heroSpellRepository.findByMatchIdAndHero(matchId, hero);
        List<HeroSpells> heroSpells = new ArrayList<>();
        if (Objects.nonNull(heroSpellsEntities)) {
            Map<String, List<HeroSpellEntity>> groupBySpell =
                    heroSpellsEntities.stream().collect(Collectors.groupingBy(entity -> entity.getSpell()));
            groupBySpell.forEach((k, v) -> heroSpells.add(new HeroSpells(k, v.size())));
        }
        return heroSpells;
    }

    @Override
    public List<HeroDamage> getDamage(Long matchId, String hero) {
        List<HeroDamageEntity> heroDamageEntities = heroDamageRepository.findByMatchIdAndHero(matchId, hero);
        List<HeroDamage> heroDamages = new ArrayList<>();
        if (Objects.nonNull(heroDamageEntities)) {
            Map<String, List<HeroDamageEntity>> damageMap = heroDamageEntities.stream()
                    .collect(Collectors.groupingBy(entity -> entity.getTarget()));

            damageMap.forEach((k, v) ->{
                int totalDamage = v.stream().map(entity -> entity.getDamage()).reduce((a, b) -> a + b).get();
                heroDamages.add(new HeroDamage(k, v.size(), totalDamage));
            });
        }
        return heroDamages;
    }
}
