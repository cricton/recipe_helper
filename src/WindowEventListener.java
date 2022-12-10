import TypeDefs.Recipe;

public interface WindowEventListener {

    void addDataEvent(Recipe e);
    void deleteDataEvent(String name);
    void updateDataEvent(String name, String value, String type);


}
