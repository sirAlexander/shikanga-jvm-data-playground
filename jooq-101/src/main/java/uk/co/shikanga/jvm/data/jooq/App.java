package uk.co.shikanga.jvm.data.jooq;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import uk.co.shikanga.jvm.data.jooq.public_.tables.daos.UsersDao;
import uk.co.shikanga.jvm.data.jooq.public_.tables.pojos.Users;
import uk.co.shikanga.jvm.data.jooq.public_.tables.records.UsersRecord;

import javax.sql.DataSource;

import static uk.co.shikanga.jvm.data.jooq.public_.tables.Users.USERS;

public class App {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyConfig.class);
        UsersDao usersDao = ctx.getBean(UsersDao.class);
        DSLContext dslContext = ctx.getBean(DSLContext.class);

        Users users = new Users();
        users.setEmail("my@friend.com");
        usersDao.insert(users);

        usersDao.fetchOne(USERS.EMAIL, "my@friend.com");

        // dslContext.insertInto(USERS, USERS.EMAIL).values("my@friend.com").execute();
                /*UsersRecord usersRecord = dslContext.newRecord(USERS);
                usersRecord.setEmail("my@friend.com");
                usersRecord.store();*/

                /* dslContext.update(USERS)
                        .set(USERS.EMAIL, "something@else.com")
                        .where(USERS.EMAIL.startsWith("my@"))
                        .execute(); */


        UsersRecord usersRecord1 = dslContext.fetchOne(USERS, USERS.EMAIL.startsWith("my@"));
        usersRecord1.setEmail("something@else.com");
        usersRecord1.store();

        dslContext.deleteFrom(USERS)
                .where(USERS.EMAIL.eq("something@else.com"))
                .execute();

        Result<UsersRecord> records = dslContext
                .selectFrom(USERS)
                .fetch();
        records.forEach(App::logRecordDetails);

        //implicit commit
        // DSLContext

    }

    private static void logRecordDetails(UsersRecord usersRecord) {
        System.out.println(usersRecord.getEmail() + " " + usersRecord.getId());
    }

    @org.springframework.context.annotation.Configuration
    @ComponentScan
    public static class MyConfig {

        @Bean
        public DataSource dataSource() {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl("jdbc:h2:file:~/tools/mydb");
            hikariConfig.setUsername("admin");
            hikariConfig.setPassword("admin");

            return new HikariDataSource(hikariConfig);
        }

        @Bean
        public DataSourceConnectionProvider dataSourceConnectionProvider() {
            return new DataSourceConnectionProvider(dataSource());
        }

        @Bean
        public DefaultDSLContext defaultDSLContext() {
            return new DefaultDSLContext(configuration());
        }

        @Bean
        public Configuration configuration() {
            DefaultConfiguration defaultConfiguration = new DefaultConfiguration();
            defaultConfiguration.setConnectionProvider(dataSourceConnectionProvider());
            defaultConfiguration.setSQLDialect(SQLDialect.H2);
            return defaultConfiguration;
        }
    }
}
