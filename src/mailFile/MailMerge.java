package mailFile;

import java.io.File;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;    //using javamail-1.4.7
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.auxilii.msgparser.MsgParser;    //using msgparser-1.15


public class MailMerge {
	//------------------Mail Part (When "submit" button in MainAttach part is clicked, this part will be executed.)
	public MailMerge(String sender, String receiver, String title, String body) throws Exception
	{
		if(sender.equals(""))
		{
			sender = "senderMail";
		}
		if(receiver.equals(""))
		{
			receiver = "receiverMail";
		}
		if(title.equals(""))
		{
			title = "Title";
		}
		if(body.equals(""))
		{
			body = "此次信件:";
		}
		
		String fileName = "newMail.eml";    //output file name
		
		System.setProperty("mail.mime.charset", "utf-8");
		
		
		Session ses = Session.getDefaultInstance(new Properties());
		Message msg = new MimeMessage(ses);
		
		msg.setFrom(new InternetAddress(sender));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
		msg.setSubject(title);
		msg.setSentDate(new Date());
		
		MimeBodyPart bp = new MimeBodyPart();
		BodyPart mainBp = new MimeBodyPart();
		MimeMultipart mlt = new MimeMultipart();
		
		String bodyRows[] = body.split("\n");    //Get from "body" which is decided in MainAttach part.
		
		mainBp.setContent("<h2>" + bodyRows[0] + "</h2>", "text/html; charset=UTF-8");
		
		for(int i=1; i<bodyRows.length; i++)
		{
			mainBp.setContent(mainBp.getContent() + "<h2>" + bodyRows[i] + "</h2>", "text/html; charset=UTF-8");
		}
		
		File classFolder = new File(MainAttach.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		System.out.println(classFolder.getAbsolutePath());    //Find the folder path of this class.
		File currentFolder = new File(classFolder.getAbsolutePath().substring(0, classFolder.getAbsolutePath().lastIndexOf("\\MailMerge.jar")));
		System.out.println(currentFolder);    //By "classFolder", find out the folder of mail files
		File allFiles[] = currentFolder.listFiles();
		
		for(int i=0; i<allFiles.length; i++)
		{
			if(allFiles[i].getName().substring(allFiles[i].getName().lastIndexOf(".") + 1, allFiles[i].getName().length()).equals("msg"))
			{
				System.out.println(allFiles[i].getName());
				
				MsgParser msgp = new MsgParser();
				com.auxilii.msgparser.Message m = msgp.parseMsg(allFiles[i].getName());
				
				bp = new MimeBodyPart();
				DataSource dtsrc = new FileDataSource(allFiles[i].getName());	//2
				bp.setDataHandler(new DataHandler(dtsrc));
				bp.setHeader("Content-Disposition", "attachment");
				bp.setFileName(MimeUtility.encodeText(allFiles[i].getName()).replaceAll("\r", "").replaceAll("\n", ""));
				
				mainBp.setContent(mainBp.getContent().toString() + 
						"<h2><u>" + m.getSubject() + "</u></h2>", "text/html; charset=UTF-8");
				
				//bp.setFileName(MimeUtility.encodeText(allFiles[i].getName()).replaceAll("\r", "").replaceAll("\n", ""));
				mlt.addBodyPart(bp);
			}
		}
		
		mlt.addBodyPart(mainBp);
		
		msg.setContent(mlt);
		msg.saveChanges();
		msg.writeTo(new FileOutputStream(fileName));
		
		System.out.println("Created.");
		
		for(int i=0; i<allFiles.length; i++)
		{
			allFiles[i].delete();
		}
	}
}
