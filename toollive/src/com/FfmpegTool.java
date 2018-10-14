package com;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
//@Scope("prototype")
public class FfmpegTool {
	
	//转换工具路径
	private static final String BASE_PATH=System.getProperty("user.dir")+"/ffmpeg/bin/ffmpeg";

	private static String urlFile = ForSample.urlFile+"/mp4/";	
	
	public static StackForSample<String> sample = new StackForSample<>();
	
	public static int count;
	public static int count2;
	public static boolean bn;
	private static String filter = ".+(.mkv|.mp4|.3gp|.mpg|.wmv|.asx|.flv|.avi|.mov)$";
	public static void add(String url) {
		if(!Pattern.matches(filter, url))return;
		count2++;
		sample.add(url);
		if(!bn) {
			bn = true;
			new Thread(new Runnable() {
				@Override
				public void run() {
					String url = FfmpegTool.sample.getFirstAndRemove(); 
					do{
						FfmpegTool.getResult(url);
						url = FfmpegTool.sample.getFirstAndRemove();
					}while(null != url);
					FfmpegTool.bn =false;
				}
			}).start();
		}
	}
	
    /**
     * 直接解析视频
     * ffmpeg能解析的格式：（asx，asf，mpg，wmv�?3gp，mp4，mov，avi，flv等）
     * @param 文件地址
     * @param 转换后的类型
     * @return
     */
	public static boolean getResult(String url) {
    	System.out.println("进来主方法了"+url);
    	File file2=new File(urlFile);
    	if(!file2.exists()) {
    		file2.mkdirs();
    	}
        File file=new File(url);
        System.out.println("file"+file.getName().substring(0, file.getName().indexOf('.')));
        List<String> commend = new ArrayList<String>();
        commend.add(BASE_PATH);
        commend.add("-i");  
        commend.add(url);  
        commend.add("-c:v");  
        commend.add("libx264");  
        commend.add("-x264opts");  
        commend.add("force-cfr=1");  
        commend.add("-vsync");  
        commend.add("cfr");  
        commend.add("-vf");  
        commend.add("idet,yadif=deint=interlaced");  
        commend.add("-filter_complex");  
        commend.add("aresample=async=1000");  
        commend.add("-c:a");  
        commend.add("libmp3lame");  
        commend.add("-b:a");  
        commend.add("192k");  
        commend.add("-pix_fmt");  
        commend.add("yuv420p");  
        commend.add("-f");  
        commend.add("mpegts"); 
        StringBuilder builder=new StringBuilder();
        builder.append(urlFile);
        builder.append(file.getName().substring(0, file.getName().indexOf('.')));
        builder.append(".");
        builder.append("mp4");
        System.out.println(builder.toString());
        commend.add(builder.toString());
        builder=null;
        try {     
            ProcessBuilder processBuilder = new ProcessBuilder(commend);
            processBuilder.command(commend);
            processBuilder.redirectErrorStream(true);
            
            Process p = processBuilder.start();
           
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while((line =br.readLine()) != null)
            {
                Start.textField4.setText("<转换细节:"+line+">");
            }
            p.waitFor();
            file.delete();
        	count++;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
