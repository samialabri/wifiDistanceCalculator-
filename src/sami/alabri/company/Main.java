package sami.alabri.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static List<Double> signalLevelValues = new ArrayList<Double>();
    private static Scanner sc = new Scanner(System.in);
    private static String macAddress;
    private static String airodumpFileName;
    private static String serverIpAdress;
    private static double constant;
    private static boolean isCalibrated;
    private static client clnt;
    private static int identity;
    private static boolean repeat = false;


    public static void main(String[] args) {
        isCalibrated = false;



        System.out.println("This Program will try to calculate the Distance from a WiFi station using the Signal Strength,");
        System.out.println("and the frequency of the WiFi, it depends on the aircrack-ng software suite, if you haven't   ,");
        System.out.println("installed it yet please quit this program and install it 'sudo apt install aircrack-ng'        ");

        System.out.println("enter the ip address of the server: ");
        serverIpAdress = sc.nextLine();
        System.out.println("enter the identity of the station: ");
        identity = Integer.parseInt(sc.nextLine());
        clnt = new client(serverIpAdress,5000,identity);

        System.out.println("enter mac address of the target device: ");
        macAddress = sc.nextLine();

        System.out.println("whats the file name airodump-ng is writing in? (enter the complete path) ");
        airodumpFileName = sc.nextLine();

        while(true) {

            if(!isCalibrated) {
                System.out.println("place the device 1m from the station, press enter once you've done");
                sc.nextLine();


                System.out.println("now the wifi equation to calculate the distance will be calibrated ");

                calibration();

                System.out.println("now put the device away and lets calulcate the distance, press enter once you are ready ");
                isCalibrated = true;
                sc.nextLine();
            }
            findDistance();
            if(!repeat){
            System.out.println("Do you want to repeat? if yes press  y otherwise CTRL + C");
            String isrepeat = sc.nextLine();
            if(isrepeat.equals("y") || isrepeat.equals("Y")){
                repeat = true;
            }
            }
            System.out.println("Do you want to repeat calibration? y for yes, anything else for no");
            String calibrationYesOrNo = sc.nextLine();
            if(calibrationYesOrNo.equals("Y") || calibrationYesOrNo.equals("y")){
                isCalibrated = false;
            }
        }
    }

    private static void findDistance(){
        for (int i = 0; i < 12; i++) {
            getSignalLevelValues("/home/pi/b-01.csv",macAddress);
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                System.out.println("something went wrong while trying to sleep");
            }
        }

        double avg = calculateAverage();
        System.out.println(avg);
        double length = calculateLength(2412,avg, constant);
        System.out.println(length);
        clnt.sendData(Double.toString(length));
    }

    private static void calibration(){


        for (int i = 0; i < 12; i++) {
            getSignalLevelValues(airodumpFileName,macAddress);
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                System.out.println("something went wrong while trying to sleep");
            }
        }

        for (double x:
                signalLevelValues) {
            System.out.println(x);
        }

        double avg = calculateAverage();
        System.out.println(avg);
        constant = calculateConstant(1,avg,2412.0);
        System.out.println(constant);

        signalLevelValues.clear();
    }

    private static double calculateLength(double signal_frequency,double signal_level,double consitant) {
        double x = (consitant - (20*Math.log10(signal_frequency)) + Math.abs(signal_level))/20.0;
        double distance = Math.pow(10,x);
        return distance;
    }

    private static double calculateConstant(double distance, double signal_level, double frequency){
        double consitant = ((20*(Math.log10(distance)/Math.log10(10))) + 20*Math.log10(frequency) - Math.abs(signal_level));
        return consitant;
    }

    private static void getSignalLevelValues(String fileName,String macAddress){
        Path pathToFile = Paths.get(fileName);
        try (BufferedReader br = Files.newBufferedReader(pathToFile,StandardCharsets.US_ASCII)) {
            String line = br.readLine();
            while ((line != null)) {
                String[] attributes = line.split(",",-1);
                if(attributes.length>2){
                    System.out.println(attributes[0] + "  " +  attributes[3]);
                    if(attributes[0].equals(macAddress)) {
                        System.out.println(attributes[3]);
                        signalLevelValues.add(Math.abs(Double.parseDouble(attributes[3])));

                    }
                }

                line = br.readLine();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }

    }

    private static double calculateAverage(){
        double sum = 0;
        System.out.println(signalLevelValues.size());
        for (double level:signalLevelValues) {
            sum = sum + level;
        }
        return sum/signalLevelValues.size();
    }
}
