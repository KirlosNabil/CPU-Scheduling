import java.util.*;

public class AG {
    private Vector<Process> processes;
    private Process runningProcess;
    private Queue<Process> readyQueue;
    private Vector<Process> diedProcesses;
    private Map<String, Integer> turnaroundTimeMap;
    private Map<String, Integer> waitingTimeMap;
    private Vector<Map<String, Integer>> quantumHistory;
    private int contextSwitch;
    private int CPUTime = 0;
    //private int anInt = 0;
    public AG(Vector<Process> processVector, int quantumTime, int contextSwitch){
        turnaroundTimeMap = new HashMap<>();
        waitingTimeMap = new HashMap<>();
        processes = processVector;
        this.contextSwitch = contextSwitch;
        processes.sort(Comparator.comparing(process -> process.arrivalTime));
        quantumHistory = new Vector<>();
        quantumHistory.add(new HashMap<>());
        for (int i = 0; i < processes.size(); i++) {
            processes.get(i).setQuantumTime(quantumTime);
            quantumHistory.get(0).put(processes.get(i).getName(), quantumTime);
        }
        System.out.println("Random Function numbers:");
        calcAGFactor();
        System.out.println("----------------------------------------------");
        System.out.println("AG Factor:");
        printAGFactor();
        System.out.println("----------------------------------------------");
        executeFirstProcess();
        readyQueue = new ArrayDeque<>();
        diedProcesses = new Vector<>();
        simulate();
        System.out.println("----------------------------------------------");
        printTATAndWT();
        System.out.println("----------------------------------------------");
        printAverages();
        System.out.println("----------------------------------------------");
        printQuantumHistory();
    }
    public void printAGFactor(){
        for (Process process : processes){
            System.out.println(process.getName()+" = "+process.getAGFactor());
        }
    }
    public void printTATAndWT(){
        System.out.println("Turnaround Time:");
        for (Map.Entry<String, Integer> entry : turnaroundTimeMap.entrySet()) {
            System.out.println(entry.getKey()+" = "+entry.getValue());
        }
        System.out.println("Waiting Time:");
        for (Map.Entry<String, Integer> entry : waitingTimeMap.entrySet()) {
            System.out.println(entry.getKey()+" = "+entry.getValue());
        }
    }
    public int randomFunction(){
        /*int[] randoms = {3, 8, 10, 12};
        return randoms[anInt++];*/
        return new Random().nextInt(0, 20);
    }
    public void calcAGFactor(){
        for (Process process : processes){
            int random = randomFunction();
            if (random < 10){
                int AGFactor = random + process.getArrivalTime() + process.getBurstTime();
                process.setAGFactor(AGFactor);
            }else if (random > 10){
                int AGFactor = 10 + process.getArrivalTime() + process.getBurstTime();
                process.setAGFactor(AGFactor);
            }else {
                int AGFactor = process.getPriority() + process.getArrivalTime() + process.getBurstTime();
                process.setAGFactor(AGFactor);
            }
            System.out.println(process.getName()+" = "+random);
        }
    }
    public void executeFirstProcess(){
        Process firstProcess = null;
        int minArrival = processes.get(0).getArrivalTime();
        int minAGFactor = processes.get(0).getAGFactor();
        for (int i = 1; i < processes.size(); i++) {
            Process tempProcess = processes.get(i);
            if (tempProcess.getArrivalTime() == minArrival){
                if (tempProcess.getAGFactor() < minAGFactor){
                    firstProcess = tempProcess;
                }
            }else {
                break;
            }
        }
        if (firstProcess == null){
            runningProcess = processes.get(0);
        }else {
            runningProcess = firstProcess;
        }
        CPUTime = runningProcess.getArrivalTime();
        int index = processes.indexOf(runningProcess);
        runningProcess.setStartTime(CPUTime);
        processes.get(index).setStartTime(CPUTime);
    }
    public void checkForReady(){
        for (Process process : processes){
            if (process.getArrivalTime() <= CPUTime && !readyQueue.contains(process) && process!=runningProcess && !diedProcesses.contains(process)){
                readyQueue.add(process);
            }
        }
    }

    public boolean isDone(){
        for (Process process: processes){
            if (process.getQuantumTime()!=0){
                return false;
            }
        }
        return true;
    }

    public void printAverages(){
        double sumTAT = 0;
        double sumWT = 0;
        for (Process process: processes){
            sumTAT += process.getTurnaroundTime();
            sumWT += process.getWaitingTime();
        }
        System.out.println("Average Turnaround Time = "+(sumTAT/processes.size()));
        System.out.println("Average Waiting Time = "+(sumWT/processes.size()));
    }

