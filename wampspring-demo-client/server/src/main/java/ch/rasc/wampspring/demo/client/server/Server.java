/**
 * Copyright 2015-2015 Ralph Schaer <ralphschaer@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.rasc.wampspring.demo.client.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpEncodingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.rasc.wampspring.config.DefaultWampConfiguration;
import ch.rasc.wampspring.config.WampEndpointRegistry;

@Configuration
@EnableAutoConfiguration(
		exclude = { HttpEncodingAutoConfiguration.class, WebMvcAutoConfiguration.class })
public class Server extends DefaultWampConfiguration {

	@Override
	protected void registerWampEndpoints(WampEndpointRegistry registry) {
		registry.addEndpoint("/wamp");
		registry.addEndpoint("/wampOverSockJS").withSockJS()
				.setStreamBytesLimit(512 * 1024).setHttpMessageCacheSize(1000)
				.setDisconnectDelay(30 * 1000);
	}

	public static void main(String[] args) {
		SpringApplication.run(Server.class, args);
	}

	@Bean
	public TestService testService() {
		return new TestService();
	}

}
