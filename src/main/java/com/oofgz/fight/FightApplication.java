package com.oofgz.fight;

import com.oofgz.fight.properties.FooProperties;
import com.oofgz.fight.properties.PostInfo;
import com.spring4all.mongodb.EnableMongoPlus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

/**
 * ApplicationStartingEvent
 * ApplicationEnvironmentPreparedEvent
 * ApplicationPreparedEvent
 * ApplicationStartedEvent <= 新增的事件
 * ApplicationReadyEvent
 * ApplicationFailedEvent
 */
@EnableMongoPlus
@EnableScheduling
@EnableLdapRepositories
@EnableConfigurationProperties
@SpringBootApplication
public class FightApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(FightApplication.class, args);
		Binder binder = Binder.get(context.getEnvironment());

		// 绑定简单配置
		FooProperties foo = binder.bind("com.zfgoo", Bindable.of(FooProperties.class)).get();
		System.out.println(foo.getFoo());

		// 绑定List配置
		List<String> post = binder.bind("com.zfgoo.post", Bindable.listOf(String.class)).get();
		System.out.println(post);

		List<PostInfo> posts = binder.bind("com.zfgoo.posts", Bindable.listOf(PostInfo.class)).get();
		System.out.println(posts);

		// 读取配置，两种写法是一个样的
		System.out.println(context.getEnvironment().containsProperty("com.zfgoo.database-platform"));
		System.out.println(context.getEnvironment().containsProperty("com.zfgoo.databasePlatform"));

	}

	/**
	 * ApplicationStartedEvent和ApplicationReadyEvent
	 * 中间还有一个过程就是command-line runners被调用的内容
	 * @return
	 */
	@Bean
	public DataLoader dataLoader () {
		return new DataLoader();
	}


	@Slf4j
	static class DataLoader implements CommandLineRunner {
		@Override
		public void run(String... args) {
			log.info("Loading data...");
		}
	}

}
