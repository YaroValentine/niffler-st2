package niffler.config;

public interface Config {

    static Config getConfig() {
        if ("docker".equals(System.getProperty("env"))) {
            return new DockerConfig();
        }
        return new LocalConfig();
    }

    String getDBHost();

    String getDBLogin();

    String getDBPassword();

    int getDBPort();

    String getSpendUrl();

    String getAuthUrl();

    String getFrontUrl();

    String getUserdataUrl();
}
