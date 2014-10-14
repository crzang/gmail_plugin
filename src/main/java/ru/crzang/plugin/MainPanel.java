package ru.crzang.plugin;

import com.googlecode.gmail4j.GmailClient;
import com.googlecode.gmail4j.GmailConnection;
import com.googlecode.gmail4j.GmailMessage;
import com.googlecode.gmail4j.auth.Credentials;
import com.googlecode.gmail4j.http.HttpGmailConnection;
import com.googlecode.gmail4j.rss.RssGmailClient;
import com.googlecode.gmail4j.util.LoginDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import javax.swing.*;
import java.util.List;

/**
 * @author by crzang.
 */
public class MainPanel implements ToolWindowFactory {
  private JPanel      panel1;
  private JTabbedPane tabbedPane1;
  private JTextArea   textArea;
  private Settings settings;

  public MainPanel() {
    panel1=new JPanel();
    textArea=new JTextArea("Test");
    panel1.add(textArea);
    settings=getSettings();
  }

  private Settings getSettings() {
    return new GmailSettings();
  }

  @Override
  public void createToolWindowContent(Project project, ToolWindow toolWindow) {
    GmailClient client = new RssGmailClient();
    GmailConnection connection = new HttpGmailConnection(new Credentials
        (settings.getLogin(),settings.getPassword().toCharArray()));
    client.setConnection(connection);
    final List<GmailMessage> messages = client.getUnreadMessages();
    for (GmailMessage message : messages) {
      panel1.add(new JLabel(message.getSubject()));
      System.out.println(message);
    }
    ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
    //Создаем контент(окно) с нашим GUI
    Content content = contentFactory.createContent(panel1, "", false);
    //Добавляем в IDE
    toolWindow.getContentManager().addContent(content);
  }
}
