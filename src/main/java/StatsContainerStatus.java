public class StatsContainerStatus {
    private int watched;
    private int watching;
    private int unwatched;

    public StatsContainerStatus(int watched, int watching, int unwatched) {
        this.watched = watched;
        this.watching = watching;
        this.unwatched = unwatched;
    }

    public int getWatched() {
        return watched;
    }

    public int getWatching() {
        return watching;
    }

    public int getUnwatched() {
        return unwatched;
    }
}
