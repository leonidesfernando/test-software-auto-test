package br.com.home.lab.softwaretesting.automation.jdbi.dao;

import br.com.home.lab.softwaretesting.automation.jdbi.model.Entry;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface EntryDao {

    @SqlUpdate("INSERT into entry(category, entry_date, description, entry_type, amount) " +
            "values (:category, :entryDate, :description, :type, :amount)")
    int add(@BindMethods Entry entry);


    @SqlQuery("select * from entry")
    @RegisterConstructorMapper(Entry.class)
    List<Entry> getAll();

    @SqlQuery("select * from entry where description = :name")
    @RegisterConstructorMapper(Entry.class)
    Entry getByName(String name);


    @SqlUpdate("delete from entry where id = :id")
    int delete(long id);

    @SqlUpdate("truncate table entry")
    void clean();
}
