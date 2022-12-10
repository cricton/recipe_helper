import java.sql.ResultSet;

public interface DBEventListener {
    void dataFetchedEvent(ResultSet e);
}
