
package project_restapi;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import project_core.Account;
import project_core.Message;
import project_core.json.CompleteObjectMapper;
import project_restapi.AccountService;
import project_restserver.GrizzlyApp;

public class AccountServiceTest extends TestCase {

  private HttpServer server;

  /**
   * Should start the server before each test. 
   * (this currently doesn't work as intended though so it is started manually in each test)
   */
  @Before
  public void setup() {
    server = GrizzlyApp.startServer(5);
  }

  @Test
  public void testCreateAccount() {
    server = GrizzlyApp.startServer(5);
    try {
      URI clientUri =
          new URI(GrizzlyApp.BASE_URI + AccountService.ACCOUNT_SERVICE_PATH + "/createAccount");
      System.out.println(clientUri);
      Account account = new Account("1231", "123");
      final HttpRequest request = HttpRequest.newBuilder(clientUri)
          .header("Content-Type", "application/json").header("Accept", "application/json")
          .POST(BodyPublishers.ofString(new CompleteObjectMapper().writeValueAsString(account)))
          .build();
      final HttpResponse<InputStream> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofInputStream());
      assertEquals(200, response.statusCode());
      assertEquals(false,
          (boolean) new CompleteObjectMapper().readValue(response.body(), Boolean.class));
    } catch (IOException | InterruptedException | URISyntaxException e) {
      fail(e.getClass() + ":" + e.getMessage());
    }

    GrizzlyApp.stopServer(server);
  }

  @Test
  public void testAccountIsValid() {
    server = GrizzlyApp.startServer(5);
    try {
      URI clientUri =
          new URI(GrizzlyApp.BASE_URI + AccountService.ACCOUNT_SERVICE_PATH + "/isValid");
      System.out.println(clientUri);
      Account account = new Account("1231", "123");
      final HttpRequest request = HttpRequest.newBuilder(clientUri)
          .header("Content-Type", "application/json").header("Accept", "application/json")
          .POST(BodyPublishers.ofString(new CompleteObjectMapper().writeValueAsString(account)))
          .build();
      final HttpResponse<InputStream> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofInputStream());
      assertEquals(200, response.statusCode());
      assertEquals(true,
          (boolean) new CompleteObjectMapper().readValue(response.body(), Boolean.class));
    } catch (IOException | InterruptedException | URISyntaxException e) {
      fail(e.getClass() + ":" + e.getMessage());
    }

    GrizzlyApp.stopServer(server);
  }

  @Test
  public void testUploadMessage() {
    server = GrizzlyApp.startServer(5);
    try {
      Account account = new Account("to@example.no", "123");
      Account from = new Account("from@example.no");
      Message message = new Message("testSubject", "this is a message", account, from);

      URI clientUri = new URI(GrizzlyApp.BASE_URI + AccountService.ACCOUNT_SERVICE_PATH + "/"
          + account.getMail_address() + "/inbox/");
      System.out.println(clientUri);
      final HttpRequest request = HttpRequest.newBuilder(clientUri)
          .header("Content-Type", "application/json").header("Accept", "application/json")
          .POST(BodyPublishers.ofString(new CompleteObjectMapper().writeValueAsString(message)))
          .build();
      final HttpResponse<InputStream> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofInputStream());
      assertEquals(200, response.statusCode());
      assertEquals(true,
          (boolean) new CompleteObjectMapper().readValue(response.body(), Boolean.class));
    } catch (IOException | InterruptedException | URISyntaxException e) {
      fail(e.getClass() + ":" + e.getMessage());
    }

    GrizzlyApp.stopServer(server);

  }

  @Test
  public void testUploadMessages() {
    server = GrizzlyApp.startServer(5);

    // All of this under is just testing List<Message> serializing/deserializing.
    Account account = new Account("to@example.no", "123");
    Account from = new Account("from@example.no");
    Message message = new Message("testSubject", "this is a message", account, from);

    List<Message> messages = new ArrayList<Message>();
    messages.add(message);
    messages.add(message);
    messages.add(new Message("hallo", "mer hallo", from, account));

    try {
      String jsonString = new CompleteObjectMapper().writeValueAsString(messages);
      System.out.println(jsonString);
      @SuppressWarnings("unchecked")
      List<Message> deserializedMessages = (List<Message>) new CompleteObjectMapper()
          .readValue(jsonString, new TypeReference<List<Message>>() {
            /* Dette er en tom implementasjon av compareTo i TypeReference, som man bare m� ha*/ });
      for (Message mes : deserializedMessages) {
        System.out.println(mes);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    // All of this over was just testing List<Message> serializing/deserializing.

    try {
      URI clientUri = new URI(GrizzlyApp.BASE_URI + AccountService.ACCOUNT_SERVICE_PATH + "/"
          + account.getMail_address() + "/inbox/");
      System.out.println(clientUri);
      final HttpRequest request = HttpRequest.newBuilder(clientUri)
          .header("Content-Type", "application/json").header("Accept", "application/json")
          .PUT(BodyPublishers.ofString(new CompleteObjectMapper().writeValueAsString(messages)))
          .build();
      final HttpResponse<InputStream> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofInputStream());
      assertEquals(200, response.statusCode());
      assertEquals(true,
          (boolean) new CompleteObjectMapper().readValue(response.body(), Boolean.class));
    } catch (IOException | InterruptedException | URISyntaxException e) {
      fail(e.getClass() + ":" + e.getMessage());
    }
    GrizzlyApp.stopServer(server);

  }

  @Test
  public void testGetMessages() {
    server = GrizzlyApp.startServer(5);
    try {
      Account account = new Account("to@example.no", "123");

      URI clientUri = new URI(GrizzlyApp.BASE_URI + AccountService.ACCOUNT_SERVICE_PATH + "/"
          + account.getMail_address() + "/inbox/");
      System.out.println(clientUri);
      final HttpRequest request =
          HttpRequest.newBuilder(clientUri).header("Accept", "application/json").GET().build();
      final HttpResponse<InputStream> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofInputStream());
      assertEquals(200, response.statusCode());
      @SuppressWarnings("unchecked")
      List<Message> messages = (List<Message>) new CompleteObjectMapper().readValue(response.body(),
          new TypeReference<List<Message>>() {
            /* Dette er en tom implementasjon av compareTo i TyepReference, som man bare m� ha*/ });
      assertEquals(true, messages.size() >= 3);
      System.out.println(messages);
    } catch (IOException | InterruptedException | URISyntaxException e) {
      fail(e.getClass() + ":" + e.getMessage());
    }

    GrizzlyApp.stopServer(server);

  }

  /**
   * Should stop the current server after each test.
   * (this currently doesn't work as intended though so it is stopped manually in each test)
   */
  @After
  public void tearDown() {
    GrizzlyApp.stopServer(server);
  }

}
