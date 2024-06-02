package com.commitfarm.farm.service;

import com.commitfarm.farm.domain.Users;
import com.commitfarm.farm.dto.user.CreateUserReq;
import com.commitfarm.farm.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public void login(String email, String password) throws Exception {
        Users user = usersRepository.findByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            throw new Exception("아이디 또는 비밀번호를 잘못 입력했습니다");
        }
    }

    public void createUser(CreateUserReq createUserReq) {
        Users user = new Users();
        user.setUsername(createUserReq.getUsername());
        user.setPassword(createUserReq.getPassword());
        user.setEmail(createUserReq.getEmail());
        user.setAdmin(false);

        usersRepository.save(user);
    }

    public Users findUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }



}
