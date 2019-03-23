/**
 *
 *  @author Mirkiewicz Maciej S16426
 *
 */

package zad1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class Service {

    private String country;
    private String country_code;
    private Map<String,Double> items = new HashMap<>(); //code, val -> PLN 2,137

    public Service(String country) {
        this.country = country;
        country_code = getCodeforCountry();
    }

        public String getWeather(String warsaw) {

        StringBuilder s = new StringBuilder();
        s.append("http://api.openweathermap.org/data/2.5/weather?q=");
        s.append(warsaw);
        s.append("&units=metric&appid=33c0c35b63ea67d6f9ee0b16bcfa3d81");
        String weather_url = s.toString();

        String weather = "error get weather try catch";
        try{
            InputStream is = new URL(weather_url).openStream();
            JSONObject jsonObject = readJSON(is);
            String tmp = jsonObject.getJSONObject("main").toString();
            tmp = tmp.replaceAll("\"", "")
                    .replaceAll("[{}]","")
                    .replaceAll("_"," ")
                    .replaceAll(":", ": ");
            String[] main = tmp.split(",");

            weather = "Pogoda dla miasta " + warsaw +": \n" + main[0]
                    + "\n" + main[1] + "\n" + main[2] + "\n" + main[3]
                    + "\n" + main[4];

            }catch (IOException e){
                e.printStackTrace();
            }
        return weather;
    }

    public Double getRateFor(String usd) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://api.exchangeratesapi.io/latest?symbols=");
        stringBuilder.append(usd);
        stringBuilder.append("&base=");
        stringBuilder.append(country_code);
        String urlForRate = stringBuilder.toString();
        double val = -1.0;

        try{
            InputStream is = new URL(urlForRate).openStream();
            JSONObject jsonObject = readJSON(is);
            String tmp = jsonObject.getJSONObject("rates").toString();
            val = Double.parseDouble(tmp.substring(tmp.indexOf(':')+1,tmp.length()-1));

        }catch (IOException e){
            e.printStackTrace();
        }
        return val;
    }


    public Double getNBPRate() {

        putToMap("http://api.nbp.pl/api/exchangerates/tables/a/?format=json");
        putToMap("http://api.nbp.pl/api/exchangerates/tables/b/?format=json");


        if (country_code.equals("PLN")){
            System.err.println("Kurs złotego do złotego to 1 złoty");
            return 1.0;
        }
        else return items.getOrDefault(country_code, -1.0);
    }

    private void putToMap(String url){


        JSONObject jsonObject;
        try {
            InputStream is = new URL(url).openStream();
            jsonObject = readJSON(is);
            JSONArray jsonArray = jsonObject.getJSONArray("rates");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tmp = jsonArray.getJSONObject(i);
                String name= tmp.getString("code");
                double mid = tmp.getDouble("mid");
                items.put(name,mid);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public JSONObject readJSON(InputStream is){

        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

            StringBuilder sb = new StringBuilder();
            int i;
            while ((i = rd.read()) != -1){
                sb.append((char) i);
            }
            String jsonText = sb.toString();

            JSONObject jsonObject = new JSONObject(jsonText.substring(jsonText.indexOf("{")));

            return jsonObject;

        }catch (IOException ex){
            ex.printStackTrace();
        }


        return null;
    }

    private String getCodeforCountry(){

        Map<String,String> hash = new HashMap<>();

        try{
            InputStream is = new FileInputStream(new File("curriencies.json"));
            JSONObject jsonObject = readJSON(is);
            JSONArray jsonArray = jsonObject.getJSONArray("curriencies");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tmp = jsonArray.getJSONObject(i);
                String country = tmp.getString("country");
                String curr;
                if (tmp.isNull("currency_code")){
                    curr = "brak waluty";
                } else {
                    curr = tmp.getString("currency_code");
                }
                hash.put(country,curr);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return hash.get(country);
    }

    public String getCountry_code() {
        return country_code;
    }
}
