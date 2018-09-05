package com.oofgz.fight;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mongodb.MongoClient;
import com.oofgz.fight.bean.JpaDept;
import com.oofgz.fight.bean.Person;
import com.oofgz.fight.bean.User;
import com.oofgz.fight.controller.GreetingController;
import com.oofgz.fight.controller.UserController;
import com.oofgz.fight.repository.JpaDeptRepository;
import com.oofgz.fight.repository.PersonRepository;
import com.oofgz.fight.service.BlogProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.naming.Name;
import java.util.List;
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
	private PersonRepository personRepository;

	@Autowired
	private MongoClient mongoClient;

	@Autowired
	private JpaDeptRepository jpaDeptRepository;

	@Autowired
	private BlogProperties blogProperties;

	@Test
	public void contextLoads() {

	}

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(
				new GreetingController(),
				new UserController()
		).build();
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
		person.setDescription("说明情况,,,修改后的结果");
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
}
