package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.*;

public class Game extends JFrame implements ActionListener, KeyListener {
	int BOM, dem = 0;
	int level;
	int maxXY = 100;
	boolean  die = false;
	Timer timer;
	boolean flag = false;
	private Color bom_cl = Color.red;
	int  key_flag = KeyEvent.VK_H;
	private Color background_number_cl = Color.yellow;
	private Color background_null_cl = Color.gray;
	private Color flag_cl = Color.green;
	private int m, n;
	int M[] = {8, 15, 21};
	int N[] = {10, 19, 27};
	int Mines[] = {10, 40, 100};
	private int values[][] = new int[maxXY][maxXY];
	private JButton bt[][] = new JButton[maxXY][maxXY];
	private JLabel point_lb, temp_lb, flag_lb, lv_lb;
	private JComboBox lv = new JComboBox();
	private boolean tick[][] = new boolean[maxXY][maxXY];
	private JButton repeat_bt, mines_bt, highScore_bt;
	private JPanel pn0, pn, pn2;
	Container cn;
	public Game(String s, int k) {
		super(s);
		level = k + 1;
		BOM = Mines[k];
		m = M[k];
		n = N[k];
		initBom();
		cn = this.getContentPane();
		pn = new JPanel();
		pn.setLayout(new GridLayout(m,n));
		for (int i = 0; i <= m + 1; i++)
			for (int j = 0; j <= n + 1; j++){
				bt[i][j] = new JButton("   ");
				tick[i][j] = true;
			}
		for (int i = 1; i <= m; i++)
			for (int j = 1; j <= n; j++){
				bt[i][j] = new JButton("   ");
				pn.add(bt[i][j]);
				bt[i][j].setActionCommand(i + " " + j);
				bt[i][j].addActionListener(this);
				bt[i][j].addKeyListener(this);
			}
		point_lb = new JLabel("00:00:00:00");
		point_lb.setFont(new Font("Arial", 1, 20));
		highScore_bt = new JButton("High Score");
		highScore_bt.setFont(new Font("Arial", 1, 20));
		highScore_bt.addActionListener(this);
		
		temp_lb = new JLabel("     ");
		mines_bt = new JButton(String.valueOf(BOM));
		mines_bt.setBackground(flag_cl);
		repeat_bt = new JButton("Ván mới");
		repeat_bt.addActionListener(this);
		flag_lb = new JLabel("Đang gắn bom (h)");
		flag_lb.setForeground(bom_cl);
		flag_lb.setFont(new Font("Arial", 1, 15));
		lv_lb = new JLabel("Mức độ: ");
		lv_lb.setFont(new Font("Arial", 1, 15));
		lv.addItem("Dễ");
		lv.addItem("Trung bình");
		lv.addItem("Khó");
		lv.setSelectedIndex(k);
		pn2 = new JPanel();
		pn2.setLayout(new FlowLayout());
		pn2.add(lv_lb);
		pn2.add(lv);
		pn2.add(mines_bt);
		pn2.add(repeat_bt);
		pn2.add(flag_lb);
		pn0= new JPanel();
		pn0.setLayout(new FlowLayout());
		pn0.add(point_lb);
		pn0.add(temp_lb);
		pn0.add(highScore_bt);
		cn.add(pn0, "North");
		cn.add(pn);
		cn.add(pn2, "South");
		this.setVisible(true);
		this.pack();
//		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		timer = new Timer(10, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				point_lb.setText(next(point_lb));
			}
		});
