package niffler.test.grpc;

import com.google.protobuf.Descriptors;
import guru.qa.grpc.niffler.grpc.UserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class NifflerUserdataGrpcTest extends BaseGrpcTest {

    @Test
    public void testGetAllUsers() {
        UserResponse allUsers = userdataStub.getAllUsers(EMPTY);
        Map<Descriptors.FieldDescriptor, Object> allFields = allUsers.getAllFields();
        Assertions.assertTrue(!allFields.isEmpty());
    }

}