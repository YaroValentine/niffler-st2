package niffler.config;

public interface Config {

    static Config getConfig() {
        if ("docker".equals(System.getProperty("test.env"))) {
            return DockerConfig.INSTANCE;
        } else if ("local".equals(System.getProperty("test.env"))) {
            return LocalConfig.INSTANCE;
        } else {
            throw new IllegalStateException("Unable to resolve 'test.env' System Property");
        }
    }

    String getDBHost();

    String getDBLogin();

    String getDBPassword();

    int getDBPort();

    String getSpendUrl();

    String getAuthUrl();

    String getFrontUrl();

    String getUserdataUrl();

    String getCurrencyGrpcAddress();
    int getCurrencyGrpcPort();
    String getUserdataGrpcAddress();
    int getUserdataGrpcPort();
}
