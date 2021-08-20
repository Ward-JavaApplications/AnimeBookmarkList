public class Demo {
    public static void main(String[] args) {
        MyGUIManager parent = new MyGUIManager();
        new Thread(new BackgroundChecker(parent)).start();
        parent.startGUI();
    }
}
