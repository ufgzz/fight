
package com.oofgz.fight.repository.secondary;

import com.oofgz.fight.dto.redis.Country;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@CacheConfig(cacheNames = "countries")
public class CountryRepository {

    @Cacheable
    public Country findByCode(String code) {
        log.info("---> Loading country with code '" + code + "'");
        return new Country(code);
    }

    @CacheEvict
    public void removeByCode(String code) {
        log.info("---> Removed country with code '" + code + "'");
    }

    @CacheEvict(allEntries = true)
    public void clean() {
        log.info("---> cleaned all countries");
    }

}
