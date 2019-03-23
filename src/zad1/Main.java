/**
 *
 *  @author Mirkiewicz Maciej S16426
 *
 */

package zad1;


import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;

public class Main {

  String country, city, currency;

  public static void main(String[] args) {

      Service s = new Service("Germany");
      String weatherJson = s.getWeather("Warsaw");

      Double rate1 = s.getRateFor("USD");
      Double rate2 = s.getNBPRate();


      System.out.println(rate2);
      System.out.println(rate1);
      System.out.println(weatherJson);

    new Main();
  }
  public Main(){
      SwingUtilities.invokeLater(() -> createGUI());
  }
  public void createGUI(){



      JFrame jf = new JFrame();
      jf.setSize(new Dimension(1200, 1000));
      //jf.setLayout(new GridLayout(3,1));//pogoda, kurs, wiki
      jf.setLayout(new BorderLayout());
      jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      jf.setTitle("Projekt 2 - WEBCLIENTS - s16426");


      JPanel jPanel = new JPanel();
      jPanel.setLayout(new GridLayout(1,3));

      JTextArea jtext_country = new JTextArea();
      jtext_country.setBorder(BorderFactory.createRaisedBevelBorder());
      jtext_country.setToolTipText("Enter country name");

      JTextArea jtext_city = new JTextArea();
      jtext_city.setBorder(BorderFactory.createRaisedBevelBorder());
      jtext_city.setToolTipText("Enter city name");

      JTextArea jtext_currency = new JTextArea();
      jtext_currency.setBorder(BorderFactory.createRaisedBevelBorder());
      jtext_currency.setToolTipText("Enter currency");

      JButton jButton = new JButton("OK");
      JTextArea money_results = new JTextArea();


      JFXPanel jfxPanel = new JFXPanel();

      jButton.addActionListener(e -> {

          country = jtext_country.getText();
          city = jtext_city.getText();
          currency = jtext_currency.getText();
          Service s = new Service(country);
          money_results.setText("");
          money_results.append(
                  "Aktualny kurs złotego dla kraju " + country + " wynosi: " + s.getNBPRate()+"\n"
          );
          money_results.append(
                  "Kurs wymiany " + currency + " - " + s.getCountry_code() + " wynosi: " + s.getRateFor(currency)
          );
          money_results.append("\n"+s.getWeather(city));
          openWiki(jfxPanel, "https://en.wikipedia.org/wiki/"+city);
          jf.repaint();
      });



      jPanel.add(jtext_country);
      jPanel.add(jtext_city);
      jPanel.add(jtext_currency);
      jPanel.add(jButton);
      jf.add(jPanel,BorderLayout.NORTH);
      jf.add(money_results, BorderLayout.WEST);
      jf.add(jfxPanel, BorderLayout.CENTER);

      jf.setVisible(true);

  }

  private void openWiki(JFXPanel jfxPanel, String url){
      Platform.runLater(() -> {
          WebView webView = new WebView();
          jfxPanel.setScene(new Scene(webView));
          webView.getEngine().load(url);

      });
  }
}
