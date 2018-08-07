package mailFile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;

public class MainAttach implements ActionListener{

	JFrame frame = null;
	JTextField senderInput = null;
	JTextField receiverInput = null;
	JTextField titleInput = null;
	JTextArea bodyInput = null;
	JPanel bodyInputPanel = null;
	JScrollPane bodyInputScroll = null;
	JLabel senderInputPrompt = null;
	JLabel receiverInputPrompt = null;
	JLabel titleInputPrompt = null;
	JLabel bodyInputPrompt = null;
	JButton submit = null;
	
	JMenu menu = null;
	JMenuBar menuBar = null;
	
	FileReader fReader = null;
	BufferedReader bReader = null;
	FileOutputStream fout = null;
	
	public static void main(String[] args) throws Exception{
		
		MainAttach mainattach = new MainAttach();
		
	}
	
	public MainAttach() throws Exception
	{
		//------------------Frame Part
		
		frame = new JFrame();	//frame
		JPanel pane = new JPanel();
		frame.setSize(1000, 600);
		frame.setTitle("�H��X�� - v1.0");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		pane.setSize(1000, 600);
		pane.setLayout(new FlowLayout(FlowLayout.LEFT));	//layout
		
		senderInputPrompt = new JLabel("�п�J�ۤv��Email�a�}:");
		receiverInputPrompt = new JLabel("�п�J��誺Email�a�}:");
		titleInputPrompt = new JLabel("�п�J�H����D:");
		bodyInputPrompt = new JLabel("�п�J�ɥR���e:");
		senderInput = new JTextField(30);
		receiverInput = new JTextField(30);
		titleInput = new JTextField(30);
		bodyInput = new JTextArea(10, 30);
		bodyInputPanel = new JPanel();
		bodyInputScroll = new JScrollPane();
		submit = new JButton("�e�X");
		menu = new JMenu("����");
		menuBar = new JMenuBar();
		
		senderInputPrompt.setHorizontalAlignment(JLabel.RIGHT);
		senderInputPrompt.setPreferredSize(new Dimension(250, 30));
		senderInput.setPreferredSize(new Dimension(30, 30));
		receiverInputPrompt.setHorizontalAlignment(JLabel.RIGHT);
		receiverInputPrompt.setPreferredSize(new Dimension(250, 30));
		receiverInput.setPreferredSize(new Dimension(30, 30));
		titleInputPrompt.setHorizontalAlignment(JLabel.RIGHT);
		titleInputPrompt.setPreferredSize(new Dimension(250, 30));
		titleInput.setPreferredSize(new Dimension(30, 30));
		bodyInputPrompt.setHorizontalAlignment(JLabel.RIGHT);
		bodyInputPrompt.setPreferredSize(new Dimension(250, 30));
		bodyInput.setLineWrap(true);
		bodyInputPanel.setLayout(new BorderLayout());
		bodyInputPanel.add(bodyInputScroll, BorderLayout.WEST);
		bodyInputScroll.setViewportView(bodyInput);
		senderInputPrompt.setFont(new Font(null, Font.BOLD, 18));
		receiverInputPrompt.setFont(new Font(null, Font.BOLD, 18));
		titleInputPrompt.setFont(new Font(null, Font.BOLD, 18));
		bodyInputPrompt.setFont(new Font(null, Font.BOLD, 18));
		senderInput.setFont(new Font(null, 0, 24));
		receiverInput.setFont(new Font(null, 0, 24));
		titleInput.setFont(new Font(null, 0, 24));
		bodyInput.setFont(new Font(null, 0, 24));
		
		DefaultCaret bodyInputCaret = (DefaultCaret)bodyInput.getCaret();
		bodyInputCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		menu.setPreferredSize(new Dimension(45, 30));
		menu.setFont(new Font(null, Font.BOLD, 14));
		
		JMenuItem menuItem = new JMenuItem("����");
		menuItem.setFont(new Font(null, Font.BOLD, 14));
		menuItem.setPreferredSize(new Dimension(43, 30));
		menuItem.addActionListener(this);
		
		JMenuItem menuItem2 = new JMenuItem("�@��");
		menuItem2.setFont(new Font(null, Font.BOLD, 14));
		menuItem2.setPreferredSize(new Dimension(43, 30));
		menuItem2.addActionListener(this);
		
		menu.add(menuItem);
		menu.add(menuItem2);
		menuBar.add(menu);
		
		pane.add(senderInputPrompt);
		pane.add(senderInput);
		pane.add(receiverInputPrompt);
		pane.add(receiverInput);
		pane.add(titleInputPrompt);
		pane.add(titleInput);
		pane.add(bodyInputPrompt);
		pane.add(bodyInputPanel);
		pane.add(submit);
		
		frame.setContentPane(pane);
		frame.setJMenuBar(menuBar);
		
		submit.addActionListener(this);
		
		try
		{
			fReader = new FileReader("config");
			bReader = new BufferedReader(fReader);
			
			String sender = bReader.readLine();
			String receiver = bReader.readLine();
			this.senderInput.setText(sender);
			this.receiverInput.setText(receiver);
			
			bReader.close();
			fReader.close();
		}
		catch(FileNotFoundException e)
		{
		}
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e)
			{
				senderInput.requestFocus();
			}
		});
		
		frame.setVisible(true);	//frame visible
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == submit)
		{
			try 
			{
				fout = new FileOutputStream("config");
				fout.write((senderInput.getText() + "\n").getBytes());
				fout.write((receiverInput.getText() + "\n").getBytes());
				fout.close();
				new MailMerge(senderInput.getText(), receiverInput.getText(), titleInput.getText(), bodyInput.getText());
				JOptionPane.showMessageDialog(frame, new JLabel("<html><h2>�X������!</h2></html>"), "�T��", JOptionPane.INFORMATION_MESSAGE);
				
			} catch (Exception e1) 
			{
				
				e1.printStackTrace();
				
			}
		}
		if(e.getActionCommand().equals("����"))
		{
			try
			{
				JOptionPane.showMessageDialog(frame, new JLabel("<html><h2>���{���i�N�h�ʶl��(msg��)�X���@��eml�ɶl��A�ۭq�U�����C</h2>"
						+ "<h2>[msg�ɶȭ���outlook�}��]</h2></html>"), "����", JOptionPane.INFORMATION_MESSAGE);
				
			} catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
		if(e.getActionCommand().equals("�@��"))
		{
			try
			{
				JOptionPane.showMessageDialog(frame, new JLabel("<html><h2>EggSoupYummy���J���� ���J</h2>"
						+ "<h2><a href='mailto:catkitchen721@gmail.com'>catkitchen721@gmail.com</a></h2>"
						+ "</html>"), "�@�̸�T", JOptionPane.INFORMATION_MESSAGE);
				
			} catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
		
	}

}
