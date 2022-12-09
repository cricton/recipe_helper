public class Connector {

    private DB_helper helper;
    private Window window;

    public Connector(){

    }

    public void addComponent(DB_helper helper){
        this.helper = helper;
    }

    public void addComponent(Window window){
        this.window = window;


    }

    public void setupEventListener(){
        window.addActionListener(helper);
    }

}
