import javax.swing.*;

public class StatsFrame {
    public StatsFrame(){
        JFrame frame = loadFrame();
        JPanel mainPanel = new JPanel(new SpringLayout());
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        mainPanel.add(getStatusStats());
        mainPanel.add(getPriorityStats());

        frame.setContentPane(mainPanel);
        SwingUtilities.updateComponentTreeUI(mainPanel);
    }

    private JFrame loadFrame(){
        JFrame frame = new JFrame("Stats");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(500,500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }
    private JPanel getStatusStats(){
        JPanel statusStatsPanel = new JPanel(new SpringLayout());
        statusStatsPanel.setLayout(new BoxLayout(statusStatsPanel,BoxLayout.Y_AXIS));
        StatsContainerStatus stats = new DataBaseManager().getStatusStats();
        JLabel titleLabel = new JLabel("Amount of anime with status");
        titleLabel.setFont((titleLabel.getFont().deriveFont(15.0f)));
        JLabel unwatchedLabel = new JLabel("Unwatched: " + stats.getUnwatched());
        JLabel watchedLabel = new JLabel("Watched: " + stats.getWatched());
        JLabel watchingLabel = new JLabel("Watching: " + stats.getWatching());
        statusStatsPanel.add(titleLabel);
        statusStatsPanel.add(unwatchedLabel);
        statusStatsPanel.add(watchedLabel);
        statusStatsPanel.add(watchingLabel);

        return statusStatsPanel;
    }

    private JPanel getPriorityStats() {
        JPanel priorityPanel = new JPanel(new SpringLayout());
        priorityPanel.setLayout(new BoxLayout(priorityPanel,BoxLayout.Y_AXIS));
        StatsContainerPriority stats = new DataBaseManager().getPriorityStats();
        JLabel titleLabel = new JLabel("Amount of anime with priority");
        titleLabel.setFont((titleLabel.getFont().deriveFont(15.0f)));
        priorityPanel.add(titleLabel);
        int index = 0;
        for(int i: stats.getAsArray()){
            priorityPanel.add(new JLabel(i + " animes have priority " + index));
            index++;
        }
        return priorityPanel;
    }
}
