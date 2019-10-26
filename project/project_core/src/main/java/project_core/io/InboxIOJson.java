
package project_core.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import project_core.Inbox;
import project_core.Message;
import project_core.json.MessageDeserializer;
import project_core.json.MessageSerializer;

public class InboxIOJson implements MailReader {

  public static final String resourceFilepath =
      new File("").getAbsolutePath() + "\\src\\main\\resources\\io\\inbox\\";

  @Override
  public void uploadMessage(Message message, String filename) throws IOException {
    String filepath = resourceFilepath + filename;
    FileWriter fr = new FileWriter(new File(filepath), true);

    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(Message.class, new MessageSerializer());
    mapper.registerModule(module);

    PrintWriter writer = new PrintWriter(fr);
    String text = mapper.writeValueAsString(message);
    System.out.println(text);
    writer.println(text);

    writer.close();
  }

  @Override
  public void uploadInbox(Inbox inbox, String filename) throws IOException {
    this.clearFile(filename);
    for (Message message : inbox.getMessages()) {
      this.uploadMessage(message, filename);
    }
  }

  @Override
  public List<Message> getMessages(String filename) throws IOException {
    String filepath = resourceFilepath + filename;
    Scanner scanner = new Scanner(new File(filepath));

    List<Message> messages = new ArrayList<Message>();

    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(Message.class, new MessageDeserializer());
    mapper.registerModule(module);

    while (scanner.hasNextLine()) {
      messages.add(mapper.readValue(scanner.nextLine(), Message.class));
    }

    scanner.close();
    return messages;
  }

  private void clearFile(String filename) throws IOException {
    String filepath = resourceFilepath + filename;
    PrintWriter writer = new PrintWriter(new File(filepath));
    writer.print("");
    writer.close();
  }
  
}
