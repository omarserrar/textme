package com.omarserrar.textme;

import com.omarserrar.textme.models.user.User;
import com.omarserrar.textme.models.user.UserRepository;
import com.omarserrar.textme.util.JWTUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TextMeApplicationTests {
	@Autowired
	UserRepository userRepository;
	@Test
	void contextLoads() {
	}

	@Test
	void testJWT(){
		User u = userRepository.findById(202L).orElseThrow();
		JWTUtils.getUserJWT(u);
	}

}
