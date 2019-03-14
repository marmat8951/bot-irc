package bot.irc.main;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class Config {
	
	
	  private static Properties props;
	  public final static String DEFAULT_PROPERTIES_FILE = "default.properties";
	  public final static String PROPERTIES_FILE = "config.properties";
	  public final static String here = (new File(".")).getAbsolutePath();
	  
	  static {
	    try {
	    	FileReader defaultProperties = new FileReader(new File(here+File.separator+"ressources"+File.separator+"config"+File.separator+DEFAULT_PROPERTIES_FILE));
			Properties defaultProp = new Properties();
			defaultProp.load(defaultProperties);
			props=new Properties(defaultProp);
	        FileReader in = new FileReader(new File(here+File.separator+"ressources"+File.separator+"config"+File.separator+PROPERTIES_FILE));
	        props.load(in);
	        in.close();
	        System.out.println("Config ready");
	        for(String s:defaultProp.stringPropertyNames()) System.out.println(s+"="+getProperty(s));
	       
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	  }
	  
	  public static String getProperty(String key) {
		  return props.getProperty(key);
	  }
	  
	  public static String getProperty(String key,String defaultValue) {
		  return props.getProperty(key, defaultValue);
	  }
	  
	  
	  public static int getPropertyAsInt(String key) {
			  return Integer.parseInt(props.getProperty(key));
	  }
	  
	  public static int getPropertyAsInt(String key, int defaultValue) {
		  try {
			  return Integer.parseInt(props.getProperty(key));
		  }catch(Exception e) {
			  return defaultValue;
		  }
	  }
	  
	  public static int getPropertyAsUnsignedInt(String key) {
		  return Integer.parseUnsignedInt(props.getProperty(key));
	  }
	  
	  
	  
		public static String[] getMultipleValues(String key) {
			return props.getProperty(key).split(",");
		}
		
		
		public static long getPropertyAsLong(String key) {
			return Long.parseLong(props.getProperty(key));
		}
		
		public static long getPropertyAsLong(String key,long defaultValue) {
			try {
				return Long.parseLong(props.getProperty(key));
			}catch(Exception e) {
				return defaultValue;
			}
		}
		
		public static boolean getPropertyAsBoolean(String key) {
			return Boolean.parseBoolean(props.getProperty(key));
		}
		
		public static boolean getPropertyAsBoolean(String key,boolean defaultValue) {
			if(props.containsKey(key)) {
				return getPropertyAsBoolean(key);
			}else {
				return defaultValue;
			}
			
		}
		
		
}