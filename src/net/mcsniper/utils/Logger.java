package net.mcsniper.utils;

public class Logger {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void printLogo(){
        String logo = "\n" +
                " __       __   ______    ______             __                               \n" +
                "/  \\     /  | /      \\  /      \\           /  |                              \n" +
                "$$  \\   /$$ |/$$$$$$  |/$$$$$$  | _______  $$/   ______    ______    ______  \n" +
                "$$$  \\ /$$$ |$$ |  $$/ $$ \\__$$/ /       \\ /  | /      \\  /      \\  /      \\ \n" +
                "$$$$  /$$$$ |$$ |      $$      \\ $$$$$$$  |$$ |/$$$$$$  |/$$$$$$  |/$$$$$$  |\n" +
                "$$ $$ $$/$$ |$$ |   __  $$$$$$  |$$ |  $$ |$$ |$$ |  $$ |$$    $$ |$$ |  $$/ \n" +
                "$$ |$$$/ $$ |$$ \\__/  |/  \\__$$ |$$ |  $$ |$$ |$$ |__$$ |$$$$$$$$/ $$ |      \n" +
                "$$ | $/  $$ |$$    $$/ $$    $$/ $$ |  $$ |$$ |$$    $$/ $$       |$$ |      \n" +
                "$$/      $$/  $$$$$$/   $$$$$$/  $$/   $$/ $$/ $$$$$$$/   $$$$$$$/ $$/       \n" +
                "                                               $$ |                          \n" +
                "                                               $$ |                          \n" +
                "                                               $$/                           \n";
        System.out.println(ANSI_RED + logo + ANSI_RESET);
    }
    public static void log(LogType type, String msg){
        if(type ==  LogType.GENERAL){
            System.out.println("[" + ANSI_CYAN+ "General" + ANSI_RESET + "] " + msg);
        }
        if(type == LogType.AUTH){
            System.out.println("[" + ANSI_BLUE+ "Auth" + ANSI_RESET + "] " + msg);
        }
        if(type ==  LogType.ERROR){
            System.out.println("[" + ANSI_RED+ "ERROR" + ANSI_RESET + "] " + msg);
        }
        if(type ==  LogType.WARNING){
            System.out.println("[" + ANSI_YELLOW+ "Warning" + ANSI_RESET + "] " + msg);
        }
        if(type ==  LogType.SUCCESS){
            System.out.println("[" + ANSI_GREEN+ "Success" + ANSI_RESET + "] " + msg);
        }
        if(type ==  LogType.CONFIG){
            System.out.println("[" + ANSI_PURPLE+ "CONFIG" + ANSI_RESET + "] " + msg);
        }


    }

    public static void log(String msg){
        System.out.println(msg);
    }
}