//		for (int  i = 1; i <= m; i++) {
//			for (int j = 1; j <= n; j++)
//				System.out.printf("%2s ", values[i][j]);
//			System.out.println("\n");
//		}
	}
	public void initBom() {
		float ratio = (float) 0.05; // Tá»‰ lá»‡ táº¡o mÃ¬n nhá»¯ng Ã´ xung quanh
		int i, j;
		for (i = 0; i <= m + 1; i++)
			for (j = 0; j <= n + 1; j++)
				values[i][j] = 0;
		while (dem < BOM) {
			// Chá»?n ra táº¡o Ä‘á»™ chÆ°a pháº£i lÃ  mÃ¬n.
			do {
				i = (int) ((m - 1) * Math.random()) + 1;
				j = (int) ((n - 2) * Math.random()) + 1;
			} while (values[i][j] != 0);
			if (values[i][j] == 0) 
				init(i, j, ratio);
		}
	}
	public void init(int i, int j, float ratio) {
		if (Math.random() < ratio) {
			values[i][j] = -1;
			for (int k = i - 1; k <= i + 1; k++)
				for (int h = j - 1; h <= j + 1; h++)
					if (values[k][h] != -1) {
						values[k][h]++;
					}
			dem++;
			//Gá»?i hÃ m táº¡o bom cho nhá»¯ng Ã´ xung quanh.
			for (int k = i - 1; k <= i + 1; k++)
				for (int h = j - 1; h <= j + 1; h++)
					if (k > 0 && h > 0 && values[k][h] != -1 && dem < BOM)
						init(k, h, ratio);
		}
	}
	public String next(JLabel lb) {
		String str[] = lb.getText().split(":");
		int tt = Integer.parseInt(str[3]);
		int s = Integer.parseInt(str[2]);
		int m = Integer.parseInt(str[1]);
		int h = Integer.parseInt(str[0]);
		String kq = "";
		int sum = tt + s * 100 + m * 60 * 100 + h * 60 * 60 * 100 + 1;
		if (sum % 100 > 9)
			kq = ":" + sum % 100 + kq;
		else
			kq = ":0" + sum % 100 + kq;
		sum /= 100;
		
		if (sum % 60 > 9)
			kq = ":" + sum % 60 + kq;
		else
			kq = ":0" + sum % 60 + kq;
		sum /= 60;
		
		if (sum % 60 > 9)
			kq = ":" + sum % 60 + kq;
		else
			kq = ":0" + sum % 60 + kq;
		sum /= 60;
		if (sum > 9)
			kq = sum + kq;
		else
			kq = "0" + sum +kq;
		return kq;
	}
	public void open(int i, int j) {
		if (tick[i][j] && values[i][j] != -1) {
			bt[i][j].setText(String.valueOf(values[i][j]));
			bt[i][j].setBackground(background_number_cl);
			tick[i][j] = false;
			checkWin();
		}
	}
	public void openEmpty(int i, int j) {
		if (tick[i][j]) {
			tick[i][j] = false;
			bt[i][j].setBackground(background_null_cl);
			checkWin();
			for (int h = i - 1; h <= i + 1; h++)
				for (int k = j - 1; k <= j + 1; k++)
					if (h >= 0 && h <= m && k >= 0 && k <= n) {
						if (values[h][k] == 0 && tick[h][k])
							openEmpty(h, k);
						else
							open(h, k);
					}
		}
	}
	public void addFlag(int i, int j) {
		if (bt[i][j].getBackground() == flag_cl) {
				tick[i][j] = true;
				bt[i][j].setBackground(null);
				mines_bt.setText(String.valueOf(Integer.parseInt(mines_bt.getText()) + 1));
		}
		else if (tick[i][j] && Integer.parseInt(mines_bt.getText()) > 0) {
			tick[i][j] = false;
			bt[i][j].setBackground(flag_cl);
			mines_bt.setText(String.valueOf(Integer.parseInt(mines_bt.getText()) - 1));
		}
		checkWin();
	}
	public void checkWin() {
		for (int i = 1; i <= m; i++)
			for (int j = 1; j <= n; j++)
				if (tick[i][j]) {
					return;
				}
		int k = 0;
		for (int i = 1; i <= m; i++)
			for (int j = 1; j <= n; j++)
				if (bt[i][j].getBackground() == flag_cl && values[i][j] != -1)
					return;
				else if (bt[i][j].getBackground() == flag_cl)
					k++;
		if (k <= BOM) {
			timer.stop();
			try {
				checkPoint(point_lb.getText(), lv.getSelectedIndex());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void checkPoint(String s, int k) throws IOException {
		String file = "point.txt";
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String str[] = {"", "", ""};
		for (int i = 0; i <= 2; i++)
			str[i] = br.readLine();
		fr.close();
		if (s.compareTo(str[k]) < 0) {
			str[k] = s;
			FileWriter f = new FileWriter(file);
			f.write(str[0] + "\n");
			f.write(str[1] + "\n");
			f.write(str[2] + "\n");
			f.flush();
			f.close();
		}
	}
	public void loss() {
		for (int i = 1; i <= m; i++)
			for (int j = 1; j <= n; j++)
				if (values[i][j] == -1) {
					bt[i][j].setBackground(bom_cl);
					bt[i][j].setText("X");
				}
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == repeat_bt.getText()) {
			new Game("Demo - Game dò mìn", lv.getSelectedIndex());
			this.dispose();
		}
		else if (e.getActionCommand().equals(highScore_bt.getText())) {
			
		} else if (!die) {
			timer.start();
			int i = 0,j = 0;
			String s = e.getActionCommand();
			int k = s.indexOf(32);
			i = Integer.parseInt(s.substring(0, k));
			j = Integer.parseInt(s.substring (k + 1, s.length()));
			if (!flag) {
				if (values[i][j] == -1 ) {
					if (bt[i][j].getBackground() != flag_cl) {
						bt[i][j].setBackground(bom_cl);
						bt[i][j].setText("X");
						timer.stop();
						loss();
						JOptionPane.showMessageDialog(null, "Trúng bom, bạn đã thua!");
						die = true;
					}
				}
				else if (values[i][j] == 0) {
					openEmpty(i, j);
				}
				else {
					open(i, j);
				}
			}
			else {
				addFlag(i, j);
			}
		}
	}
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == key_flag) {
			flag = !flag;
		}
		if (flag) {
			flag_lb.setText("Đang gán cờ (h)");
			flag_lb.setForeground(flag_cl);
		}
		else {
			flag_lb.setText("Đang gỡ bom (h)");
			flag_lb.setForeground(bom_cl);
		}
		
	}
	public void keyTyped(KeyEvent e) {
		
	}
	public void keyReleased(KeyEvent e) {
		
	}
	public static void main(String[] args) {
		new Game("Demo - Game dò mìn", 0);
	}
}