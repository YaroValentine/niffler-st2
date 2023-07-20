package niffler.config;


public class DockerConfig implements Config {

    static final DockerConfig INSTANCE = new DockerConfig();

    private DockerConfig() {
    }

    @Override
    public String getDBHost() {
        return "niffler-all-db";
    }

    @Override
    public String getDBLogin() {
        return "postgres";
    }

    @Override
    public String getDBPassword() {
        return "secret";
    }

    @Override
    public int getDBPort() {
        return 5432;
    }

    @Override
    public String getSpendUrl() {
        return "http://niffler-spend:8093";
    }

    @Override
    public String getFrontUrl() {
        return "http://niffler-frontend";
    }

    @Override
    public String getUserdataUrl() {
        return null;
    }

    @Override
    public String getCurrencyGrpcAddress() {
        return "http://niffler-currency:8091";
    }

    @Override
    public int getCurrencyGrpcPort() {
        return 8092;
    }

    @Override
    public String getAuthUrl() {
        return "http://niffler-auth:9000";
    }

    @Override
    public String getUserdataGrpcAddress() {
        return "http://niffler-userdata:8089";
    }

    @Override
    public int getUserdataGrpcPort() {
        return 8090;
    }
}
