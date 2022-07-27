package gg.bayes.challenge.util.service;

import gg.bayes.challenge.entity.HeroDamageEntity;
import gg.bayes.challenge.entity.HeroItemEntity;
import gg.bayes.challenge.entity.HeroKillsEntity;
import gg.bayes.challenge.entity.HeroSpellEntity;
import gg.bayes.challenge.service.repository.HeroDamageRepository;
import gg.bayes.challenge.service.repository.HeroItemRepository;
import gg.bayes.challenge.service.repository.HeroKillsRepository;
import gg.bayes.challenge.service.repository.HeroSpellRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.Duration;
import java.time.LocalTime;

@Slf4j
@Service
public class ParserService {

    private final HeroItemRepository heroItemRepository;

    private final HeroKillsRepository heroKillsRepository;

    private final HeroSpellRepository heroSpellRepository;

    private final HeroDamageRepository heroDamageRepository;

    public ParserService(HeroItemRepository heroItemRepository, HeroKillsRepository heroKillsRepository,
                         HeroSpellRepository heroSpellRepository, HeroDamageRepository heroDamageRepository) {
        this.heroItemRepository = heroItemRepository;
        this.heroKillsRepository = heroKillsRepository;
        this.heroSpellRepository = heroSpellRepository;
        this.heroDamageRepository = heroDamageRepository;
    }

    public void readLines(String payload, long matchId) {
        BufferedReader bufReader = new BufferedReader(new StringReader(payload));
        try {
            String line = "";
            while( (line=bufReader.readLine()) != null ) {
                processLine(line, matchId);
            }
        } catch (Exception e) {
            log.error("Exception occurred : {} ", e.getMessage());
        }
    }

    public void processLine(String line, long matchId) {
        if (line.contains("buys item")) {
            insertItems(line, matchId);
        } else if (line.contains("killed by")) {
            insertKills(line, matchId);
        } else if (line.contains("casts ability")) {
            insertSpells(line, matchId);
        } else if (line.contains("hits")) {
            insertDamage(line, matchId);
        }
    }

    private void insertItems(String line, long matchId) {
        String time = line.substring(1, line.indexOf("]"));
        long timeIn = getTimeInMillis(time);
        HeroItemEntity item = new HeroItemEntity();
        item.setItem(line.substring(line.indexOf(PatternConstants.PATTERN_ITEM)));
        item.setHero(getHeroName(line));
        item.setMatchId(matchId);
        item.setTimestamp(timeIn);
        heroItemRepository.save(item);
    }

    private void insertKills(String line, long matchId) {
        if (line.split(PatternConstants.PATTERN_HERO).length > 2) {
            HeroKillsEntity heroKills = new HeroKillsEntity();
            String hero = line.substring(line.indexOf(PatternConstants.PATTERN_KILL) + PatternConstants.PATTERN_KILL.length());
            heroKills.setHero(hero);
            heroKills.setMatchId(matchId);
            heroKills.setHeroDeceased(getHeroName(line));
            heroKillsRepository.save(heroKills);
        }
    }

    private void insertSpells(String line, long matchId) {
        if (line.contains(PatternConstants.PATTERN_HERO)) {
            String hero = line.substring(line.indexOf("] ")+2, line.indexOf(PatternConstants.PATTERN_CAST)-1);
            String spell = line.substring(line.indexOf(PatternConstants.PATTERN_CAST) + PatternConstants.PATTERN_CAST.length());
            spell = spell.substring(0, spell.indexOf(" "));
            HeroSpellEntity heroSpell = new HeroSpellEntity();
            heroSpell.setMatchId(matchId);
            heroSpell.setHero(hero);
            heroSpell.setSpell(spell);
            heroSpellRepository.save(heroSpell);
        }
    }

    private void insertDamage(String line, long matchId) {
        if (line.split(PatternConstants.PATTERN_HERO).length > 2) {

            HeroDamageEntity heroDamage = new HeroDamageEntity();
            String hero = line.substring(line.indexOf("] ")+2, line.indexOf(PatternConstants.PATTERN_HITS)-1);
            String target = line.substring(line.indexOf(PatternConstants.PATTERN_HITS) + PatternConstants.PATTERN_HITS.length());
            target = target.substring(0, target.indexOf(" "));
            String damage = line.substring(line.indexOf(PatternConstants.PATTERN_DAMAGE) + PatternConstants.PATTERN_DAMAGE.length());
            damage = damage.substring(0, damage.indexOf(" "));
            heroDamage.setHero(hero);
            heroDamage.setTarget(target);
            heroDamage.setMatchId(matchId);
            heroDamage.setDamage(Integer.valueOf(damage));
            heroDamageRepository.save(heroDamage);
        }
    }

    private Long getTimeInMillis(String s) {
        return Duration.between(LocalTime.MIDNIGHT, LocalTime.parse(s)).toMillis();
    }

    private String getHeroName(String str) {
        String name = str.substring(str.indexOf(PatternConstants.PATTERN_HERO));
        return name.substring(0, name.indexOf(" "));
    }
}

