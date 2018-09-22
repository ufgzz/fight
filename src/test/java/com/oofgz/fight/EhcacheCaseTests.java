package com.oofgz.fight;

import com.oofgz.fight.domain.secondary.DsUser;
import com.oofgz.fight.repository.secondary.CountryRepository;
import com.oofgz.fight.repository.secondary.DsUserSecondaryRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class EhcacheCaseTests {


    private static final List<String> SAMPLE_COUNTRY_CODES = Arrays.asList("AF", "AX",
            "AL", "DZ", "AS", "AD", "AO", "AI", "AQ", "AG", "AR", "AM", "AW", "AU", "AT",
            "AZ", "BS", "BH", "BD", "BB", "BY", "BE", "BZ", "BJ", "BM", "BT", "BO", "BQ",
            "BA", "BW", "BV", "BR", "IO", "BN", "BG", "BF", "BI", "KH", "CM", "CA", "CV",
            "KY", "CF", "TD", "CL", "CN", "CX", "CC", "CO", "KM", "CG", "CD", "CK", "CR",
            "CI", "HR", "CU", "CW", "CY", "CZ", "DK", "DJ", "DM", "DO", "EC", "EG", "SV",
            "GQ", "ER", "EE", "ET", "FK", "FO", "FJ", "FI", "FR", "GF", "PF", "TF", "GA",
            "GM", "GE", "DE", "GH", "GI", "GR", "GL", "GD", "GP", "GU", "GT", "GG", "GN",
            "GW", "GY", "HT", "HM", "VA", "HN", "HK", "HU", "IS", "IN", "ID", "IR", "IQ",
            "IE", "IM", "IL", "IT", "JM", "JP", "JE", "JO", "KZ", "KE", "KI", "KP", "KR",
            "KW", "KG", "LA", "LV", "LB", "LS", "LR", "LY", "LI", "LT", "LU", "MO", "MK",
            "MG", "MW", "MY", "MV", "ML", "MT", "MH", "MQ", "MR", "MU", "YT", "MX", "FM",
            "MD", "MC", "MN", "ME", "MS", "MA", "MZ", "MM", "NA", "NR", "NP", "NL", "NC",
            "NZ", "NI", "NE", "NG", "NU", "NF", "MP", "NO", "OM", "PK", "PW", "PS", "PA",
            "PG", "PY", "PE", "PH", "PN", "PL", "PT", "PR", "QA", "RE", "RO", "RU", "RW",
            "BL", "SH", "KN", "LC", "MF", "PM", "VC", "WS", "SM", "ST", "SA", "SN", "RS",
            "SC", "SL", "SG", "SX", "SK", "SI", "SB", "SO", "ZA", "GS", "SS", "ES", "LK",
            "SD", "SR", "SJ", "SZ", "SE", "CH", "SY", "TW", "TJ", "TZ", "TH", "TL", "TG",
            "TK", "TO", "TT", "TN", "TR", "TM", "TC", "TV", "UG", "UA", "AE", "GB", "US",
            "UM", "UY", "UZ", "VU", "VE", "VN", "VG", "VI", "WF", "EH", "YE", "ZM", "ZW");

    @Autowired
    private DsUserSecondaryRepository dsUserSecondaryRepository;


    @Autowired
    private CountryRepository countryRepository;

    private Random random;

    @Before
    public void setUp() {

        log.info("测试Ehcache的缓存功能");
        dsUserSecondaryRepository.deleteAll();
        dsUserSecondaryRepository.save(new DsUser("AAA", 23));

        this.random = new Random();
        //clean();
    }

    @Test
    public void ehcacheTest() {

        DsUser u1 = dsUserSecondaryRepository.findByName("AAA");
        log.info("第一次查询：" + u1.getAge());

        DsUser u2 = dsUserSecondaryRepository.findByName("AAA");
        log.info("第二次查询：" + u2.getAge());

        u1.setAge(20);
        dsUserSecondaryRepository.save(u1);
        DsUser u3 = dsUserSecondaryRepository.findByName("AAA");
        log.info("第三次查询：" + u3.getAge());

    }


    @Test
    public void mixEhcacheAndRedisTest() {
        for (int i = 0 ; i < 10; i ++) {
            retrieveCountry();
        }

       // removeCountry();
       // clean();
    }

    public void removeCountry() {
        String randomCode = SAMPLE_COUNTRY_CODES
                .get(this.random.nextInt(SAMPLE_COUNTRY_CODES.size()));
        log.info("Removing for country with code '" + randomCode + "'");
        this.countryRepository.removeByCode(randomCode);
    }

    public void retrieveCountry() {
        String randomCode = SAMPLE_COUNTRY_CODES
                .get(this.random.nextInt(SAMPLE_COUNTRY_CODES.size()));
        log.info("Looking for country with code '" + randomCode + "'");
        this.countryRepository.findByCode(randomCode);
    }

    public void clean() {
        log.info("clean all countries");
        this.countryRepository.clean();
    }

}
