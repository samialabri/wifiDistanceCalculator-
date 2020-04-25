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
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
///Users/samiabri/Desktop/b-01.csv
        while(true) {
            System.out.println("enter mac address: ");
            String mac = sc.nextLine();
            for (int i = 0; i < 12; i++) {
                getSignalLevelValues("/home/pi/b-01.csv",mac);
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
            double k = calculateConistant(1,avg,2412.0);
            System.out.println(k);

            signalLevelValues.clear();

            System.out.println("now put the phone away and lets calulcate the distance, press enter once you are reade ");
            sc.nextLine();
            System.out.println("enter mac address: ");
            mac = sc.nextLine();
            for (int i = 0; i < 12; i++) {
                getSignalLevelValues("/home/pi/b-01.csv",mac);
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    System.out.println("something went wrong while trying to sleep");
                }
            }

            avg = calculateAverage();
            System.out.println(avg);
            double length = calculateLength(2412,avg,k);
            System.out.println(length);
        }
    }

    private static double calculateLength(double signal_frequency,double signal_level,double consitant) {
        double x = (consitant - (20*Math.log10(signal_frequency)) + Math.abs(signal_level))/20.0;
        double distance = Math.pow(10,x);
        return distance;
    }

    private static double calculateConistant(double distance,double signal_level,double frequency){
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
