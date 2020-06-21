package uk.co.shikanga.jvm.data.jooq;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record2;
import org.jooq.Result;
import org.jooq.impl.DSL;
import uk.co.shikanga.jvm.data.jooq.public_.tables.Users;

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

            dslContext.insertInto(USERS, USERS.EMAIL).values("my@friend.com").execute();

            dslContext.update(USERS)
                    .set(USERS.EMAIL, "something@else.com")
                    .where(USERS.EMAIL.startsWith("my@"))
                    .execute();

            dslContext.deleteFrom(USERS)
                    .where(USERS.EMAIL.eq("something@else.com"))
                    .execute();

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
