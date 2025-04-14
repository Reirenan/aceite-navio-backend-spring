package br.com.treinaweb.twjobs.api;

import br.com.treinaweb.twjobs.core.permissions.TWJobsPermissions;
import br.com.treinaweb.twjobs.core.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UsersRestController {


    public final UserRepository userRepository;

//    @GetMapping("statistics/count")
//    @TWJobsPermissions.IsCompany
//    public Long howMany() {
//        return userRepository.countAllUsers();
//    }



}
