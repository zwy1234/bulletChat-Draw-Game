import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        updateByPeople update=new updateByPeople();
        Thread t1=new Thread(update);
        t1.start();

        //UI thread
        new UI();

    }
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
        private int columnCount = 200;
        private int rowCount = 200;
        private List<Rectangle> cells;
        private Point selectedCell;
        JButton startBtn;

        static Timer timer;//100毫秒执行一次
        {
            timer = new Timer(100, this);
        }



        public TestPane() {
//            this.setBackground();
            cells = new ArrayList<>(columnCount * rowCount);
            startBtn = new JButton("开始");
            startBtn.setBounds(20,20,20,20);
//            startBtn.setEnabled(false);
            startBtn.addActionListener((e)-> {
                int x = (int)(Math.random()*columnCount);
                int y = (int)(Math.random()*rowCount);

            });

//            add(startBtn);
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
            Graphics2D g2d = (Graphics2D) g.create();

            int width = getWidth();
            int height = getHeight();
//            System.out.println("heihgt"+height);
//            System.out.println("width"+width);

//            //修改
//            width=h;

             int test0=25;

            int cellWidth = (width-200) / columnCount;
            int cellHeight = (height+55) / rowCount;


            int xOffset = (width - (columnCount * cellWidth)) / 2;
            int yOffset = (height - (rowCount * cellHeight)) / 2;
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

            Set<String> sets = map.keySet();
            for (String set : sets) {
//                System.out.println(set);
                String[] split = set.split(",");
                String color = map.get(set);
                g2d.setColor(getColor(color));
                g2d.fillRect(xOffset + ( Integer.parseInt(split[0])* cellWidth),yOffset + (Integer.parseInt(split[1]) * cellHeight),cellWidth,cellHeight);
            }

            Font f = new Font("隶书",Font.PLAIN,20);
            g2d.setFont(f);
            g2d.setColor(Color.BLUE);
            g2d.drawString("发送 fill x坐标 y坐标 颜色(英文) 可填充",1020,200);
            g2d.drawString("单元格 例如 fill 10 20 red",1020,224);
            g2d.setColor(Color.GREEN);
            g2d.drawString("发送 clear x坐标 y坐标  可清除单元格",1020,250);
            g2d.drawString("例如clear 20 50",1020,270);
            g2d.dispose();
        }

        public Color getColor(String colors){

            Color color;
            try {
                Field field = Class.forName("java.awt.Color").getField(colors);
                color = (Color)field.get(null);
            } catch (Exception e) {
                color = null; // Not defined
            }
            return color;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();

            timer.start();
        }


    }
}