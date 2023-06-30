package niffler.test.grpc;

import com.google.protobuf.Descriptors;
import guru.qa.grpc.niffler.grpc.User;
import guru.qa.grpc.niffler.grpc.UserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NifflerUserdataGrpcTest extends BaseGrpcTest {

    @Test
    public void testGetAllUsers() {
        UserResponse allUsers = userdataStub.getAllUsers(EMPTY);

        boolean userExists = allUsers.getUsersList().stream()
                .anyMatch(u -> u.getUsername().equals("yaro"));

        Assertions.assertTrue(userExists);
    }

}