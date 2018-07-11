package net.winterly.dropwizard.hk2bundle.jdbi;

import org.skife.jdbi.v2.sqlobject.SqlQuery;

public interface ExampleDAO {

    @SqlQuery("SELECT :i")
    int selectNumber(int i);

}
