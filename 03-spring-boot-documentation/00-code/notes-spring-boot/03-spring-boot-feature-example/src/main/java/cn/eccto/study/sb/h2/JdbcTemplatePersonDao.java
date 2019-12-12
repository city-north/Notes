package cn.eccto.study.sb.h2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * description
 *
 * @author EricChen 2019/12/12 11:26
 */
@Repository
public class JdbcTemplatePersonDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(Person person) {
        String sql = "insert into Person (first_Name, Last_Name, Address) values (?, ?, ?)";
        jdbcTemplate.update(sql, person.getFirstName(), person.getLastName(),
                person.getAddress());
    }

    public List<Person> loadAll() {
        return jdbcTemplate.query("select * from Person", (resultSet, i) -> {
            return toPerson(resultSet);
        });
    }

    private Person toPerson(ResultSet resultSet) throws SQLException {
        Person person = new Person();
        person.setId(resultSet.getLong("ID"));
        person.setFirstName(resultSet.getString("FIRST_NAME"));
        person.setLastName(resultSet.getString("LAST_NAME"));
        person.setAddress(resultSet.getString("ADDRESS"));
        return person;
    }
}
