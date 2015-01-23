package ch.rasc.wampspring.demo.client.server;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import reactor.Environment;
import reactor.jarjar.com.lmax.disruptor.YieldingWaitStrategy;
import reactor.jarjar.com.lmax.disruptor.dsl.ProducerType;
import reactor.spring.context.config.EnableReactor;
import reactor.spring.core.task.RingBufferAsyncTaskExecutor;
import ch.rasc.wampspring.config.DefaultWampConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@EnableScheduling
@EnableReactor
public class Server extends DefaultWampConfiguration {

	public static void main(String[] args) {
		SpringApplication.run(Server.class, args);
	}

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	Environment env;

	@Override
	public ObjectMapper objectMapper() {
		return this.objectMapper;
	}

	@Override
	@Bean
	public Executor clientInboundChannelExecutor() {
		RingBufferAsyncTaskExecutor executor = new RingBufferAsyncTaskExecutor(this.env);
		executor.setName("clientInboundChannelExecutor");
		executor.setBacklog(2048);
		executor.setProducerType(ProducerType.MULTI);
		executor.setWaitStrategy(new YieldingWaitStrategy());
		return executor;
	}

	@Override
	@Bean
	public Executor clientOutboundChannelExecutor() {
		RingBufferAsyncTaskExecutor executor = new RingBufferAsyncTaskExecutor(this.env);
		executor.setName("clientOutboundChannelExecutor");
		executor.setBacklog(2048);
		executor.setProducerType(ProducerType.MULTI);
		executor.setWaitStrategy(new YieldingWaitStrategy());
		return executor;
	}

	@Override
	@Bean
	public Executor brokerChannelExecutor() {
		RingBufferAsyncTaskExecutor executor = new RingBufferAsyncTaskExecutor(this.env);
		executor.setName("brokerChannelExecutor");
		executor.setBacklog(1024);
		executor.setProducerType(ProducerType.SINGLE);
		executor.setWaitStrategy(new YieldingWaitStrategy());
		return executor;
	}

}
