package net.mcsniper.managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ProxyManager {

    public static ArrayList<String> getProxies() throws FileNotFoundException {
        ArrayList<String> proxies = new ArrayList<String>();
        Scanner in = new Scanner(new File("./proxies.txt"));
        while(in.hasNext()){
            proxies.add(in.next());
        }

        return proxies;
    }
}
