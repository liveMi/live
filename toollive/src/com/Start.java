package com;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
	public class Start extends JFrame implements ActionListener
	{
		
		private static final long serialVersionUID = 1L;
		// 创建复选框
		public static JCheckBox checkBox01 = new JCheckBox("全部",false);
		public static JCheckBox checkBox02 = new JCheckBox("图片",true);
		public static JCheckBox checkBox03 = new JCheckBox("音频",true);
		public static JCheckBox checkBox04 = new JCheckBox("视频",true);
	    private JLabel textField = new JLabel(); 
	    public static JLabel textField2 = new JLabel(); 
	    public static JLabel textField4 = new JLabel(); 
	    private JLabel textField3 = new JLabel(); 
	    private JButton start = new JButton("开始");
	    private JButton url = new JButton("设置存储路径");
	    private Thread workThead = null;  
	    private Start live;
	    public Start() {  
	    	live = this;
	    	textField3.setForeground(Color.red);
	        this.setLayout(new FlowLayout()); 
	        this.add(textField2); 
	        this.add(url);   
	        url.addActionListener(this);
	        this.add(start);  
	        //按钮的监听器，负责由事件触发后创建工作线程  
	        start.addActionListener(new ActionListener() {  
	            public void actionPerformed(ActionEvent e) {  
	                // TODO Auto-generated method stub  
	            	live.remove(start);
	            	live.remove(url);
	            	live.remove(checkBox01);
	            	live.remove(checkBox02);
	            	live.remove(checkBox03);
	            	live.remove(checkBox04);
	                if (workThead == null) { 
	                    workThead = new WorkThead();  
	                    workThead.start();  
	                }  
	            }  
	        });  
	        this.add(checkBox01);  
	        checkBox01.addChangeListener(new ChangeListener() {
	            @Override
	            public void stateChanged(ChangeEvent e) {
	                JCheckBox checkBox = (JCheckBox) e.getSource();
	                ForSample.all = checkBox.isSelected();
	            }
	        });
	        this.add(checkBox02);  
	        checkBox02.addChangeListener(new ChangeListener() {
	            @Override
	            public void stateChanged(ChangeEvent e) {
	                JCheckBox checkBox = (JCheckBox) e.getSource();
	                ForSample.img = checkBox.isSelected();
	            }
	        });
	        this.add(checkBox03);  
	        checkBox03.addChangeListener(new ChangeListener() {
	            @Override
	            public void stateChanged(ChangeEvent e) {
	                JCheckBox checkBox = (JCheckBox) e.getSource();
	                ForSample.audio = checkBox.isSelected();
	            }
	        });
	        this.add(checkBox04);  
	        checkBox04.addChangeListener(new ChangeListener() {
	            @Override
	            public void stateChanged(ChangeEvent e) {
	                JCheckBox checkBox = (JCheckBox) e.getSource();
	                ForSample.video = checkBox.isSelected();
	            }
	        });
	        this.add(textField);  
	        this.add(textField3); 
	        this.add(textField4);
	    }  

	    class WorkThead extends Thread{  
	        public void run() {  
	        	new Thread(new Runnable() {
					@Override
					public void run() {
						ForSample.numberStatistical();
					}
				}).start();
                
	            while (true) {  
	                try {  
	                    Thread.sleep(500);  
	                } catch (InterruptedException e) {  
	                    // TODO Auto-generated catch block  
	                    e.printStackTrace();  
	                }  
	                    SwingUtilities.invokeLater(new Runnable() {  
	                    	@Override
	                        public void run() {  
	                    		textField.setText(" 设备数:  \n"+ForSample.numberDevices+" 文件数:  "+ForSample.numberFile+" 需转换数:  "+FfmpegTool.count2+" 转换成功:  "+FfmpegTool.count);
	                    		if(ForSample.bn) {
	                    			if(FfmpegTool.count2 == FfmpegTool.count) {
	                    				textField4.setText("<转换> <完成>");
		                    			return;
		                    		}
	                    			textField3.setText("<传输> <完成>");
	                    		}
	                    	}  
	                    });  
	                
	            }  
	        }  
	    } 
	    
	    
	    public static void main( String[] args )
	    {
	    	Start jTest = new Start();  
	        jTest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	        ImageIcon icon=new ImageIcon(System.getProperty("user.dir")+"/img/live.png");
	        jTest.setIconImage(icon.getImage());
	        jTest.setSize(700, 100);  
	        jTest.setResizable(false);
	        jTest.setVisible(true);
	        jTest.setLocationRelativeTo(null);
	    }
	    
	    @Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JFileChooser jfc=new JFileChooser("选择存放文件的位置");
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			jfc.showDialog(new JLabel(), "选择");
			File file=jfc.getSelectedFile();
			if(file!=null&&file.isDirectory()){
				textField2.setText("<储存位置:> "+file.getAbsolutePath());
				ForSample.urlFile = file.getAbsolutePath();
			}
		}
}
