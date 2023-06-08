package niffler.db.dao;import niffler.db.DataSourceProvider;import niffler.db.ServiceDB;import niffler.db.entity.Authority;import niffler.db.entity.AuthorityEntity;import niffler.db.entity.UserEntity;import org.springframework.dao.EmptyResultDataAccessException;import org.springframework.jdbc.core.JdbcTemplate;import org.springframework.jdbc.datasource.DataSourceTransactionManager;import org.springframework.jdbc.support.GeneratedKeyHolder;import org.springframework.jdbc.support.JdbcTransactionManager;import org.springframework.jdbc.support.KeyHolder;import org.springframework.transaction.support.TransactionTemplate;import java.sql.PreparedStatement;import java.util.ArrayList;import java.util.List;import java.util.UUID;import java.util.stream.IntStream;public class NifflerUsersDAOSpringJdbc implements NifflerUsersDAO {    private final TransactionTemplate transactionTemplate;    private final JdbcTemplate jdbcTemplate;    @SuppressWarnings("ConstantConditions")    public NifflerUsersDAOSpringJdbc() {        DataSourceTransactionManager transactionManager = new JdbcTransactionManager(                DataSourceProvider.INSTANCE.getDataSource(ServiceDB.NIFFLER_AUTH));        this.transactionTemplate = new TransactionTemplate(transactionManager);        this.jdbcTemplate = new JdbcTemplate(transactionManager.getDataSource());    }    @SuppressWarnings("ConstantConditions")    @Override    public int createUser(UserEntity user) {        final String insertUserSql = "INSERT INTO users (" +                "username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +                "VALUES (?, ?, ?, ?, ?, ?)";        final String insertAuthoritySql = "INSERT INTO authorities (user_id, authority) VALUES (?, ?)";        return transactionTemplate.execute(transactionStatus -> {            KeyHolder keyHolder = new GeneratedKeyHolder();            jdbcTemplate.update(connection -> {                PreparedStatement ps = connection.prepareStatement(insertUserSql, new String[]{"id"});                ps.setString(1, user.getUsername());                ps.setString(2, passwordEncoder.encode(user.getPassword()));                ps.setBoolean(3, user.getEnabled());                ps.setBoolean(4, user.getAccountNonExpired());                ps.setBoolean(5, user.getAccountNonLocked());                ps.setBoolean(6, user.getCredentialsNonExpired());                return ps;            }, keyHolder);            UUID finalUserId = (UUID) keyHolder.getKeyList().get(0).get("id");            user.setId(finalUserId);            List<Object[]> authorities = user.getAuthorities().stream()                    .map(authority -> new Object[]{finalUserId, authority.getAuthority().name()})                    .toList();            jdbcTemplate.batchUpdate(insertAuthoritySql, authorities);            return 1;        });    }    @Override    public UserEntity readUser(UUID uuid) {        final String selectUserSql = "SELECT * FROM users WHERE id = ?";        try {            UserEntity user = jdbcTemplate.queryForObject(selectUserSql, new Object[]{uuid.toString()}, (rs, rowNum) -> {                UserEntity newUser = new UserEntity();                newUser.setId(UUID.fromString(rs.getString("id")));                newUser.setUsername(rs.getString("username"));                newUser.setEnabled(rs.getBoolean("enabled"));                newUser.setAccountNonExpired(rs.getBoolean("account_non_expired"));                newUser.setAccountNonLocked(rs.getBoolean("account_non_locked"));                newUser.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));                return newUser;            });            final String selectAuthoritiesSql = "SELECT * FROM authorities WHERE user_id = ?";            List<AuthorityEntity> authorities = jdbcTemplate.query(                    selectAuthoritiesSql, new Object[]{uuid.toString()}, (rs, rowNum) -> {                        AuthorityEntity authorityEntity = new AuthorityEntity();                        authorityEntity.setId(UUID.fromString(rs.getString("id")));                        authorityEntity.setAuthority(Authority.valueOf(rs.getString("authority")));                        return authorityEntity;                    });            user.setAuthorities(authorities);            return user;        } catch (EmptyResultDataAccessException e) {            return null;        }    }    @Override    public int updateUser(UserEntity user) {        final String updateUserSql = "UPDATE users SET "                + "username = ?, password = ?, enabled = ?, "                + "account_non_expired = ?, account_non_locked = ?, "                + "credentials_non_expired = ? WHERE id = ?";        int userUpdate = jdbcTemplate.update(updateUserSql,                user.getUsername(),                passwordEncoder.encode(user.getPassword()),                user.getEnabled(),                user.getAccountNonExpired(),                user.getAccountNonLocked(),                user.getCredentialsNonExpired(),                user.getId().toString());        final String deleteAuthoritiesSql = "DELETE FROM authorities WHERE user_id = ?";        jdbcTemplate.update(deleteAuthoritiesSql, user.getId().toString());        final String insertAuthoritySql = "INSERT INTO authorities (user_id, authority) VALUES (?, ?)";        List<Object[]> authorities = user.getAuthorities().stream()                .map(authority -> new Object[]{user.getId().toString(), authority.getAuthority().name()})                .toList();        int[] authoritiesUpdates = jdbcTemplate.batchUpdate(insertAuthoritySql, authorities);        int totalAuthoritiesUpdated = IntStream.of(authoritiesUpdates).sum();        return userUpdate + totalAuthoritiesUpdated;    }    @Override    public String getUserId(String userName) {        return jdbcTemplate.query("SELECT id FROM users WHERE username = ?",                rs -> {                    return rs.getString("id");                },                userName        );    }    @SuppressWarnings("ConstantConditions")    @Override    public int removeUser(UserEntity user) {        return transactionTemplate.execute(status -> {            jdbcTemplate.update("DELETE FROM authorities WHERE user_id = ?", user.getId());            return jdbcTemplate.update("DELETE FROM users WHERE id = ?", user.getId());        });    }}