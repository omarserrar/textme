package com.omarserrar.textme;

import com.omarserrar.textme.models.responses.RegisterResponse;
import com.omarserrar.textme.models.user.User;
import com.omarserrar.textme.models.user.UserRepository;
import com.omarserrar.textme.services.AuthenticationService;
import com.omarserrar.textme.util.JWTUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class TestAuth {
    @Autowired
    AuthenticationService authService;
    @Test
    void testRegisterSuccess() throws Exception {
        User u = User.builder().guest(false).firstName("Omar").lastName("Omaar").username("user11121").password("555eea").build();
        RegisterResponse r = authService.createUser(u);
        String jwt=r.getJwt();
        User uu = r.getU();
        Long id = JWTUtils.getUserId(jwt);
        Assertions.assertEquals(uu.getId(), id);
        Assertions.assertEquals(false, uu.getGuest());
    }
    @Test
    void testRegisterSuccess2() throws Exception {
        User u = User.builder().firstName("Omar").lastName("Omaar").username("user111221").password("555eea").build();
        RegisterResponse r = authService.createUser(u);
        String jwt=r.getJwt();
        User uu = r.getU();
        Long id = JWTUtils.getUserId(jwt);
        Assertions.assertEquals(uu.getId(), id);
        Assertions.assertEquals(false, uu.getGuest());
    }
    @Test
    void testRegisterFail1() throws Exception {
        User u = User.builder().firstName("Omar").lastName("Omaar").username("omarser").password("555eea").build();
        Assertions.assertThrowsExactly(Exception.class, ()->{authService.createUser(u);});
    }
    @Test
    void testRegisterFail2() throws Exception {
        User u = User.builder().firstName("Omar").lastName("Omaar").username("e").password("555eea").build();
        Assertions.assertThrowsExactly(Exception.class, ()->{authService.createUser(u);});
    }
    @Test
    void testRegisterFail3() throws Exception {
        User u = User.builder().firstName("Omar").lastName("Omaar").username("oaameed").password("1").build();
        Assertions.assertThrowsExactly(Exception.class, ()->{authService.createUser(u);});
    }
    @Test
    void testRegisterGuestSuccess1() throws Exception {
        User u = User.builder().guest(true).firstName("Omar").lastName("Omaar").build();
        RegisterResponse r = authService.createUser(u);
        String jwt=r.getJwt();
        User uu = r.getU();
        Long id = JWTUtils.getUserId(jwt);
        Assertions.assertEquals(uu.getId(), id);
        Assertions.assertEquals(true, uu.getGuest());
    }
    @Test
    void testRegisterGuestFail3() throws Exception {
        User u = User.builder().firstName("Omar").lastName("Omaar").build();
        Assertions.assertThrowsExactly(Exception.class, ()->{authService.createUser(u);});
    }
    @Test
    void testRegisterGuestFail1() throws Exception {
        User u = User.builder().guest(true).firstName("dd").lastName("Omaar").build();
        Assertions.assertThrowsExactly(Exception.class, ()->{authService.createUser(u);});
    }
    @Test
    void testRegisterGuestFail2() throws Exception {
        User u = User.builder().guest(true).firstName("dd").lastName("").build();
        Assertions.assertThrowsExactly(Exception.class, ()->{authService.createUser(u);});
    }

}
