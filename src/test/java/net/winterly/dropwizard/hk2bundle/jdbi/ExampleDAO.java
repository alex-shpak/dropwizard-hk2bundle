package net.winterly.dropwizard.hk2bundle.jdbi;

import org.glassfish.hk2.api.UseProxy;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

@UseProxy
public interface ExampleDAO {

    @SqlQuery("SELECT :i")
    int selectNumber(@Bind("i") int i);

}
