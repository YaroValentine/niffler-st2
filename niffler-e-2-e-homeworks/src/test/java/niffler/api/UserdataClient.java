package niffler.api;

import niffler.model.UserJson;

import java.io.IOException;

public class UserdataClient extends BaseRestClient {

    private final UserdataService userService = retrofit.create(UserdataService.class);

    public UserdataClient() {
        super(CFG.getUserdataUrl());
    }

    public UserJson getUser(String username) {
        try {
            return userService.currentUser(username).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
