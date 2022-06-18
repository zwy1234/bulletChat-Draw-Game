import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.List;
import java.util.*;

public class UI {
    private static String s="";



    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("By:UnKown");

        System.out.print("请输入B站房间号: ");
        String roomID = in.readLine();

        System.out.println("正在连接至直播间, 请稍等..");
        DankamuService dankamuService=new DankamuService(roomID);

        //danmu thread
        Thread danmuService=new Thread(dankamuService);
        danmuService.start();

        //update by people
//        updateByPeople update=new updateByPeople();
//        Thread t1=new Thread(update);
//        t1.start();

        //UI thread
        new UI();

    }

    //存储上色各自坐标以及颜色
    public static  Map<String,String> map=new HashMap<>();
    public UI() {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                TestPane testPane=new TestPane();
                frame.add(testPane);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
    public  class TestPane extends JPanel implements ActionListener {
        //列数
        private int columnCount = 200;
        //行数
        private int rowCount = 200;

        private List<Rectangle> cells;
        private Point selectedCell;

        static int cellWidth =0;
        static int cellHeight = 0;


        static int xOffset = 0;
        static int yOffset = 0;

        //常见颜色英文
        static String[] colors={"red","origin","yellow","green","blue","black","pink","gray"};

        //当前颜色
        static int color_now=0;

        static String[] split=null;


        static String color="";

        static Color color_0=null;

        static Set<String> sets=null;

        static Graphics2D g2d=null;

        JLabel label = new JLabel();
        ImageIcon img = new ImageIcon("src/main/images/pkq.png");// 创建图片对象

        Font f = new Font("隶书",Font.PLAIN,20);

        static Timer timer;//100毫秒执行一次
        {
            timer = new Timer(100, this);
        }


        public TestPane() {

            label.setIcon(img);

            label.setBounds(1220,100,50,50);
//            add(label);
            this.addMouseListener(new MouseListener() {
                @Override
                public void mousePressed(MouseEvent e) {
                    // do nothing
                    if(e.getButton()==e.BUTTON1){    // 判断获取的按钮是否为鼠标的右击
                          // 获得鼠标点击位置的坐标并发送到标签的文字上

                        map.put((e.getX()-xOffset)/cellWidth+","+(e.getY()-yOffset)/cellHeight,colors[color_now]);
                    }
                    if(e.getButton()==e.BUTTON3){
                        color_now=(color_now+1)%colors.length;
                    }
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    // do nothing
                }
                @Override
                public void mouseClicked(MouseEvent e) {

//                pauseRestartTimer();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // do nothing
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // do nothing
                }
            });
//            this.setBackground();
            cells = new ArrayList<>(columnCount * rowCount);
            timer.start();
        }

        @Override
        public Dimension getPreferredSize() {

            GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
            Rectangle rect=ge.getMaximumWindowBounds();
            int w=rect.width;
            int h=rect.height;
            return new Dimension(w, h);
        }

        @Override
        public void invalidate() {
            cells.clear();
            selectedCell = null;
            super.invalidate();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
             g2d= (Graphics2D) g.create();

            int width = getWidth();
            int height = getHeight();


            cellWidth = (width-200) / columnCount;
            cellHeight = (height+55) / rowCount;

            xOffset=10;
            yOffset=10;

            if (cells.isEmpty()) {
                for (int row = 0; row < rowCount; row++) {
                    for (int col = 0; col < columnCount; col++) {
                        Rectangle cell = new Rectangle(
                                xOffset + (col * cellWidth),
                                yOffset + (row * cellHeight),
                                cellWidth,
                                cellHeight);
                        cells.add(cell);
                    }
                }
            }

            if (selectedCell != null) {
                int index = selectedCell.x + (selectedCell.y * columnCount);
                Rectangle cell = cells.get(index);
                g2d.setColor(Color.BLUE);
                g2d.fill(cell);
            }

            g2d.setColor(Color.GRAY);
            for (Rectangle cell : cells) {
                g2d.draw(cell);
            }
            g2d.setColor(Color.BLACK);

             sets = map.keySet();
            for (String set : sets) {
//                System.out.println(set);
                split = set.split(",");
                color = map.get(set);
                g2d.setColor(getColor(color));
                g2d.fillRect(xOffset + ( Integer.parseInt(split[0])* cellWidth),yOffset + (Integer.parseInt(split[1]) * cellHeight),cellWidth,cellHeight);
            }


            g2d.setFont(f);
            g2d.setColor(Color.BLUE);
            g2d.drawString("发送 fill x坐标 y坐标 颜色(英文) 可填充",1020,60);
            g2d.drawString("单元格 例如 fill 10 20 red",1020,84);
            g2d.setColor(Color.RED);
            g2d.drawString("发送 clear x坐标 y坐标  可清除单元格",1020,120);
            g2d.drawString("例如clear 20 50",1020,144);

            g2d.drawString("仅列出部分颜色，支持大部分常见颜色",1020,280);
            g2d.setColor(Color.RED);
            g2d.drawString("红色 red",1020,310);
            g2d.setColor(Color.GREEN);
            g2d.drawString("绿色 green",1180,310);
            g2d.setColor(Color.YELLOW);
            g2d.drawString("黄色 yellow",1020,340);
            g2d.setColor(Color.GRAY);
            g2d.drawString("灰色 gray",1180,340);
            g2d.setColor(Color.PINK);
            g2d.drawString("粉红色 pink",1020,370);
            g2d.setColor(Color.BLUE);
            g2d.drawString(" 蓝色 blue",1180,370);



            g2d.dispose();

        }

        public Color getColor(String colors){


            try {
                Field field = Class.forName("java.awt.Color").getField(colors);
                color_0 = (Color)field.get(null);
            } catch (Exception e) {
                color_0 = null; // Not defined
            }
            return color_0;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
            timer.start();
        }


    }
}