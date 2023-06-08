package niffler.db.dao;

import niffler.db.DataSourceProvider;
import niffler.db.ServiceDB;
import niffler.db.entity.Authority;
import niffler.db.entity.AuthorityEntity;
import niffler.db.entity.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NifflerUsersDAOJdbc implements NifflerUsersDAO {
    private static final DataSource dataSource = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.NIFFLER_AUTH);
    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public int createUser(UserEntity user) {
        int executeUpdate;

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement1 = connection.prepareStatement("INSERT INTO users "
                    + "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) "
                    + " VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

                 PreparedStatement statement2 = connection.prepareStatement(
                         "INSERT INTO authorities (user_id, authority) VALUES (?, ?)")) {

                statement1.setString(1, user.getUsername());
                statement1.setString(2, passwordEncoder.encode(user.getPassword()));
                statement1.setBoolean(3, user.getEnabled());
                statement1.setBoolean(4, user.getAccountNonExpired());
                statement1.setBoolean(5, user.getAccountNonLocked());
                statement1.setBoolean(6, user.getCredentialsNonExpired());

                executeUpdate = statement1.executeUpdate();


                final UUID finalUserId;

                try (ResultSet generatedKeys = statement1.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        finalUserId = UUID.fromString(generatedKeys.getString(1));
                        user.setId(finalUserId);
                    } else {
                        throw new SQLException("User ID not found.");
                    }
                }

                for (AuthorityEntity authority : user.getAuthorities()) {
                    statement2.setObject(1, finalUserId);
                    statement2.setString(2, authority.getAuthority().name());
                    statement2.addBatch();
                    statement2.clearParameters();
                }
                statement2.executeBatch();
            } catch (SQLException e) {
                connection.rollback();
                connection.setAutoCommit(true);
                throw new RuntimeException(e);
            }

            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return executeUpdate;
    }

    @Override
    public UserEntity readUser(UUID uuid) {

        UserEntity userEntity = new UserEntity();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement1 = connection.prepareStatement("SELECT * FROM users WHERE id=(?)");
             PreparedStatement statement2 = connection.prepareStatement("SELECT * FROM authorities WHERE user_id=(?)")) {

            statement1.setObject(1, uuid);
            ResultSet resultSet1 = statement1.executeQuery();

            if (resultSet1.next()) {
                userEntity.setId(UUID.fromString(resultSet1.getString(1)));
                userEntity.setUsername(resultSet1.getString(2));
                userEntity.setPassword(resultSet1.getString(3));
                userEntity.setEnabled(resultSet1.getBoolean(4));
                userEntity.setAccountNonExpired(resultSet1.getBoolean(5));
                userEntity.setAccountNonLocked(resultSet1.getBoolean(6));
                userEntity.setCredentialsNonExpired(resultSet1.getBoolean(7));
            } else {
                throw new IllegalArgumentException("User UUID not found");
            }

            statement2.setObject(1, uuid);
            ResultSet resultSet2 = statement2.executeQuery();
            List<AuthorityEntity> authorities = new ArrayList<>();

            while (resultSet2.next()) {
                AuthorityEntity authorityEntity = new AuthorityEntity();
                authorityEntity.setId(UUID.fromString(resultSet2.getString(1)));
                authorityEntity.setAuthority(Authority.valueOf(resultSet2.getString(3)));
                authorities.add(authorityEntity);

                userEntity.setAuthorities(authorities);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return userEntity;
    }

    @Override
    public int updateUser(UserEntity user) {
        int executeUpdate;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE users SET "
                     + "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)="
                     + "(?, ?, ?, ?, ?, ?) WHERE id=(?)")) {

            statement.setString(1, user.getUsername());
            statement.setString(2, passwordEncoder.encode(user.getPassword()));
            statement.setBoolean(3, user.getEnabled());
            statement.setBoolean(4, user.getAccountNonExpired());
            statement.setBoolean(5, user.getAccountNonLocked());
            statement.setBoolean(6, user.getCredentialsNonExpired());
            statement.setObject(7, user.getId());

            executeUpdate = statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return executeUpdate;
    }

    @Override
    public int removeUser(UserEntity user) {
        int executeUpdate;
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement deleteAuthSt = connection.prepareStatement("DELETE FROM authorities WHERE user_id=(?)");
                 PreparedStatement deleteUserSt = connection.prepareStatement("DELETE FROM users WHERE id=(?)")) {

                UUID userId = UUID.fromString(getUserId(user.getUsername()));
                deleteAuthSt.setObject(1, userId);
                deleteAuthSt.executeUpdate();

                deleteUserSt.setObject(1, userId);
                executeUpdate = deleteUserSt.executeUpdate();

            } catch (SQLException e) {
                connection.rollback();
                connection.setAutoCommit(true);
                throw new RuntimeException(e);
            }

            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return executeUpdate;
    }

    @Override
    public String getUserId(String userName) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement st = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            st.setString(1, userName);
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            } else {
                throw new IllegalArgumentException("Can`t find user by given username: " + userName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

