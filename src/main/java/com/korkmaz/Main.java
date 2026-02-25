package com.korkmaz;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Scan terminal
        Scanner sc = new Scanner(System.in);
        // take file name
        String input = sc.nextLine();
        // get file manager object
        FileManager fm = new FileManager(input);
        //Read file
        fm.ReadFile();
        // get cmd list extracted from file
        List<String> commandList = fm.getCmdList();
        // --------- Task Manager ---------
        SimulationManager simulationManager = new SimulationManager();
        TaskManager taskManager = new TaskManager(commandList, simulationManager);
        taskManager.sendTasks();
        sc.close();
    }
}