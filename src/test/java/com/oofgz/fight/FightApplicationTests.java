package com.oofgz.fight;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.oofgz.fight.entity.User;
import com.oofgz.fight.controller.GreetingController;
import com.oofgz.fight.controller.UserController;
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
	private UserController userController;

	private MockMvc mockMvc;


	@Test
	public void contextLoads() {

	}

	@Before
	public void setUp() {
		log.info("测试Restful的功能(JdbcTemplate操作数据库)");
		mockMvc = MockMvcBuilders.standaloneSetup(
				new GreetingController(),
				userController
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

		String initExceptStr = "[{\"name\":\"TestMaster\",\"age\":\"20\",\"phone\":\"15980278580\",\"profession\":\"软件工程师\"}]";
		String updateExceptStr = "{\"name\":\"SuperTestMaster\",\"age\":\"30\",\"phone\":\"15980278580\",\"profession\":\"软件工程师\"}";

		// 0、先清空所有用户
		requestBuilder = delete("/User/deleteAllUsers");
		mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(content().string(equalTo("delete all success")));

		// 1、get查一下user列表，应该为空
		requestBuilder = get("/User/getUserList");
		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("[]")));

		// 2、post提交一个user
		user = new User();
		user.setName("TestMaster");
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
		user.setName("SuperTestMaster");
		user.setAge("30");
		requestBuilder = put("/User/TestMaster")
				.contentType(APPLICATION_JSON).content(ow.writeValueAsString(user));
		mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(content().string(equalTo("put update success")));

		// 5、get一个id为1的user
		requestBuilder = get("/User/SuperTestMaster");
		mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(content().string(equalTo(updateExceptStr)));

		// 6、delete删除id为1的user
		requestBuilder = delete("/User/SuperTestMaster");
		mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(content().string(equalTo("delete success")));

		// 7、get查一下user列表，应该为空
		requestBuilder = get("/User/getUserList");
		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("[]")));

	}

}
