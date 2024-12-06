package com.stream.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.stream.app.services.VideoService;

@SpringBootTest
class SpringStreamBackendApplicationTests {

	@Autowired
	private VideoService videoService;

	@Test
	void contextLoads() {
		videoService.processVideo("c227e4bc-ac3a-4cd3-951a-fd55c0cd99ee");
	}

}
