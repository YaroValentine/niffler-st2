package niffler.db.dao;

import niffler.db.entity.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public interface NifflerUsersDAO {
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    int createUser(UserEntity user);

    UserEntity readUser(UUID uuid);

    int updateUser(UserEntity user);

    int removeUser(UserEntity user);

    String getUserId(String userName);
}
