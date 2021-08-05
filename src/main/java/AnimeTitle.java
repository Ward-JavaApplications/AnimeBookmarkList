public class AnimeTitle {
    private String title;
    private int malID;
    private String status;
    private int priority;
    private boolean released = true;

    public AnimeTitle(String title, String status, int priority) {
        this.title = title;
        this.status = status;
        this.priority = priority;
    }

    public AnimeTitle(String title, String status) {
        this.title = title;
        this.status = status;
    }
    public AnimeTitle(String title, String status, int priority,boolean released) {
        this.title = title;
        this.status = status;
        this.priority = priority;
        this.released = released;
    }

    public AnimeTitle(String title, int malID, String status, boolean released) {
        this.title = title;
        this.malID = malID;
        this.status = status;
        this.released = released;
    }

    public AnimeTitle(String title, int malID, String status, int priority, boolean released) {
        this.title = title;
        this.malID = malID;
        this.status = status;
        this.priority = priority;
        this.released = released;
    }

    public AnimeTitle(String title, String status, boolean released) {
        this.title = title;
        this.status = status;
        this.released = released;
    }

    public AnimeTitle(String title, int malID, String status, int priority) {
        this.title = title;
        this.malID = malID;
        this.status = status;
        this.priority = priority;
    }

    public AnimeTitle(String title, int malID, String status) {
        this.title = title;
        this.malID = malID;
        this.status = status;
    }

    public int getMalID() {
        return malID;
    }

    public boolean isReleased() {
        return released;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


}
