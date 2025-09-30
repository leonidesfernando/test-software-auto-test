package br.com.home.lab.softwaretesting.automation.jdbi.test;

import br.com.home.lab.softwaretesting.automation.jdbi.dao.EntryDao;
import br.com.home.lab.softwaretesting.automation.jdbi.dao.factory.DaoFactory;
import br.com.home.lab.softwaretesting.automation.jdbi.model.Entry;
import br.com.home.lab.softwaretesting.automation.model.Category;
import br.com.home.lab.softwaretesting.automation.model.EntryType;
import br.com.home.lab.softwaretesting.automation.util.DataGen;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class JdbiTest {

    private EntryDao dao;
    private static Entry entryContext = null;

    @BeforeClass
    public void setup(){
        dao = DaoFactory.get(EntryDao.class);
        dao.clean();
    }

    @Test
    public void testAddEntry(){
        Entry entry = getNewEntry();
        dao.add(entry);
        entryContext = entry;
        log.info("New entry added with id: {}", entryContext.description());
    }

    @Test(dependsOnMethods = {"testAddEntry"})
    public void testFindAll(){
        List<Entry> list = dao.getAll();
        assertThat(list).hasSize(1);
        list.forEach(e -> log.info(e.toString()));
    }

    @Test(dependsOnMethods = {"testAddEntry"})
    public void testGetByName(){
        Entry entry = dao.getByName(entryContext.description());
        entryContext = entry;
        assertThat(entry).isNotNull();
        assertThat(entry.description()).isEqualTo(entryContext.description());
        log.info("Entry found: {}", entry.toString());
    }

    @Test(dependsOnMethods = {"testFindAll", "testGetByName"})
    public void testDelete(){
        int rows = dao.delete(entryContext.id());
        log.info("Entry to be deleted: {}", entryContext.id());
        assertThat(rows).isEqualTo(1);
        log.info("Entry deleted, rows affected: {}", rows);
    }


    private static final EntryType[] entryTypes = EntryType.values();

    private static final Category[] categories = Category.values();

    public static Entry getNewEntry(){
        String description = new StringJoiner(" ")
                .add("Jdbi Test")
                .add(DataGen.productName())
                .add(LocalDateTime.now().toString()).toString();

        return new Entry(0, description, DataGen.localDateCurrentMonth(),
                entryTypes[DataGen.number(0, entryTypes.length - 1)],
                categories[DataGen.number(0, categories.length - 1)], DataGen.amount());
    }

    /*
CREATE TABLE public.entry
(
    id bigint GENERATED ALWAYS AS IDENTITY,
    category character varying(255) COLLATE pg_catalog."default" NOT NULL,
    entry_date timestamp without time zone NOT NULL,
    description character varying(255) COLLATE pg_catalog."default",
    entry_type character varying(255) COLLATE pg_catalog."default" NOT NULL,
    amount numeric(38,2) NOT NULL,
    CONSTRAINT entry_pkey PRIMARY KEY (id),
    CONSTRAINT enrty_amount_check CHECK (amount >= 0::numeric)
)

     */
}
