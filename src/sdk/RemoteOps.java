/**
 * 
 */
package sdk;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
// TODO: Auto-generated Javadoc

/**
 * The Class RemoteOps, for operations on remote server
 * eq read file, write file, execute command
 *
 * @author shivam
 */
public class RemoteOps {

	/**
	 * Instantiates a new remote ops.
	 * @param str_Host the str_ host
	 * @param str_Username the str_ username
	 * @param str_Password the str_ password
	 * @param int_Port the int_ port
	 * @throws Exception 
	 */
	public RemoteOps(String str_Host, String str_Username, String str_Password, int int_Port) throws Exception
	{
		try{
			obj_Session = obj_JSch.getSession(str_Username, str_Host);
			obj_Session.setPort(int_Port);
			obj_Session.setPassword(str_Password);
			Properties obj_Properties = new Properties();
			obj_Properties.put("StrictHostKeyChecking", "no");
			obj_Session.setConfig(obj_Properties);
			obj_Session.connect();
		}catch(JSchException jex)
		{
			throw new Exception(jex);
		}
	}

	/** The obj_ session. */
	Session obj_Session = null;

	/** The obj_ j sch. */
	JSch obj_JSch = new JSch();

	/**
	 * Connect.
	 *
	 * @param str_Host the str_ host
	 * @param str_Username the str_ username
	 * @param str_Password the str_ password
	 * @param int_Port the int_ port
	 * @return the remote ops
	 * @throws JSchException the j sch exception
	 */
	public RemoteOps connect(String str_Host, String str_Username, String str_Password, int int_Port) throws JSchException
	{
		System.out.println("Connectiong to "+str_Host);
		System.out.println("Creating Session");
		obj_Session = obj_JSch.getSession(str_Username, str_Host);
		obj_Session.setPort(int_Port);
		obj_Session.setPassword(str_Password);
		Properties obj_Properties = new Properties();
		obj_Properties.put("StrictHostKeyChecking", "no");
		obj_Session.setConfig(obj_Properties);
		System.out.println("Attempting connecion");
		obj_Session.connect();
		System.out.println("Connected :)");
		return this;
	}
	/**
	 * Read remote file.<br>
	 *eg> int_SSHPort = 22; //By default SSH port of linux system is 22.<br>
	 *		String str_Directory = "/opt/Avaya/"<br>
	 *		String str_Host = "10.136.16.59";<br>
	 *		String str_Username = "admin";<br>
	 *		String str_Password = "GSC123gsc!";<br>
	 *		String str_File = "installation.properties";<br>
	 *		String strFileContent=ReadFileFromLinux(str_Host, str_Username, str_Password, int_SSHPort, str_Directory, str_File);<br>
	 * @param str_Host the str_ host
	 * @param str_Username the str_ username
	 * @param str_Password the str_ password
	 * @param int_Port the int_ port
	 * @param str_FileDirectory the str_ file directory
	 * @param str_FileName the str_ file name
	 * @return the string that contains the entire file.
	 */
	public String readRemoteFile(String str_FileDirectory, String str_FileName)
	{

		/*JSch obj_JSch = new JSch();
		com.jcraft.jsch.Session obj_Session = null;*/
		StringBuilder obj_StringBuilder = new StringBuilder();
		try
		{
			/*obj_Session = obj_JSch.getSession(str_Username, str_Host);
			obj_Session.setPort(int_Port);
			obj_Session.setPassword(str_Password);
			Properties obj_Properties = new Properties();
			obj_Properties.put("StrictHostKeyChecking", "no");
			obj_Session.setConfig(obj_Properties);
			obj_Session.connect();*/
			Channel obj_Channel = obj_Session.openChannel("sftp");
			obj_Channel.connect();
			ChannelSftp obj_SFTPChannel = (ChannelSftp) obj_Channel;
			obj_SFTPChannel.cd(str_FileDirectory);
			InputStream obj_InputStream = obj_SFTPChannel.get(str_FileName);
			char[] ch_Buffer = new char[0x10000];
			Reader obj_Reader = new InputStreamReader(obj_InputStream, "UTF-8");
			int int_Line = 0;
			do
			{
				int_Line = obj_Reader.read(ch_Buffer, 0, ch_Buffer.length);
				if (int_Line > 0)
				{ obj_StringBuilder.append(ch_Buffer, 0, int_Line);}
			}
			while (int_Line >= 0);
			obj_Reader.close();
			obj_InputStream.close();
			obj_SFTPChannel.exit();
			obj_Channel.disconnect();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return obj_StringBuilder.toString(); 
	}
	/**
	 * returns the rows of log after the given date only of the given level
	 * @param str_FileDirectory
	 * @param str_FileName
	 * @param date
	 * @param logLvl="WARN","ERROR","FINE","FINEST"
	 * @return rows of log in a list
	 * @throws ParseException
	 */
	public List<String> getLogsAfter(String str_FileDirectory, String str_FileName,Date date, String logLvl) throws ParseException
	{
		try{
			List<String> logRows=new ArrayList<String>();
			DateFormat logformat=new SimpleDateFormat("dd MMM yyyy HH:mm:ss",Locale.ENGLISH);
			TimeZone gmtTime = TimeZone.getTimeZone("EDT");
			logformat.setTimeZone(gmtTime);
			DateFormat df = new SimpleDateFormat("dd MMM yyyy");
			TimeZone istTime = TimeZone.getTimeZone("IST");
			logformat.setTimeZone(istTime);
			/////////
			String ddmmyyyy=df.format(date);
			String startOfLine=logLvl+" "+ddmmyyyy;
			////////
			String logContent=readRemoteFile(str_FileDirectory, str_FileName);
			String[] arrLog=logContent.split("\n");
			for(int count=arrLog.length-1;count>=0;count--)
			{
				String line=arrLog[count];
				if(line.startsWith(startOfLine))
				{

					String lD=line.substring(line.indexOf(logLvl)+logLvl.length()+1, line.indexOf(",")).trim();
					Date logDate=logformat.parse(lD);
					Calendar cal = Calendar.getInstance();
					cal.setTime(logDate);
					cal.add(Calendar.MINUTE, 570);
					//int hr=cal.get(Calendar.HOUR_OF_DAY);
					//cal.set(Calendar.HOUR_OF_DAY,hr);
					String newTime = logformat.format(cal.getTime());
					logDate=logformat.parse(newTime);
					if(date.before(logDate))
					{
						logRows.add(line);
					}
					else
					{	
						if(date.after(logDate))
							break;
					}
				}

			}
			return logRows;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public Date getDate() throws ParseException
	{
		String date=executeRemoteCommand("date");
		DateFormat nowformat = new SimpleDateFormat("EEE MMM dd hh:mm:ss Z yyyy", Locale.ENGLISH);
		return nowformat.parse(date);
	}
	/**
	 * Execute remote command.
	 *
	 * @param str_Host the str_ host
	 * @param str_Username the str_ username
	 * @param str_Password the str_ password
	 * @param int_Port the int_ port  is 22 mostly for Linux
	 * @param command the command
	 * @return the string
	 */
	public String executeRemoteCommand(String command)
	{
		/*JSch obj_JSch = new JSch();
		com.jcraft.jsch.Session obj_Session = null;*/
		StringBuilder obj_StringBuilder=new StringBuilder();
		try
		{
			/*obj_Session = obj_JSch.getSession(str_Username, str_Host);
			obj_Session.setPort(int_Port);
			obj_Session.setPassword(str_Password);
			Properties obj_Properties = new Properties();
			obj_Properties.put("StrictHostKeyChecking", "no");
			obj_Session.setConfig(obj_Properties);
			obj_Session.connect();*/
			System.out.println("opening remote channel");
			Channel channel=obj_Session.openChannel("exec");
			System.out.println("Executing command");
			((ChannelExec)channel).setCommand(command);
			InputStream in=channel.getInputStream();
			channel.connect();
			System.out.println("reading the output");
			char[] ch_Buffer = new char[0x10000];
			Reader obj_Reader = new InputStreamReader(in, "UTF-8");
			int int_Line = 0;
			do
			{
				int_Line = obj_Reader.read(ch_Buffer, 0, ch_Buffer.length);
				if (int_Line > 0)
				{ obj_StringBuilder.append(ch_Buffer, 0, int_Line);}
			}
			while (int_Line >= 0);
			System.out.println("output read");
			obj_Reader.close();
			in.close();
			channel.disconnect();
			System.out.println("Disconnecing the channel");
		}
		catch(Exception e)
		{
			System.out.println("An exception occured while executng the command ");
			e.printStackTrace();
		}
		return obj_StringBuilder.toString();
	}

	/**
	 * Write remote file.
	 *
	 * @param str_Host the str_ host
	 * @param str_Username the str_ username
	 * @param str_Password the str_ password
	 * @param int_Port the int_ port is 22 mostly for Linux
	 * @param str_Content the str_ content
	 * @param str_FileDirectory the str_ file directory
	 * @param str_FileName the str_ file name
	 */
	public void writeRemoteFile(String str_Content, String str_FileDirectory, String str_FileName)
	{
		/*JSch obj_JSch = new JSch();
		com.jcraft.jsch.Session obj_Session = null;*/
		try
		{
			/*obj_Session = obj_JSch.getSession(str_Username, str_Host);
			obj_Session.setPort(int_Port);
			obj_Session.setPassword(str_Password);
			Properties obj_Properties = new Properties();
			obj_Properties.put("StrictHostKeyChecking", "no");
			obj_Session.setConfig(obj_Properties);
			obj_Session.connect();*/
			Channel obj_Channel = obj_Session.openChannel("sftp");
			obj_Channel.connect();
			ChannelSftp obj_SFTPChannel = (ChannelSftp) obj_Channel;
			obj_SFTPChannel.cd(str_FileDirectory);
			InputStream obj_InputStream = new ByteArrayInputStream(str_Content.getBytes());
			obj_SFTPChannel.put(obj_InputStream, str_FileDirectory + str_FileName );
			obj_SFTPChannel.exit();
			obj_InputStream.close();
			obj_Channel.disconnect();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public void disconnect()
	{
		System.out.println("Disconnecting remote server");
		obj_Session.disconnect();
		System.out.println("remote server Disconnected");
	}
}
