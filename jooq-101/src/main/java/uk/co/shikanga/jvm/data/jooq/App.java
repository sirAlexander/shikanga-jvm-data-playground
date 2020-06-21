package uk.co.shikanga.jvm.data.jooq;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;
import uk.co.shikanga.jvm.data.jooq.public_.tables.daos.UsersDao;
import uk.co.shikanga.jvm.data.jooq.public_.tables.pojos.Users;
import uk.co.shikanga.jvm.data.jooq.public_.tables.records.UsersRecord;

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

            DSL.using(connection).transaction(configuration -> {

                DSLContext dslContext = DSL.using(configuration);

                UsersDao usersDao = new UsersDao(configuration);

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

            });

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        // DSLContext

    }

    private static void logRecordDetails(UsersRecord usersRecord) {
        System.out.println(usersRecord.getEmail() + " " + usersRecord.getId());
    }
}
