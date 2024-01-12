import java.util.*;

public class PriorityScheduling
{
    // Handled the starvation problem by using aging in which the process's priority will decrease by 1 every second
    // in the ready queue and then it will start to be executed if its priority reached to be smallest one
    PriorityScheduling(Vector<Process> processes)
    {
        Map<String,Integer> index = new HashMap<>();
        for(int i = 0; i < processes.size(); i++)
        {
            index.put(processes.elementAt(i).name,i);
        }
        int processSize = processes.size();
        Vector<Process> processesOrder = new Vector<>();
        Vector<Integer> start = new Vector<>();
        processes.sort(Comparator.comparing(process -> process.arrivalTime));
        int startTime = 0;
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparing((Process process) -> process.priority).thenComparing(process-> process.burstTime));
        Process []finalResult = new Process[processSize];
        while(true)
        {
            while(!processes.isEmpty() && processes.firstElement().arrivalTime <= startTime)
            {
                readyQueue.add(processes.firstElement());
                processes.remove(0);
            }
            if(!readyQueue.isEmpty())
            {
                Process p = readyQueue.poll();
                p.startTime = startTime;
                processesOrder.add(p);
                start.add(startTime);
                startTime += p.burstTime - 1;
                if(p.burstTime == 0)
                {
                    startTime++;
                }
                p.completionTime = startTime + 1;
                finalResult[index.get(p.name)] = p;
                updateAging(readyQueue);
            }
            if(processes.isEmpty() && readyQueue.isEmpty())
            {
                break;
            }
            startTime++;
        }
        start.add(startTime+1);
        printExecutionOrderOfProcesses(processesOrder,start);
        printProcessesDetails(finalResult, processSize);
    }
    void updateAging(PriorityQueue<Process> pq)
    {
        Vector<Process> processes = new Vector<>();
        while(!pq.isEmpty())
        {
            processes.add(pq.poll());
        }
        for(Process p : processes)
        {
            if(p.priority != 0)
            {
                p.priority--;
            }
            pq.add(p);
        }
    }
    void printExecutionOrderOfProcesses(Vector<Process> processesOrder, Vector<Integer> start)
    {
        System.out.println("Execution order of processes:");
        int cnt = 1;
        for(int i = 0; i < processesOrder.size(); i++)
        {
            int s = start.elementAt(i);
            int e = start.elementAt(i+1);
            System.out.println(cnt + "- " + processesOrder.elementAt(i).name + "  Start: " + s + "  End: " + e);
            cnt++;
        }
        System.out.println();
    }
    void printProcessesDetails(Process[] processes, int processesSize)
    {
        System.out.println("Waiting time and Turnaround time for each process:");
        int cnt = 1;
        double totalTurnAroundTime = 0, totalWaitingTime = 0;
        for(int i = 0; i < processesSize; i++)
        {
            Process p = processes[i];
            p.TurnaroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.completionTime - p.burstTime - p.arrivalTime;
            totalTurnAroundTime += p.TurnaroundTime;
            totalWaitingTime += p.waitingTime;
            System.out.println(cnt + "- " + p.name + ": Waiting time = " + p.waitingTime + "   Turnaround time = " + p.TurnaroundTime);
            cnt++;
        }
        System.out.println();
        System.out.println("Average waiting time: " + totalWaitingTime/processesSize);
        System.out.println("Average Turnaround time: " + totalTurnAroundTime/processesSize);
    }
}
