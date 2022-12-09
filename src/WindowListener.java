import java.awt.event.ActionEvent;

public interface WindowListener {

    void addDataEvent(Recipe e);
    void deleteDataEvent(String name);
    void updateDataEvent(String name, String value, String type);
    void fetchDataEvent();

}
