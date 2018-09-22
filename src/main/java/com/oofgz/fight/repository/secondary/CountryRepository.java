
package com.oofgz.fight.repository.secondary;

import com.oofgz.fight.dto.redis.Country;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@CacheConfig(cacheNames = "countries")
public class CountryRepository {

    @Cacheable
    public Country findByCode(String code) {
        System.out.println("---> Loading country with code '" + code + "'");
        return new Country(code);
    }

    @CacheEvict
    public void removeByCode(String code) {
        System.out.println("---> Removed country with code '" + code + "'");
    }

    @CacheEvict(allEntries = true)
    public void clean() {
        System.out.println("---> cleaned all countries");
    }

}
