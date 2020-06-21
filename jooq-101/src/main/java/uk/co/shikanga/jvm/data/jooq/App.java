package uk.co.shikanga.jvm.data.jooq;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static uk.co.shikanga.jvm.data.jooq.public_.tables.Users.USERS;

public class App {

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(
                "jdbc:h2:file:~/tools/mydb",
                "admin",
                "admin")) {
            DSLContext dslContext = DSL.using(connection);

            Result<Record2<Integer, String>> records = dslContext
                    .select(USERS.ID, USERS.EMAIL)
                    .from(USERS)
                    .fetch();
            records.forEach(System.out::println);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        // DSLContext

    }
}