    public void simulate(){
        while (!isDone()){
            runningProcess.setStartTime(CPUTime);
            int nonPreemptiveTime = (int) Math.ceil(runningProcess.getQuantumTime()*0.5);
            if (nonPreemptiveTime < runningProcess.getRemainingBurstTime()){
                CPUTime += nonPreemptiveTime;
                runningProcess.setRemainingBurstTime(runningProcess.getRemainingBurstTime()-nonPreemptiveTime);
            }else {
                CPUTime += runningProcess.getRemainingBurstTime();
                runningProcess.setRemainingBurstTime(0);
            }
            int preemptiveTime = 0;
            while (true){
                checkForReady();
                // case iii
                if (runningProcess.getRemainingBurstTime()==0){
                    System.out.println("Process "+runningProcess.getName()+" ran from "+runningProcess.getStartTime()+" to "+CPUTime);
                    int TAT = CPUTime-runningProcess.getArrivalTime();
                    int waitingTime = TAT-runningProcess.getBurstTime();
                    int index = processes.indexOf(runningProcess);
                    processes.get(index).setTurnaroundTime(TAT);
                    processes.get(index).setWaitingTime(waitingTime);
                    turnaroundTimeMap.put(runningProcess.getName(), TAT);
                    waitingTimeMap.put(runningProcess.getName(), waitingTime);
                    processes.get(index).setQuantumTime(0);
                    quantumHistory.add(deepCopy(quantumHistory.lastElement()));
                    quantumHistory.lastElement().put(runningProcess.getName(), 0);
                    diedProcesses.add(runningProcess);
                    runningProcess = readyQueue.poll();
                    if (runningProcess != null) {
                        runningProcess.setStartTime(CPUTime);
                        break;
                    }else if (diedProcesses.size() == processes.size()){
                        break;
                    }else {
                        for (Process process: processes){
                            if (!diedProcesses.contains(process)){
                                runningProcess = process;
                                CPUTime = runningProcess.getArrivalTime();
                                break;
                            }
                        }
                    }
                }
                // case i
                if (preemptiveTime+nonPreemptiveTime == runningProcess.getQuantumTime()){
                    double sum=0;
                    for (Process process:processes){
                        sum += process.getQuantumTime();
                    }
                    int mean = (int) Math.ceil(0.1*(sum/processes.size()));
                    int newQuantum = runningProcess.getQuantumTime()+mean;
                    int index = processes.indexOf(runningProcess);
                    runningProcess.setQuantumTime(newQuantum);
                    processes.get(index).setQuantumTime(newQuantum);
                    quantumHistory.add(deepCopy(quantumHistory.lastElement()));
                    quantumHistory.lastElement().put(runningProcess.getName(), newQuantum);
                    System.out.println("Process "+runningProcess.getName()+" ran from "+runningProcess.getStartTime()+" to "+CPUTime);
                    readyQueue.add(runningProcess);
                    runningProcess = readyQueue.poll();
                    break;
                }
                // case ii
                if (!readyQueue.isEmpty()){
                    if (contextSwitch(preemptiveTime+nonPreemptiveTime)){
                        break;
                    }
                }
                preemptiveTime++;
                CPUTime++;
                runningProcess.setRemainingBurstTime(runningProcess.getRemainingBurstTime()-1);
            }
        }
    }
    public boolean contextSwitch(int usedQuantum){
        Process newProcess = runningProcess;
        for (Process process: readyQueue) {
            if (process.getAGFactor()<newProcess.getAGFactor()){
                newProcess = process;
            }
        }
        if (newProcess != runningProcess){
            System.out.println("Process "+runningProcess.getName()+" ran from "+runningProcess.getStartTime()+" to "+CPUTime);
            int unusedQuantum = runningProcess.getQuantumTime() - usedQuantum;
            int newQuantum = runningProcess.getQuantumTime() + unusedQuantum;
            runningProcess.setQuantumTime(newQuantum);
            int index = processes.indexOf(runningProcess);
            processes.get(index).setQuantumTime(newQuantum);
            quantumHistory.add(deepCopy(quantumHistory.lastElement()));
            quantumHistory.lastElement().put(runningProcess.getName(), newQuantum);
            readyQueue.add(runningProcess);
            readyQueue.remove(newProcess);
            runningProcess = newProcess;
            CPUTime += contextSwitch;
            return true;
        }
        return false;
    }
    public void printQuantumHistory(){
        for (int i = 0; i < quantumHistory.size(); i++) {
            System.out.println("Quantum "+i);
            for (Map.Entry<String, Integer> entry : quantumHistory.get(i).entrySet()) {
                System.out.println(entry.getKey()+" = "+entry.getValue());
            }
            System.out.println("----------------------");
        }
    }
    public Map<String, Integer> deepCopy(Map<String, Integer> original) {
        Map<String, Integer> copy = new HashMap<>();
        for (Map.Entry<String, Integer> entry : original.entrySet()) {
            int value = entry.getValue();
            String key = entry.getKey();
            copy.put(key, value);
        }
        return copy;
    }
}