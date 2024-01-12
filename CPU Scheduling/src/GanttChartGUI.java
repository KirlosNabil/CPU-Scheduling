import javax.swing.*;
import java.awt.*;
import java.util.Vector;

class GanttChartGUI extends JFrame {

    private Vector<Process> processes;

    public GanttChartGUI(Vector<Process> processes) {
        this.processes = processes;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Process Execution Gantt Chart");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLayout(new FlowLayout());
        GridBagConstraints b = new GridBagConstraints();
        int row = 0, column = 0;
        b.gridheight = 50;
        b.gridwidth = 50;
        Panel pp = new Panel(new GridBagLayout());
        for(Process p : processes)
        {
            GanttChartPanel ganttChartPanel = new GanttChartPanel(p);
            b.gridx = column;
            b.gridy = row;
            column += 1;
            row += 1;
            pp.add(ganttChartPanel,b);
            pp.setVisible(true);
        }
        add(pp);
        setVisible(true);
    }
}