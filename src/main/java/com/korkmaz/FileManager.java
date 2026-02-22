package com.korkmaz;

import java.io.*;
import java.util.*;

public class FileManager{
    private static FileManager instance;
    private String filename;
    private List<String> cmdList;

    private FileManager(String filename){
        this.filename = filename;
    }
    public static FileManager getInstance(String filename){
        if(instance == null){
            instance = new FileManager(filename);
        }
        return instance;
    }
    public void ReadFile(){
        cmdList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while((line= br.readLine()) != null){
                String [] tokens = line.split(";");
                for(String token : tokens){
                    token = token.trim();
                    if(token.isEmpty()){continue;}
                    cmdList.add(token);

                }
            }
        }catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
    public List<String> getCmdList(){
        return cmdList;
    }
    public void WriteFile(String output){
        try(FileWriter fw =  new FileWriter("output.txt")){
            fw.write(output);
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

}
