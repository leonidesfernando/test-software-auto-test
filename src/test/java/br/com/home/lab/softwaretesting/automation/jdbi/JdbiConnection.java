package br.com.home.lab.softwaretesting.automation.jdbi;

import br.com.home.lab.softwaretesting.automation.config.Configurations;
import lombok.Getter;
import org.aeonbits.owner.ConfigFactory;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.SqlStatements;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;


public class JdbiConnection {

    private static final Configurations config = ConfigFactory.create(Configurations.class);

    @Getter
    private final Jdbi jdbi;

    public JdbiConnection(){
        jdbi = Jdbi.create(config.dbUrl(), config.dbUsername(), config.dbPassword());
        jdbi.installPlugin(new SqlObjectPlugin());
        jdbi.getConfig(SqlStatements.class).setUnusedBindingAllowed(true);
    }
}
