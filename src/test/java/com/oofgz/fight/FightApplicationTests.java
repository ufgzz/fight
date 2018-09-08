package com.oofgz.fight;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.oofgz.fight.controller.GreetingController;
import com.oofgz.fight.controller.RestfulUserController;
import com.oofgz.fight.dto.restful.RestfulUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

	@Autowired
	private RestfulUserController restfulUserController;

	@Autowired
	private GreetingController greetingController;

	private MockMvc mockMvc;


	@Test
	public void contextLoads() {

	}

	@Before
	public void setUp() {
		log.info("测试Restful的功能(JdbcTemplate操作数据库)");
		mockMvc = MockMvcBuilders.standaloneSetup(
				greetingController,
				restfulUserController
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
		RestfulUser restfulUser;
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

		String initExceptStr = "[{\"name\":\"郑福根\",\"nameSpell\":\"zfg\",\"age\":\"30\",\"phone\":\"15980278588\",\"profession\":\"高级软件工程师\"}]";
		String updateExceptStr = "{\"name\":\"正富贵\",\"nameSpell\":\"zfg\",\"age\":\"30\",\"phone\":\"15980278588\",\"profession\":\"超级英雄\"}";

		// 0、先清空所有用户
		requestBuilder = delete("/RestfulUser/deleteAllUsers");
		mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(content().string(equalTo("delete all success")));

		// 1、get查一下user列表，应该为空
		requestBuilder = get("/RestfulUser/getUserList");
		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("[]")));

		// 2、post提交一个user
		restfulUser = new RestfulUser();
		restfulUser.setName("郑福根");
		restfulUser.setAge("30");
		restfulUser.setPhone("15980278588");
		restfulUser.setProfession("高级软件工程师");
		requestBuilder = post("/RestfulUser/postUser")
				.contentType(APPLICATION_JSON).content(ow.writeValueAsString(restfulUser));
		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().string(equalTo("add success")));

		// 3、get获取user列表，应该有刚才插入的数据
		requestBuilder = get("/RestfulUser/getUserList");
		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(content().string(equalTo(initExceptStr)));

		// 4、put修改id为1的user
		restfulUser = new RestfulUser();
		restfulUser.setName("正富贵");
		restfulUser.setAge("30");
		restfulUser.setPhone("15980278588");
		restfulUser.setProfession("超级英雄");
		requestBuilder = put("/RestfulUser/zfg")
				.contentType(APPLICATION_JSON).content(ow.writeValueAsString(restfulUser));
		mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(content().string(equalTo("put update success")));

		// 5、get一个id为1的user
		requestBuilder = get("/RestfulUser/zfg");
		mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(content().string(equalTo(updateExceptStr)));

		// 6、delete删除id为1的user
		requestBuilder = delete("/RestfulUser/zfg");
		mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(content().string(equalTo("delete success")));

		// 7、get查一下user列表，应该为空
		requestBuilder = get("/RestfulUser/getUserList");
		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("[]")));

	}

}
