public class AnimeTitle {
    private String title;
    private String status;
    private int priority;

    public AnimeTitle(String title, String status, int priority) {
        this.title = title;
        this.status = status;
        this.priority = priority;
    }

    public AnimeTitle(String title, String status) {
        this.title = title;
        this.status = status;
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
