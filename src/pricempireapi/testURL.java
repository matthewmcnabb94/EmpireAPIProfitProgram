/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pricempireapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

/**
 *
 * @author matth
 */
public class testURL {
    
    
    
    
    
    private static HttpURLConnection connection;
    private static  BufferedReader reader;
    private static String line;
    private static StringBuffer responseContent = new StringBuffer();
    
    
    
    
    
    
    
    
    
    public static void main(String[] args)
    {
        String volume = getDailySales();
        System.out.println("Volume is: "+volume);
    }
    
    private static String getDailySales()
    {
        
        String volume = null;
        try
        {
            
        
        URL url = new URL("http://api.scraperapi.com/?api_key=67c389629947dded3a2187e18ffdf26c&url=https://steamcommunity.com/market/priceoverview/?appid=730&market_hash_name=%E2%98%85%20Specialist%20Gloves%20%7C%20Fade%20(Battle-Scarred)");
        System.out.println("URL for volume is: "+url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36");
        conn.connect();
        int responsecode = conn.getResponseCode();

        if (responsecode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responsecode);
        } else {

      
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();

            System.out.println("Success");
            
            JSONObject jobj = new JSONObject(responseContent.toString());
            volume = jobj.getString("volume");
            

            
            }
        
        }catch(Exception e)
        {
            System.out.println("Exception: "+e.getMessage());
        }
        
        
        
        return volume;
    }
    
}
