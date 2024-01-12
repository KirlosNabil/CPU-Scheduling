import javax.swing.*;
import java.util.*;

public class SJF
{
    private double AverageWaitingTime = 0.0;
    private double AverageTurnAroundTime = 0.0;
    public static int currentTime = 0;


    public SJF(Vector<Process> processes,int contextSwitch)
    {
        Map<String, Integer> index = new HashMap<>();
        for(int i = 0; i < processes.size(); i++)
        {
            index.put(processes.elementAt(i).name,i);
        }
        Process []finalResult = new Process[processes.size()];
        processes.sort(Comparator.comparing(process -> process.arrivalTime));
        currentTime = processes.elementAt(0).getArrivalTime();

        this.schedule(processes,contextSwitch);
        System.out.println();
        System.out.println("Execution order of processes:");
        for(int i = 0; i < processes.size(); i++)
        {
            System.out.println((i + 1) + " - " + processes.elementAt(i).name + "  Start: " + processes.elementAt(i).startTime + "  End: " + processes.elementAt(i).completionTime);
        }
        System.out.println();
        System.out.println("Waiting time and Turnaround time for each process:");
        for(int i = 0; i < processes.size(); i++)
        {
            finalResult[index.get(processes.elementAt(i).name)] = processes.elementAt(i);
        }
        for(int i = 0; i < processes.size(); i++)
        {
            System.out.println(i+1 + "- " + finalResult[i].name + "   Turnaround time: " + finalResult[i].TurnaroundTime + "    Waiting time: " + finalResult[i].waitingTime);
        }
        System.out.println();
        System.out.println("AverageTurnAroundTime: " + ((AverageTurnAroundTime / processes.size())));
        System.out.println("AverageWaitingTime: " + ((AverageWaitingTime / processes.size())));
        System.out.println();
        SwingUtilities.invokeLater(() -> {

            new GanttChartGUI(processes);
        });
    }

    public void schedule(Vector<Process> processes, int contextSwitch)
    {
        int start = 0;
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparing(process -> process.burstTime));
        Vector<Process> ordered = new Vector<>();
        while(true)
        {
            while(!processes.isEmpty() && processes.firstElement().arrivalTime <= start)
            {
                readyQueue.add(processes.firstElement());
                processes.remove(0);
            }
            if(!readyQueue.isEmpty())
            {
                Process p = readyQueue.poll();
                p.startTime = start;
                start += p.burstTime;
                p.completionTime = start;
                p.TurnaroundTime = p.completionTime - p.arrivalTime;
                p.waitingTime = p.TurnaroundTime - p.burstTime;
                AverageTurnAroundTime += p.TurnaroundTime;
                AverageWaitingTime += p.waitingTime;
                start += contextSwitch;
                ordered.add(p);
            }
            else
            {
                start++;
            }
            if(readyQueue.isEmpty() && processes.isEmpty())
            {
                break;
            }
        }
        for(Process p : ordered)
        {
            processes.add(p);
        }
    }
}