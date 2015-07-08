import java.io.*;
import java.util.Arrays;
import java.awt.event.*;

import javax.swing.event.*;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JSlider;

public class Window extends JFrame implements ActionListener, ItemListener, ChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JTextField textField;
	private JTextField txtOutputgif;
	private JTextField txtInputFile;
	private String outputString;
	private JComboBox<String> modeCombo, subColorCombo, intenseCombo, tintCombo;
	private boolean addText = false;
	private boolean addTint = false;
	private boolean growTint = false;
	private JButton btnGenerate, btnChooseFile;
	private JCheckBox chckbxAddSubtitle, chckbxTint, chckbxGrowTint;
	private JSlider speedSlider, growthSlider, burnSlider;
	private JFileChooser openFile;
	//Format for output Arguments:
	//{inputFile (0), -f (1), outputFile (2), -s (3), speed (4), -i (5), intensity (6), -m (7), mode (8), -t (9), subtitle (10),...
	//-c (11), subColor (12), -g (13), growth (14), -tint (15), tintColor (16), -b (17), burn (18), -growBurn (19)}
	private String[] outputArgs = new String[20];
	
	
	private String speedFlag, intenseFlag, modeFlag, subFlag, subColorFlag, growFlag, tintFlag, burnFlag = "";
	/**
	 * @wbp.nonvisual location=112,371
	 */

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
		openFile = new JFileChooser();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Arrays.fill(outputArgs, "");
		frame = new JFrame();
		frame.setBounds(100, 100, 454, 462);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		btnGenerate = new JButton("Generate");
		btnGenerate.setBounds(331, 405, 117, 29);
		frame.getContentPane().add(btnGenerate);
		btnGenerate.addActionListener(this);
		
		String[] modes = {"Mode 0 - Shake","Mode 1 - Pulse", "Mode 2 - Left/Right","Mode 3 - Grow"};
		modeCombo = new JComboBox<String>(modes);
		modeCombo.setBounds(100, 52, 110, 27);
		frame.getContentPane().add(modeCombo);
		modeCombo.addActionListener(this);
		
		JLabel lblSelectMode = new JLabel("Select Mode:");
		lblSelectMode.setBounds(6, 56, 82, 16);
		frame.getContentPane().add(lblSelectMode);
		
		JLabel lblSubtitle = new JLabel("Subtitle:");
		lblSubtitle.setBounds(6, 107, 61, 16);
		frame.getContentPane().add(lblSubtitle);
		
		textField = new JTextField();
		textField.setBounds(64, 101, 201, 28);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.addActionListener(this);
		
		JLabel lblSubtitleColor = new JLabel("Subtitle Color:");
		lblSubtitleColor.setBounds(6, 135, 90, 16);
		frame.getContentPane().add(lblSubtitleColor);
		
		String[] subtitleColor = {"Yellow","Red","Black","White","Green","Blue","Cyan","Pink","Pale Green"};
		subColorCombo = new JComboBox<String>(subtitleColor);
		subColorCombo.setBounds(100, 131, 110, 27);
		frame.getContentPane().add(subColorCombo);
		subColorCombo.addActionListener(this);
		
		chckbxAddSubtitle = new JCheckBox("Add Subtitle");
		chckbxAddSubtitle.setBounds(6, 78, 128, 23);
		frame.getContentPane().add(chckbxAddSubtitle);
		chckbxAddSubtitle.addItemListener(this);
		
		JLabel lblSpeed = new JLabel("Speed:");
		lblSpeed.setBounds(6, 173, 61, 16);
		frame.getContentPane().add(lblSpeed);
		
		final int fps_min = 20;
		final int fps_max = 500;
		speedSlider = new JSlider(JSlider.HORIZONTAL,fps_min,fps_max,fps_min);
		speedSlider.setBounds(47, 170, 190, 29);
		frame.getContentPane().add(speedSlider);
		speedSlider.addChangeListener(this);
		
		JLabel lblIntensity = new JLabel("Intensity:");
		lblIntensity.setBounds(6, 201, 61, 16);
		frame.getContentPane().add(lblIntensity);
		
		String[] intensities = {"Light","Soft","Medium","Heavy","Dizzy","Stop"};
		intenseCombo = new JComboBox<String>(intensities);
		intenseCombo.setBounds(64, 197, 90, 27);
		frame.getContentPane().add(intenseCombo);
		intenseCombo.addActionListener(this);
		
		txtOutputgif = new JTextField();
		txtOutputgif.setText("Output.gif");
		txtOutputgif.setBounds(20, 404, 299, 28);
		frame.getContentPane().add(txtOutputgif);
		txtOutputgif.setColumns(10);
		
		JLabel lblOutput = new JLabel("Output:");
		lblOutput.setBounds(6, 380, 61, 16);
		frame.getContentPane().add(lblOutput);
		
		JLabel lblModeGrowth = new JLabel("Mode 3 Growth:");
		lblModeGrowth.setBounds(6, 229, 103, 16);
		frame.getContentPane().add(lblModeGrowth);
		
		growthSlider = new JSlider();
		growthSlider.setValue(20);
		growthSlider.setMinimum(10);
		growthSlider.setMaximum(50);
		growthSlider.setBounds(107, 226, 190, 29);
		frame.getContentPane().add(growthSlider);
		growthSlider.addChangeListener(this);
		
		chckbxTint = new JCheckBox("Tint");
		chckbxTint.setBounds(6, 257, 128, 23);
		frame.getContentPane().add(chckbxTint);
		chckbxTint.addItemListener(this);
		
		JLabel lblTintColor = new JLabel("Tint Color:");
		lblTintColor.setBounds(6, 283, 73, 16);
		frame.getContentPane().add(lblTintColor);
		
		String[] tintColor = {"Red","Yellow","Black","White","Green","Blue","Cyan","Pink","Pale Green"};
		tintCombo = new JComboBox<String>(tintColor);
		tintCombo.setBounds(82, 279, 117, 27);
		frame.getContentPane().add(tintCombo);
		tintCombo.addActionListener(this);
		
		JLabel lblBurn = new JLabel("Tint Burn:");
		lblBurn.setBounds(6, 311, 61, 16);
		frame.getContentPane().add(lblBurn);
		
		burnSlider = new JSlider();
		burnSlider.setValue(3);
		burnSlider.setMinimum(0);
		burnSlider.setMaximum(10);
		burnSlider.setBounds(75, 308, 190, 29);
		frame.getContentPane().add(burnSlider);
		burnSlider.addChangeListener(this);
		
		chckbxGrowTint = new JCheckBox("Grow Tint");
		chckbxGrowTint.setBounds(6, 339, 128, 23);
		frame.getContentPane().add(chckbxGrowTint);
		chckbxGrowTint.addItemListener(this);
		
		JLabel lblFile = new JLabel("File:");
		lblFile.setBounds(6, 16, 61, 16);
		frame.getContentPane().add(lblFile);
		
		txtInputFile = new JTextField();
		txtInputFile.setText("Input File");
		txtInputFile.setBounds(36, 10, 184, 28);
		frame.getContentPane().add(txtInputFile);
		txtInputFile.setColumns(10);
		
		btnChooseFile = new JButton("Choose file...");
		btnChooseFile.setBounds(224, 11, 117, 29);
		frame.getContentPane().add(btnChooseFile);
		btnChooseFile.addActionListener(this);
		
		//openFile = new JFileChooser();
	}
	
	public void actionPerformed(ActionEvent e){
		Object source = e.getSource();
		JComboBox<String> cb;
		String inputString;
		if(source == modeCombo){
			cb = (JComboBox<String>)source;
			inputString = (String)cb.getSelectedItem();
			outputArgs[7] = "-m";
			if(inputString.equals("Mode 0 - Shake")){
				outputArgs[8] = "0";
			}else if(inputString.equals("Mode 1 - Pulse")){
				outputArgs[8] = "1";
			}else if(inputString.equals("Mode 2 - Left/Right")){
				outputArgs[8] = "2";
			}else if(inputString.equals("Mode 3 - Grow")){
				outputArgs[8] = "3";
			}
		}else if(source == subColorCombo){
			cb = (JComboBox<String>)source;
			outputArgs[11] = "-c";
			outputArgs[12] = (String)cb.getSelectedItem();
		}else if(source == intenseCombo){
			cb = (JComboBox<String>)source;
			outputArgs[5] = "-i";
			outputArgs[6] = (String)cb.getSelectedItem();
		}else if(source == tintCombo){
			cb = (JComboBox<String>)source;
			outputArgs[15] = "-tint";
			outputArgs[16] = (String)cb.getSelectedItem();
		}else if(source == btnChooseFile){
			int returnVal = openFile.showOpenDialog(Window.this);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				File file = openFile.getSelectedFile();
				txtInputFile.setText(file.getAbsolutePath());
			}
		}else if(source == btnGenerate){
			System.out.println("Generating file...");
			outputArgs[0] = txtInputFile.getText();
			outputArgs[1] = "-f";
			outputArgs[2] = txtOutputgif.getText(); //Set up the input and output files
			if(addText){
				outputArgs[9] = "-t";
				outputArgs[10] = textField.getText();
			}if(addTint){
				outputArgs[15] = "-tint";
				outputArgs[16] = (String)tintCombo.getSelectedItem();
				outputArgs[17] = "-b";
				int burn = (int)burnSlider.getValue();
				outputArgs[18] = String.valueOf(burn);
			}if(growTint){
				outputArgs[19] = "-growBurn";
			}
				//cleanUp();
			try{
				intensifier.main(outputArgs);
			}catch(IOException err){}
		}
		
	}
	
	private void cleanUp(){
		speedFlag = "";
		intenseFlag = "";
		modeFlag = "";
		subFlag = "";
		subColorFlag = "";
		growFlag = "";
		tintFlag = "";
		burnFlag = "";
		addText = false;
		addTint = false;
		growTint = false;
	}
	
	private String[] createOutput(){
		String[] outputArgs = {txtInputFile.getText(), "-f", txtOutputgif.getText()};
		
		return outputArgs;
	}
	
	public void itemStateChanged(ItemEvent i){
		Object source = i.getItemSelectable();
		if(source == chckbxAddSubtitle){
			if(i.getStateChange() == ItemEvent.SELECTED){
				addText = true;
			}else{
				addText = false;
				outputArgs[9] = "";
			}
		}else if(source == chckbxTint){
			if(i.getStateChange() == ItemEvent.SELECTED){
				addTint = true;
			}else{
				addTint = false;
				outputArgs[15] = "";
			}
		}else if(source == chckbxGrowTint){
			if(i.getStateChange() == ItemEvent.SELECTED){
				growTint = true;
			}else{
				growTint = false;
				outputArgs[17] = "";
			}
		}
	}
	
	public void stateChanged(ChangeEvent c){
		JSlider source = (JSlider)c.getSource();
		if(!source.getValueIsAdjusting()){
			if(source == speedSlider){
				int fps = (int)source.getValue();
				outputArgs[3] = "-s";
				outputArgs[4] = String.valueOf(fps);
			}else if(source == growthSlider){
				int growth = (int)source.getValue();
				outputArgs[13] = "-g";
				outputArgs[14] = String.valueOf(growth);
			}else if(source == burnSlider){
				int burn = (int)source.getValue();
				outputArgs[17] = "-b";
				outputArgs[18] = String.valueOf(burn);
			}
		}
	}
}
