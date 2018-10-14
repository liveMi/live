package com;

import java.io.File;
import java.util.regex.Pattern;

import javax.swing.filechooser.FileSystemView;

import jmtp.PortableDevice;
import jmtp.PortableDeviceFolderObject;
import jmtp.PortableDeviceManager;
import jmtp.PortableDeviceObject;
import jmtp.PortableDeviceStorageObject;

/**
 * 单例模式
 * 对usb接口访问唯一
 * @author Administrator
 */
public class ForSample
{

	static FileSystemView fsv = FileSystemView.getFileSystemView();
	
	//过滤标准
	final static String filter=".+(.JPEG|.jpeg|.JPG|.jpg|.png|.PNG|.ico|.bmp|.gif|.tiff)$";
	//过滤标准
	final static String filter1=".+(.aac|.mp3|.mp1|.mp2|.au|.ra|.rm|.ram|.mid|.rmi|.au|.aif|.aiff|.wav)$";
	//过滤标准
	final static String filter2=".+(.mkv|.mp4|.3gp|.mpg|.wmv|.asx|.flv|.avi|.mov)$";

	public static boolean all = false;
	public static boolean img = true;
	public static boolean audio = true;
	public static boolean video = true;
	
	//存放地址
	public static String urlFile=fsv.getHomeDirectory().getPath()+File.separator+"FILE";
	
	//文件数目
	public static Integer numberFile=0;
	
	//盘符数目
	public static Integer numberDevices=0;

	//接口数目
	public static Integer usbPortNumber=0;
	
	public static boolean bn;
	
	
	public ForSample(){
		
	};
	
	private static void add(PortableDeviceObject obj) {
		numberFile++;
		File file = new File(urlFile);
		if(!file.exists()) 
         {
             file.mkdirs();
        }
		Start.textField2.setText("《新增文件"+file.getPath()+File.separator+obj.getOriginalFileName()+"》         ");
		obj.copy(file.toPath());
		FfmpegTool.add(urlFile+File.separator+obj.getOriginalFileName());
	}
	
	private static void fileAdd(PortableDeviceObject obj)
	{
		if(!(obj instanceof PortableDeviceFolderObject)) 
		{
			if(all) {
				add(obj);
			}else {
				if(img) {
					if(Pattern.matches(filter, obj.getOriginalFileName().toLowerCase()))
					{
						add(obj);
					}
				}
				if(audio) {
					if(Pattern.matches(filter1, obj.getOriginalFileName().toLowerCase()))
					{
						add(obj);
					}
				}
				if(video) {
					if(Pattern.matches(filter2, obj.getOriginalFileName().toLowerCase()))
					{
						add(obj);
					}
				}
			}
			
		}else{
			for(PortableDeviceObject subObj:((PortableDeviceFolderObject) obj).getChildObjects()) 
			{
				fileAdd(subObj);
			}
		}
	}
	
	public static void numberStatistical(){
		PortableDeviceManager manager = new PortableDeviceManager();
		
		//遍历接口
        for (PortableDevice device : manager) 
        {

        	usbPortNumber++;
        	//连接接口
            device.open();
            
            // 遍历盘符
            for (PortableDeviceObject object : device.getRootObjects()) 
            {
            	//盘符数
            	numberDevices++;
                //判断是否子类对象
                if (object instanceof PortableDeviceStorageObject) 
                {
                    PortableDeviceStorageObject storage = (PortableDeviceStorageObject) object;

                    // 遍历文件/文件夹，从最外层
                    for (PortableDeviceObject child : storage.getChildObjects()) 
                    {
                    	fileAdd(child);
                    }
                }
            }
            //接口资源处理,不影响读取文件
            device.close();
            bn = true;
        }
		
	}
	
}
