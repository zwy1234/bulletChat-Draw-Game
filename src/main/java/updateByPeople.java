import java.util.Scanner;

/**
 * debug by people
 */
public class updateByPeople implements Runnable {
    Scanner scan = new Scanner(System.in);

    @Override
    public void run() {
        while(true){
        String s = scan.nextLine();
        String[] msg0 = parseDanmu(s);
        if (msg0 != null) {
            switch (msg0[0]) {
                case "fill":
                    UI.map.put(msg0[1] + "," + msg0[2], msg0[3]);

                    UI.TestPane.timer.start();
                    break;

                case "clear":
                    UI.map.remove(msg0[1] + "," + msg0[2]);

                    UI.TestPane.timer.start();
                    break;
            }
            }
        }

    }

    public static  String[] parseDanmu(String msg){
        if(msg.startsWith("fill")||msg.startsWith("clear")){
            String[] s = msg.split(" ");
            return s;
        }
        return null;
    }
}
