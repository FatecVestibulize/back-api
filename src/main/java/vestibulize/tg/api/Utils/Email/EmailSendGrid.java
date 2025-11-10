package vestibulize.tg.api.Utils.Email;

import com.sendgrid.SendGrid;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.Method;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Content;
import java.io.IOException;

public class EmailSendGrid {

  public Email from = new Email("vestibulize@gmail.com");
  public Email to;
  public String title;

  public EmailSendGrid(String to, String title) {
    this.title = title;
    this.to = new Email(to);
  }

  public void sendChangePasswordRequest(String token) throws IOException {
    
    String body = "Clique no botão abaixo para redefinir sua senha: <br>" +
    "<a style=\"cursor: pointer;\" href=\"" + System.getProperty("FRONT_WEB_URL", System.getenv("FRONT_WEB_URL")) + "/alterar-senha?token=" + token + "\" >" +
      "<button style=\"background-color: #4A4C78; color: white; border: none; border-radius: 8px; padding: 10px; text-decoration: none; width: 100%; height: 50px; margin: 10px 0;\">" +
          "Redefinir Senha "+
      "</button>" +
    "</a>" +
    "<br> <br>" +
    "<p>Se você não solicitou a redefinição de senha, ignore este email.</p>";

    this.send(body);

  }

  private Content createEmailContentTemplate(String body) {

      return new Content("text/html", 
        "<!DOCTYPE html>" +
           "<html lang=\"pt-BR\">" +
           "<head>" +
           "  <meta charset=\"UTF-8\">" +
           "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
           "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">" +
           "  <style type=\"text/css\">" +
           "    * { margin: 0; padding: 0; box-sizing: border-box; }" +
           "    body { font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; line-height: 1.6; color: #1f2937; background-color: #E6E9F0; }" +
           "    .email-wrapper { width: 100%; background-color: #E6E9F0; padding: 20px 0; }" +
           "    .email-container { max-width: 600px; margin: 0 auto; background-color: #E6E9F0; }" +
           "    .email-content { background-color: #ffffff; border-radius: 8px; padding: 40px 30px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); }" +
           "    .header { background: linear-gradient(143.35deg, #3D3A66 68.63%, #4A4C78 166.73%); border-radius: 8px; padding: 30px; text-align: center; margin: -40px -30px 30px -30px; }" +
           "    .logo-text { color: #ffffff; font-size: 28px; font-weight: bold; text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2); letter-spacing: 1px; }" +
           "    .body-content { color: #1f2937; font-size: 16px; line-height: 1.8; margin: 20px auto; }" +
           "    .footer { margin-top: 30px; padding-top: 20px; border-top: 2px solid #E6E9F0; text-align: center; color: #6b7280; font-size: 14px; }" +
           "    .link { color: #4A4C78; text-decoration: none; font-weight: 600; }" +
           "    .link:hover { color: #F58220; }" +
           "    @media only screen and (max-width: 600px) { " +
           "      .email-wrapper { padding: 10px 0; }" +
           "      .email-content { padding: 30px 20px; border-radius: 0; }" +
           "      .header { padding: 20px; margin: -30px -20px 20px -20px; border-radius: 0; }" +
           "      .logo-text { font-size: 24px; }" +
           "      .body-content { font-size: 14px; }" +
           "    }" +
           "  </style>" +
           "</head>" +
           "<body>" +
           "  <div class=\"email-wrapper\">" +
           "    <div class=\"email-container\">" +
           "      <div class=\"email-content\">" +
           "        <div class=\"header\">" +
           "          <div class=\"logo-text\">" + this.title + "</div>" +
           "        </div>" +
           "        <div class=\"body-content\">"
                      + body +
           "        </div>" +
           "        <div class=\"footer\">" +
           "          <p>Este email foi enviado pelo sistema Vestibulize.</p>" +
           "        </div>" +
           "      </div>" +
           "    </div>" +
           "  </div>" +
           "</body>" +
           "</html>"
        );

  }

  public void send(String body) throws IOException {

    try {

      Mail mail = new Mail(this.from, this.title, this.to, this.createEmailContentTemplate(body));
    
      String apiKey = System.getProperty("SENDGRID_API_KEY", System.getenv("SENDGRID_API_KEY"));
      if (apiKey == null || apiKey.isEmpty()) {
        throw new IOException("SENDGRID_API_KEY não encontrada. Configure no arquivo .env ou como variável de ambiente.");
      }

      SendGrid sg = new SendGrid(apiKey);
      Request request = new Request();
      
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sg.api(request);
      System.out.println(response.getStatusCode());
      System.out.println(response.getBody());
      System.out.println(response.getHeaders());
    
    } catch (IOException ex) {
    
      throw ex;
    
    }

  }
}