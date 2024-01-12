import java.util.Scanner;
import java.util.Vector;

public class CPUScheduler
{
    private Vector<Process> processes = new Vector<>();
    public void run()
    {
        processes = new Vector<>();
        Scanner in = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int numberOfProcesses = in.nextInt();
        System.out.print("Enter Round Robin Time Quantum: ");
        int roundRobinTimeQuantum = in.nextInt();
        System.out.print("Enter Context Switching time: ");
        int contextSwitch = in.nextInt();
        for(int i = 0; i < numberOfProcesses; i++)
        {
            System.out.print("Enter process name: ");
            String name = in.next();
            System.out.print("Enter process color: ");
            String color = in.next(); // Lesa msh 3aref el color 3obara 3n ehh
            System.out.print("Enter process arrival time: ");
            int arrivalTime = in.nextInt();
            System.out.print("Enter process burst time: ");
            int burstTime = in.nextInt();
            System.out.print("Enter process priority number: ");
            int priorityNumber = in.nextInt();
            processes.add(new Process(name,color,arrivalTime,burstTime,priorityNumber));
            System.out.println();
        }
        while(true)
        {
            System.out.println("Choose sorting algorithm: ");
            System.out.println("1- SJF");
            System.out.println("2- SRTF");
            System.out.println("3- Priority");
            System.out.println("4- AG");
            System.out.println("5- Exit");
            System.out.print("Enter your choice: ");
            int sortingAlgorithm = new Scanner(System.in).nextInt();
            if(sortingAlgorithm == 5)
            {
                break;
            }
            else if (sortingAlgorithm > 5 || sortingAlgorithm < 1)
            {
                System.out.println("Invalid choice");
            }
            sortProcesses(sortingAlgorithm, roundRobinTimeQuantum, contextSwitch);
        }
    }
    void sortProcesses(int sortingAlgorithm, int quantumTime, int contextSwitch)
    {
        Vector<Process> copyOfProcesses = deepCopy(processes);
        if(sortingAlgorithm == 1)
        {
            Scanner in = new Scanner(System.in);
            SJF sjf = new SJF(copyOfProcesses,contextSwitch);
        }
        else if(sortingAlgorithm == 2)
        {
            SRTF srtf = new SRTF(copyOfProcesses);
        }
        else if(sortingAlgorithm == 3)
        {
            PriorityScheduling priorityScheduling = new PriorityScheduling(copyOfProcesses);
        }
        else if(sortingAlgorithm == 4)
        {
            AG ag = new AG(copyOfProcesses, quantumTime, contextSwitch);
        }
    }
    Vector<Process> deepCopy(Vector<Process> processes)
    {
        Vector<Process> copy = new Vector<>();
        for(int i = 0; i < processes.size(); i++)
        {
            copy.add(new Process(processes.elementAt(i).getName(), processes.elementAt(i).getColor(), processes.elementAt(i).getArrivalTime(), processes.elementAt(i).getBurstTime(), processes.elementAt(i).getPriority()));
        }
        return copy;
    }
}
