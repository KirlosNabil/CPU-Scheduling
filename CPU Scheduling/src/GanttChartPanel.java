import javax.swing.*;
import java.awt.*;
import java.util.Vector;

class GanttChartPanel extends JPanel {

    private Process process;

    public GanttChartPanel(Process process) {
        this.process = process;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int startX = 50;
        int startY = 50;
        int barHeight = 30;
        int contextSwitch = 10;

        int startPixel = startX + process.startTime;
        int endPixel = startPixel + process.burstTime;

        g.setColor(new Color(255,250,100));
        g.fillRect(startPixel, startY, endPixel - startPixel, barHeight);

        g.setColor(Color.BLACK);
        g.drawString(process.name, startPixel + 5, startY + barHeight / 2 + 5);

        // Draw time intervals
        int currentTime = startX;
        while (currentTime < startX + process.completionTime + contextSwitch) {
            g.setColor(Color.GRAY);
            g.drawLine(currentTime, startY, currentTime, startY + barHeight);
            currentTime += contextSwitch;
        }
    }
}