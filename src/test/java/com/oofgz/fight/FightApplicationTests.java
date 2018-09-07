package com.oofgz.fight;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.MongoClient;
import com.oofgz.fight.bean.User;
import com.oofgz.fight.controller.GreetingController;
import com.oofgz.fight.controller.UserController;
import com.oofgz.fight.domain.primary.*;
import com.oofgz.fight.domain.secondary.DsUser;
import com.oofgz.fight.domain.thirdly.DsMessage;
import com.oofgz.fight.repository.primary.JpaDeptRepository;
import com.oofgz.fight.repository.primary.MongoDbUserRepository;
import com.oofgz.fight.repository.primary.MybatisUserMapper;
import com.oofgz.fight.repository.primary.PersonRepository;
import com.oofgz.fight.repository.secondary.DsUserRepository;
import com.oofgz.fight.repository.thirdly.DsMessageRepository;
import com.oofgz.fight.service.BlogProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.naming.Name;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class FightApplicationTests {

	private MockMvc mockMvc;

	@Autowired
	private MongoClient mongoClient;

	@Autowired
	private BlogProperties blogProperties;

	@Autowired
	@Qualifier("secondaryJdbcTemplate")
	private JdbcTemplate jdbcTemplateSecondary;

	@Autowired
	@Qualifier("thirdlyJdbcTemplate")
	private JdbcTemplate jdbcTemplateThirdly;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private JpaDeptRepository jpaDeptRepository;

	@Autowired
	private DsUserRepository dsUserRepository;

	@Autowired
	private DsMessageRepository dsMessageRepository;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RedisTemplate<String, RedisUser> redisUserRedisTemplate;

	@Autowired
	private MongoDbUserRepository mongoDbUserRepository;

	@Autowired
	private MybatisUserMapper mybatisUserMapper;


	@Test
	public void contextLoads() {

	}

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(
				new GreetingController(),
				new UserController()
		).build();

		jdbcTemplateSecondary.update("DELETE FROM DS_USER");
		jdbcTemplateThirdly.update("DELETE FROM DS_USER");

		mongoDbUserRepository.deleteAll();
	}

	@Test
	public void getSayHello() throws Exception {
		mockMvc.perform(get("/Greet/greeting").accept(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("{\"id\":1,\"content\":\"Hello, World!\"}")));
	}


	@Test
	public void testUserController() throws Exception {
		// 测试UserController
		RequestBuilder requestBuilder;
		User user;
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

		String initExceptStr = "[{\"name\":\"测试大师\",\"age\":\"20\",\"phone\":\"15980278580\",\"profession\":\"软件工程师\"}]";
		String updateExceptStr = "{\"name\":\"测试终极大师\",\"age\":\"30\",\"phone\":\"15980278580\",\"profession\":\"软件工程师\"}";

		// 1、get查一下user列表，应该为空
		requestBuilder = get("/User/getUserList");
		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("[]")));

		// 2、post提交一个user
		user = new User();
		user.setName("测试大师");
		user.setAge("20");
		user.setPhone("15980278580");
		user.setProfession("软件工程师");
		requestBuilder = post("/User/postUser")
				.contentType(APPLICATION_JSON).content(ow.writeValueAsString(user));
		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().string(equalTo("add success")));

		// 3、get获取user列表，应该有刚才插入的数据
		requestBuilder = get("/User/getUserList");
		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().string(equalTo(initExceptStr)));

		// 4、put修改id为1的user
		user = new User();
		user.setName("测试终极大师");
		user.setAge("30");
		requestBuilder = put("/User/测试大师")
				.contentType(APPLICATION_JSON).content(ow.writeValueAsString(user));
		mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(content().string(equalTo("put update success")));

		// 5、get一个id为1的user
		requestBuilder = get("/User/测试大师");
		mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(content().string(equalTo(updateExceptStr)));

		// 6、delete删除id为1的user
		requestBuilder = delete("/User/测试终极大师");
		mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(content().string(equalTo("delete success")));

		// 7、get查一下user列表，应该为空
		requestBuilder = get("/User/getUserList");
		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("[]")));

	}

	@Test
	public void findAllLdap() {
		personRepository.findAll().forEach(new Consumer<Person>() {
			@Override
			public void accept(Person person) {
				System.out.println(person);
			}
		});
	}

	@Test
	public void saveLdap() {
		Name dn = LdapNameBuilder.newInstance()
				.add("ou", "Mini")
				.build();
		Person person = new Person();
		person.setId(dn);
		person.setDescription("说明情况,,,修改后的结果+++++");
		person.setNew(!personRepository.existsById(dn));
		personRepository.save(person);
		personRepository.findAll().forEach(new Consumer<Person>() {
			@Override
			public void accept(Person person) {
				System.out.println(person);
			}
		});
	}

	@Test
	public void mongoPlusTest() {
		log.info("MinConnectionsPerHost = {}, MaxConnectionsPerHost = {}",
				mongoClient.getMongoClientOptions().getMinConnectionsPerHost(),
				mongoClient.getMongoClientOptions().getConnectionsPerHost()
		);
	}


	@Test
	public void jpaDeptTest() {
		//创建10条记录
		jpaDeptRepository.save(new JpaDept("AAA", "Des_AAA", 10));
		jpaDeptRepository.save(new JpaDept("BBB", "Des_BBB", 20));
		jpaDeptRepository.save(new JpaDept("CCC", "Des_CCC", 30));
		jpaDeptRepository.save(new JpaDept("DDD", "Des_DDD", 40));
		jpaDeptRepository.save(new JpaDept("EEE", "Des_EEE", 50));
		jpaDeptRepository.save(new JpaDept("FFF", "Des_FFF", 60));
		jpaDeptRepository.save(new JpaDept("GGG", "Des_GGG", 70));
		jpaDeptRepository.save(new JpaDept("HHH", "Des_HHH", 80));
		jpaDeptRepository.save(new JpaDept("III", "Des_III", 90));
		jpaDeptRepository.save(new JpaDept("JJJ", "Des_JJJ", 100));

		// 测试findAll, 查询所有记录
		List<JpaDept> jpaDeptList = jpaDeptRepository.findAll();
		Assert.assertEquals(10, jpaDeptList.size());

		// 测试findByName, 查询名称为FFF的JpaDept
		JpaDept jpaDept = jpaDeptRepository.findByName("FFF");
		Assert.assertEquals(60, jpaDept.getNum().longValue());

		// 测试findUser, 查询名称为GGG的JpaDept
		jpaDept = jpaDeptRepository.findJpaDept("GGG");
		Assert.assertEquals(70, jpaDept.getNum().longValue());

		// 测试findByNameAndAge, 查询名称为FFF并且人数为60的JpaDept
		jpaDept = jpaDeptRepository.findByNameAndNum("FFF", 60);
		Assert.assertEquals("FFF", jpaDept.getName());

		// 测试删除名称为AAA的JpaDept
		jpaDeptRepository.delete(jpaDeptRepository.findByName("AAA"));

		// 测试findAll, 查询所有记录, 验证上面的删除是否成功
		jpaDeptList = jpaDeptRepository.findAll();
		Assert.assertEquals(9, jpaDeptList.size());

	}

	@Test
	public void testBlogProperties() {
		Assert.assertEquals("zfgcian@gmail.com", blogProperties.getName());
		Assert.assertEquals("Spring boot教程", blogProperties.getTitle());
		Assert.assertEquals("zfgcian@gmail.com正在努力学习《Spring boot教程》", blogProperties.getDesc());

		log.info("随机数测试输出：");
		log.info("随机字符串 : " + blogProperties.getValue());
		log.info("随机int : " + blogProperties.getNumber());
		log.info("随机long : " + blogProperties.getBigNumber());
		log.info("随机10以下 : " + blogProperties.getRandomNumIn10());
		log.info("随机10-20 : " + blogProperties.getRandomNumBetween1020());
	}


	@Test
	public void multiplyDatasourceTest() {
		// 往第二个数据源中插入两条数据
		jdbcTemplateSecondary.update("insert into ds_user(name,age) values(?, ?)", "aaa", 20);
		jdbcTemplateSecondary.update("insert into ds_user(name,age) values(?, ?)", "bbb", 30);

		// 往第三个数据源中插入一条数据，若插入的是第一个数据源，则会主键冲突报错
		jdbcTemplateThirdly.update("insert into ds_user(name,age) values(?, ?)", "aaa", 20);

		// 查一下第二个数据源中是否有两条数据，验证插入是否成功
		Assert.assertEquals("2", jdbcTemplateSecondary.queryForObject("select count(1) from ds_user", String.class));

		// 查一下第三个数据源中是否有两条数据，验证插入是否成功
		Assert.assertEquals("1", jdbcTemplateThirdly.queryForObject("select count(1) from ds_user", String.class));

	}

	@Test
	public void multiplyJpaDatasourceTest() {
		dsUserRepository.save(new DsUser("aaa", 10));
		dsUserRepository.save(new DsUser("bbb", 20));
		dsUserRepository.save(new DsUser("ccc", 30));
		dsUserRepository.save(new DsUser("ddd", 40));
		dsUserRepository.save(new DsUser("eee", 50));
		Assert.assertEquals(5, dsUserRepository.findAll().size());

		dsMessageRepository.save(new DsMessage("o1", "aaa_aaa_aaa"));
		dsMessageRepository.save(new DsMessage("o2", "bbb_bbb_bbb"));
		dsMessageRepository.save(new DsMessage("o3", "ccc_ccc_ccc"));
		Assert.assertEquals(3, dsMessageRepository.findAll().size());
	}


	@Test
	public void redisUserTest() {
        // 保存字符串
        stringRedisTemplate.opsForValue().set("aaa", "111");
        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));

        // 保存对象
        RedisUser user = new RedisUser("超人", 20);
        redisUserRedisTemplate.opsForValue().set(user.getUsername(), user);

        user = new RedisUser("蝙蝠侠", 30);
        redisUserRedisTemplate.opsForValue().set(user.getUsername(), user);

        user = new RedisUser("蜘蛛侠", 40);
        redisUserRedisTemplate.opsForValue().set(user.getUsername(), user);

        Assert.assertEquals(20, redisUserRedisTemplate.opsForValue().get("超人").getAge().longValue());
        Assert.assertEquals(30, redisUserRedisTemplate.opsForValue().get("蝙蝠侠").getAge().longValue());
        Assert.assertEquals(40, redisUserRedisTemplate.opsForValue().get("蜘蛛侠").getAge().longValue());

    }


    @Test
    public void mongoDbUserTest() {

		// 创建三个MongoDbUser，并验证MongoDbUser总数
		mongoDbUserRepository.save(new MongoDbUser(1L, "didi", 30));
		mongoDbUserRepository.save(new MongoDbUser(2L, "mama", 40));
		mongoDbUserRepository.save(new MongoDbUser(3L, "kaka", 50));
		Assert.assertEquals(3, mongoDbUserRepository.findAll().size());

		// 删除一个MongoDbUser，再验证MongoDbUser总数
		MongoDbUser u = mongoDbUserRepository.findById(1L).get();
		mongoDbUserRepository.delete(u);
		Assert.assertEquals(2, mongoDbUserRepository.findAll().size());

		// 删除一个MongoDbUser，再验证MongoDbUser总数
		u = mongoDbUserRepository.findByUsername("mama");
		mongoDbUserRepository.delete(u);
		Assert.assertEquals(1, mongoDbUserRepository.findAll().size());


	}

	@Test
	@Rollback
	public void mybatisUserTest() {

		// insert一条数据，并select出来验证
		mybatisUserMapper.insert("AAA", 20);
		MybatisUser mybatisUser = mybatisUserMapper.findByName("AAA");
		Assert.assertEquals(20, mybatisUser.getAge().intValue());

		// update一条数据，并select出来验证
		mybatisUser.setAge(30);
		mybatisUserMapper.update(mybatisUser);
		mybatisUser = mybatisUserMapper.findByName("AAA");
		Assert.assertEquals(30, mybatisUser.getAge().intValue());

		// 删除这条数据，并select验证
		mybatisUserMapper.delete(mybatisUser.getId());
		mybatisUser = mybatisUserMapper.findByName("AAA");
		Assert.assertEquals(null, mybatisUser);

		mybatisUser = new MybatisUser("BBB", 30);
		mybatisUserMapper.insertByUser(mybatisUser);
		Assert.assertEquals(30, mybatisUserMapper.findByName("BBB").getAge().intValue());

		Map<String, Object> map = new HashMap<>();
		map.put("username", "CCC");
		map.put("age", 40);
		mybatisUserMapper.insertByMap(map);
		Assert.assertEquals(40, mybatisUserMapper.findByName("CCC").getAge().intValue());

		List<MybatisUser> mybatisUserList = mybatisUserMapper.findAll();
		for(MybatisUser user : mybatisUserList) {
			Assert.assertEquals(null, user.getId());
			Assert.assertNotEquals(null, user.getUsername());
		}

	}

}
