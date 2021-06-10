package GUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game extends JFrame implements MouseListener, ActionListener{
	int MAX = 100;
	int M = 9, N = 9;
	int mines = 10;
	int countMines = 0;
	int sizeX[] = {392, 636, 1190};
	int sizeY[] = {460, 700, 700};
	int mX[] = {9, 16, 16};
	int nY[] = {9, 16, 30};
	int mine[] = {10, 40, 99};
	int W = 360, H = 360;
	int lv = 0;
	JPanel pn, pn2;
	JButton[][]bt = new JButton[MAX][MAX];
	JButton newGame_bt, lv_bt;
	JLabel flag_lb;
	int a[][] = new int[MAX][MAX];
	int number[][] = new int[MAX][MAX];
	boolean t[][] = new boolean[MAX][MAX];
	boolean flag[][] = new boolean[MAX][MAX];
	Container cn;
	boolean click = true;
	public Game(int lv) {
		super("HaiZuka");
		this.lv = lv;
		this.M = mX[lv - 1];
		this.N = nY[lv - 1];
		this.W = sizeX[lv - 1];
		this.H = sizeY[lv - 1];
		this.mines = mine[lv - 1];
		cn = init();
	}
	
	public Container init() {
		Container cn = this.getContentPane();
		
		initMines();
		
		pn = new JPanel();
		pn.setLayout(new GridLayout(M, N));
		
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++) {
				bt[i][j] = new JButton();
				bt[i][j].addMouseListener(this);
				bt[i][j].setActionCommand(String.valueOf(i * N + j));
				bt[i][j].setIcon(getIcon("999"));
				pn.add(bt[i][j]);
			}
		
		pn2 = new JPanel();
		pn2.setLayout(new FlowLayout());
		
		newGame_bt = new JButton();
		newGame_bt.setActionCommand("newGame");
		newGame_bt.addActionListener(this);
		newGame_bt.setIcon(getIcon2("c1", 30, 30));
		newGame_bt.setBackground(Color.gray);
		newGame_bt.setBorder(null);
		
		lv_bt = new JButton();
		lv_bt.setActionCommand("lv_bt");
		lv_bt.setIcon(getIcon2("lv" + lv, 94, 30));
		lv_bt.setBackground(Color.gray);
		lv_bt.setBorder(null);
		lv_bt.addActionListener(this);
		
		flag_lb = new JLabel(mines + "");
		flag_lb.setFont(new Font("UTM Nokia", 1, 28));
		flag_lb.setForeground(Color.blue);
		
		pn2.add(lv_bt);
		pn2.add(newGame_bt);
		pn2.add(flag_lb);
		
		
//		updateMatrix();
		cn.add(pn);
		cn.add(pn2, "North");
		
		this.setVisible(true);
		this.setSize(W, H);
		this.setLocationRelativeTo(null);
//		this.setResizable(false);
		return cn;
	}
	
	public void initMines() {
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++) {
				a[i][j] = 0;
				number[i][j] = 0;
				t[i][j] = true;
				flag[i][j] = false;
		}
		int I, J;
		while (countMines < mines) {
			do {
				I = (int) (Math.random() * M);
				J = (int) (Math.random() * N);
			} while (a[I][J] == 1);
			a[I][J] = 1;
			countMines++;
			loang(I, J);
		}
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++) 
				if (a[i][j] == 1){
					for (int h = i - 1; h <= i + 1; h++)
						for (int k = j - 1; k <= j + 1; k++)
							if (kt(h, k) && a[h][k] == 0)
								number[h][k]++;
				}
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++) 
				if (a[i][j] == 1)
						number[i][j] = 9;
	}
	
	public boolean kt(int i, int j) {
		if (i >= M || i < 0 || j >= N || j < 0)
			return false;
		return true;
	}
	
	public void loang(int i, int j) {
		for (int h = i - 1; h <= i + 1; h++)
			for (int k = j - 1; k <= j + 1; k++)
				if (kt(h, k) && a[h][k] == 0 && countMines < mines && Math.random() < 0.05) {
					a[h][k] = 1;
					countMines++;
					loang(h, k);
				}
	}
	
	public Icon getIcon(String index) {
		int w = 39;
		int h = 39;
		Image image = new ImageIcon(getClass().getResource("/Icons/" + index + ".png")).getImage();
		Icon ic = new ImageIcon(image.getScaledInstance(w, h, image.SCALE_SMOOTH));
		return ic;
	}
	
	public Icon getIcon2(String index, int W, int H) {
		int w = W;
		int h = H;
		Image image = new ImageIcon(getClass().getResource("/Icons/" + index + ".png")).getImage();
		Icon ic = new ImageIcon(image.getScaledInstance(w, h, image.SCALE_SMOOTH));
		return ic;
	}
	
	public void updateMatrix() {
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				bt[i][j].setIcon(getIcon(number[i][j] + ""));
	}
	
	public void printArray() {
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++)
				System.out.print(a[i][j] + " ");
			System.out.println();
		}
	}
	
	public void checkWin() {
		int count = 0;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < M; j++)
				if (t[i][j])
				count ++;
		if (count == mines) {
			click = false;
			for (int i = 0; i < N; i++)
				for (int j = 0; j < M; j++)
					if (t[i][j])
						bt[i][j].setIcon(getIcon("998"));
			newGame_bt.setIcon(getIcon2("c3", 30, 30));
//			JOptionPane.showMessageDialog(null, "Win");
		}
	}
	
	public void loss() {
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				if (number[i][j] == 9 && flag[i][j] == false)
					bt[i][j].setIcon(getIcon("9"));
		click = false;
		newGame_bt.setIcon(getIcon2("c2", 30, 30));
//		JOptionPane.showMessageDialog(null, "Loser");
	}
	
	public void open(int I, int J) {
		if (t[I][J] == true && flag[I][J] == false) {
			if (number[I][J] != 9) {
				bt[I][J].setIcon(getIcon(number[I][J] + ""));
				t[I][J] = false;
				if (number[I][J] == 0)
					for (int i = I - 1; i <= I + 1; i++)
						for (int j = J - 1; j <= J + 1; j++)
							if (kt(i, j))
								open(i, j);
			} else {
				loss();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (!click)
			return;
		JButton but = (JButton) e.getComponent();
		int K = Integer.parseInt(but.getActionCommand());
//		System.out.println(K);
		int J = K % N;
		int I = K / N;
		if (MouseEvent.BUTTON1 == e.getButton()) {
			if (t[I][J] == true)
				open(I, J);
			else if (number[I][J] != 0){
				int count = 0;
				for (int i = I - 1; i <= I + 1; i++)
					for (int j = J - 1; j <= J + 1; j++)
						if (kt(i, j) && flag[i][j])
							count ++;
				if (count == number[I][J]) {
					for (int i = I - 1; i <= I + 1; i++)
						for (int j = J - 1; j <= J + 1; j++)
							if (kt(i, j))
								open(i, j);
				}
			}
		} else if (MouseEvent.BUTTON3 == e.getButton()) {
			if (flag[I][J] == false && t[I][J] == true) {
				flag[I][J] = true;
				bt[I][J].setIcon(getIcon(998 + ""));
				flag_lb.setText(Integer.parseInt(flag_lb.getText()) - 1 + "");
			}
			else if (flag[I][J] == true && t[I][J] == true) {
				flag[I][J] = false;
				bt[I][J].setIcon(getIcon(999 + ""));
				flag_lb.setText(Integer.parseInt(flag_lb.getText()) + 1 + "");
			}
		}
		checkWin();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		new Game(2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals(newGame_bt.getActionCommand())) {
			this.dispose();
			new Game(lv);
		} else if (e.getActionCommand().equals(lv_bt.getActionCommand())) {
			lv++;
			if (lv == 4)
				lv = 1;
			lv_bt.setIcon(getIcon2("lv" + lv, 94, 30));;
		}
	}
}
