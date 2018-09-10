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
import org.springframework.context.annotation.Configuration;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ApplicationStartingEvent
 * ApplicationEnvironmentPreparedEvent
 * ApplicationPreparedEvent
 * ApplicationStartedEvent <= 新增的事件
 * ApplicationReadyEvent
 * ApplicationFailedEvent
 */
@EnableAsync
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


	/**
	 * 在Spring Boot主类中定义一个线程池
	 */
	@Configuration
	class TaskPoolConfig {

		@Bean("taskExecutor")
		public Executor taskExecutor() {
			ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
			executor.setCorePoolSize(10);                   //线程池创建时候初始化的线程数
			executor.setMaxPoolSize(20);                    //线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
			executor.setQueueCapacity(200);                 //用来缓冲执行任务的队列
			executor.setKeepAliveSeconds(60);               //当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
			executor.setThreadNamePrefix("taskExecutor-");  //设置好了之后可以方便我们定位处理任务所在的线程池
			executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
			return executor;
		}

	}

}
