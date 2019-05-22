package com.ken.app;

import java.io.*;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

public class App {

    private static TreeMap<Integer, Computer> data;
    private int nextId;

    public App() {
        //Load Treemap???
        readDatabase();
        this.nextId = createId();
    }

    public TreeMap<Integer, Computer> getData() {
        return data;
    }

    public void deleteKey(String value) {
        for(Map.Entry<Integer, Computer> entry : data.entrySet()) {
            if(entry.getValue().getMac().equals(value.toUpperCase())) {
                data.remove(entry.getKey());
                //System.out.println("Key: " + entry.getKey());
                //System.out.println("MAC: " + entry.getValue().getMac());
                //System.out.println("Serial: " + entry.getValue().getSerial());
                //System.out.println(data.get(6));
                break;
            } else if (entry.getValue().getSerial().equals(value)) {
                data.remove(entry.getKey());
                /*System.out.println("Key: " + entry.getKey());
                System.out.println("MAC: " + entry.getValue().getMac());
                System.out.println("Serial: " + entry.getValue().getSerial());
                System.out.println(data.get(6).getSerial());
                System.out.println(entry.getKey());*/
                break;
            }
        }
    }

    public boolean findKey(String serial, String mac) {
        for(Map.Entry<Integer, Computer> entry : data.entrySet()) {
            if(entry.getValue().getMac().equals(mac.toUpperCase()) || entry.getValue().getSerial().equals(serial)) {
                return true;
            }
        }
        return false;
    }

    public Computer findInfo(String key) {
        for(Map.Entry<Integer, Computer> entry : data.entrySet()) {
            if(entry.getValue().getMac().equals(key.toUpperCase()) || entry.getValue().getSerial().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public boolean readDatabase() {
        try {
            FileInputStream f = new FileInputStream(new File("database.ser"));
            ObjectInputStream o = new ObjectInputStream(f);

            data = (TreeMap) o.readObject();
            if(data.isEmpty()) {
                System.out.println("Data is empty");
            }

        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound read");
            data = new TreeMap<Integer, Computer>();
            return true;
        } catch (IOException e) {
            System.out.println("IO read");
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("Classnotfound read");
            return false;
        }
        return true;
    }

    public boolean saveDatabase() {
        try {
            FileOutputStream f = new FileOutputStream(new File("database.ser"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            o.writeObject(data);

            o.close();
            f.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("FiileNotFoundException save");
            return false;
        } catch (IOException e){
            System.out.println("IOException save");
            return false;
        }
        return true;
    }

    public void add(String serial, String mac) {
        Computer tmp = new Computer(serial, mac);
        data.put(getNextId(), tmp);
    }

    public void removeById(int key) {
        data.remove(key);
    }

    public void print() {
        System.out.println(data);
    }

    private int getNextId() {
        nextId = nextId + 1;
        return nextId;
    }

    private int createId() {
        int tmp;
        try {
            tmp = data.lastKey();
        } catch (NoSuchElementException e) {
            tmp = 0;
        }
        return tmp;
    }


}
