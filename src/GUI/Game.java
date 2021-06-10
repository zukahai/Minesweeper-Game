package GUI;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JFrame implements MouseListener{
	int MAX = 100;
	int M = 10, N = 10;
	int mines = 10;
	int countMines = 0;
	JPanel pn;
	JButton[][]bt = new JButton[MAX][MAX];
	int a[][] = new int[MAX][MAX];
	int number[][] = new int[MAX][MAX];
	boolean t[][] = new boolean[MAX][MAX];
	boolean flag[][] = new boolean[MAX][MAX];
	Container cn;
	
	public Game() {
		super("HaiZuka");
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
				bt[i][j].setIcon(getIcon(999));
				pn.add(bt[i][j]);
			}
//		updateMatrix();
		cn.add(pn);
		
		this.setVisible(true);
		this.setSize(500, 500);
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
				if (kt(h, k) && a[h][k] == 0 && countMines < mines && Math.random() < 0.15) {
					a[h][k] = 1;
					countMines++;
					loang(h, k);
				}
	}
	
	public Icon getIcon(int index) {
		int w = 52;
		int h = 50;
		Image image = new ImageIcon(getClass().getResource("/Icons/" + index + ".png")).getImage();
		Icon ic = new ImageIcon(image.getScaledInstance(w, h, image.SCALE_SMOOTH));
		return ic;
	}
	
	public void updateMatrix() {
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				bt[i][j].setIcon(getIcon(number[i][j]));
	}
	
	public void printArray() {
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++)
				System.out.print(a[i][j] + " ");
			System.out.println();
		}
	}
	
	public void open(int I, int J) {
		if (t[I][J] == true && flag[I][J] == false && number[I][J] != 9) {
			bt[I][J].setIcon(getIcon(number[I][J]));
			t[I][J] = false;
			if (number[I][J] == 0)
				for (int i = I - 1; i <= I + 1; i++)
					for (int j = J - 1; j <= J + 1; j++)
						if (kt(i, j))
							open(i, j);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		JButton but = (JButton) e.getComponent();
		int K = Integer.parseInt(but.getActionCommand());
//		System.out.println(K);
		int J = K % N;
		int I = K / N;
		if (MouseEvent.BUTTON1 == e.getButton()) {
			open(I, J);
		} else if (MouseEvent.BUTTON3 == e.getButton()) {
			if (flag[I][J] == false && t[I][J] == true) {
				flag[I][J] = true;
				bt[I][J].setIcon(getIcon(998));
			}
		}
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
		new Game();
	}
}
