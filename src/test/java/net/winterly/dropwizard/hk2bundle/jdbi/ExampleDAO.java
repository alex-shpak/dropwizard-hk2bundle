package net.winterly.dropwizard.hk2bundle.jdbi;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

public interface ExampleDAO {

    @SqlQuery("SELECT :i")
    int selectNumber(@Bind("i") int i);

}
