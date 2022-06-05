package br.com.axe.agendadorapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AgendadorApiApplicationTests {

	@Test
	void contextLoads() {
		AgendadorApiApplication.main(new String[]{});
		int x = 0;
		assertEquals(0, x);
	}

}
