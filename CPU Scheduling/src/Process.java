import java.awt.Color;

public class Process
{
    public int arrivalTime;
    public int burstTime;
    public String name;
    public String color;
    public boolean check;
    public int remainingBurstTime;
    public int priority;
    public int completionTime = -1;
    public int startTime = -1;
    public int waitingTime;
    public int TurnaroundTime;
    public int aging = 15;
    private int quantumTime=0;
    private int AGFactor = 0;

    public Process(String name, String color, int arrival_time, int burst_time, int priority)
    {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrival_time;
        this.burstTime = burst_time;
        remainingBurstTime = burst_time;
        this.priority = priority;
    }


    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    public int getAgingOrBurstTime()
    {
        if(aging <= 0)
        {
            return aging;
        }
        else
        {
            return remainingBurstTime;
        }
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public void setRemainingBurstTime(int remainingBurstTime) {
        this.remainingBurstTime = remainingBurstTime;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        TurnaroundTime = turnaroundTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public boolean isCheck() {
        return check;
    }

    public int getRemainingBurstTime() {
        return remainingBurstTime;
    }

    public int getPriority() {
        return priority;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getTurnaroundTime() {
        return TurnaroundTime;
    }

    @Override
    public String toString() {
        return String.format("Process %-10s Turn Around Time %-5d Wating Time %-5d", name , TurnaroundTime, waitingTime);
        //return "Process{" + "name=" + name + ", startTime=" + startTime + ", completionTime=" + completionTime + ", TAT= " + TurnaroundTime + ", wt= " + waitingTime + '}';
    }

    public int getQuantumTime() {
        return quantumTime;
    }

    public void setQuantumTime(int quantumTime) {
        this.quantumTime = quantumTime;
    }

    public int getAGFactor() {
        return AGFactor;
    }

    public void setAGFactor(int AGFactor) {
        this.AGFactor = AGFactor;
    }
}
