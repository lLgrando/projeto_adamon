package tech.ada.adamon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class AdamonApplicationTests {

	@Value("${legume.cozinha}")
	String value;

	@Test
	void contextLoads() {
		System.out.println(value);
	}

	
}
