package niffler.db.jpa;

import niffler.config.Config;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import niffler.db.ServiceDB;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum EmfProvider {
    INSTANCE;

    private final Map<ServiceDB, EntityManagerFactory> emfStore = new ConcurrentHashMap<>();

    public EntityManagerFactory getEmf(ServiceDB service) {
        return emfStore.computeIfAbsent(service, serviceDB -> {
            Map<String, Object> properties = new HashMap<>();
            properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            properties.put("hibernate.connection.driver_class", "org.postgresql.Driver");
            properties.put("hibernate.connection.username", Config.getConfig().getDBLogin());
            properties.put("hibernate.connection.password", Config.getConfig().getDBPassword());
            properties.put("hibernate.connection.url", service.getJdbcUrl());

            return new ThreadLocalEmf(Persistence.createEntityManagerFactory(
                    "niffler-persistence-unit-name",
                    properties
            ));
        });
    }
}
