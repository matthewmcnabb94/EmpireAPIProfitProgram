/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pricempireapi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author matth
 */
public class TestCSGOTM {
    
    private static  BufferedReader reader, readerS, readerV;
    private static String line, lineS, lineV;
    private static StringBuffer responseContent = new StringBuffer();
    private static StringBuffer responseContentS = new StringBuffer();
    private static StringBuffer responseContentV = new StringBuffer();
    private static PrintWriter pw = null;
    private static PrintWriter pwS = null;
    private static PrintWriter printProfit = null;
    
    private final static String apiKey = "2c17a87b-372f-483a-85ad-6269d4b5eaa5";
    
    private static double  price, fPrice, steamBuyOrderPrice;
    
    private static CSGOTMPOJO csTM = null;
    static ArrayList<CSGOTMPOJO> data = new ArrayList<CSGOTMPOJO>();
    
     private static STEAMPOJO steam = null;
    static ArrayList<STEAMPOJO> dataS = new ArrayList<STEAMPOJO>();
    
    private static HttpURLConnection connectionV;
    
    


    
    
    public static void main(String[] args)
    {
        getCSGOTMItems();
    }
    
    
    
    
    public static void getCSGOTMItems() {
        try {

            HashMap<String,Double> csgoTMItems = new HashMap<String,Double>();
            String marketHashName = null;
            Double buyOrderPrice = null;
            
            URL url = new URL("https://market.csgo.com/api/v2/prices/class_instance/USD.json");
            System.out.println("API URL: "+url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36");
            conn.connect();
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                
                PrintWriter pwS = null;
                        
                try {
                    pw = new PrintWriter(new FileWriter("F:\\PricEmpireAPI\\CSGOTM.csv"), true);
                } catch (FileNotFoundException e) {
                    System.out.println("File not found" + e.getMessage());;
                }
                
                
                try {
                    pwS = new PrintWriter(new FileWriter("F:\\PricEmpireAPI\\STEAM.csv"), true);
                } catch (FileNotFoundException e) {
                    System.out.println("File not found" + e.getMessage());;
                }

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
                
                
                JSONObject obj = new JSONObject(responseContent.toString());
                String currency = obj.getString("currency");
                System.out.println("Currency is: "+currency);
                JSONObject items = obj.getJSONObject("items");
                System.out.println("Item size: "+items.length());
                
                
                

                Iterator<?> keys = items.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    if (items.get(key) instanceof JSONObject) {
                        JSONObject xx = new JSONObject(items.get(key).toString());
                        marketHashName = xx.getString("market_hash_name");
                        buyOrderPrice = xx.getDouble("buy_order");
                        
                        
                        
                        if (buyOrderPrice != null) {

                            if (marketHashName.contains("Music Kit |") || marketHashName.contains("Sticker |") || marketHashName.contains("Warhammer") || marketHashName.contains("Souvenir Sawe") || marketHashName.contains("Seal") || marketHashName.contains("Stainless (Field-Tes")
                                    || marketHashName.contains("â˜… B") || marketHashName.equals("â˜… B") || marketHashName.contains("Patch |") || marketHashName.contains("Souvenir MP5-SD | Lab")) {
                                //System.out.println("Skipping skin as it contains music kit");
                            } else {
                                if (!csgoTMItems.containsKey(marketHashName)) {
                                    csgoTMItems.put(marketHashName, buyOrderPrice);
                                }

                            }

                        }

                    }
                }

                printMap(csgoTMItems);
                
                
                
//                System.out.println("Size of map: "+csgoTMItems.size());
               //System.out.println(csgoTMItems);
//                
//                for (String name : csgoTMItems.keySet()) {
//                    String skin = name.toString();
//                    String value = csgoTMItems.get(name).toString();
//                    System.out.println(skin+ " " + value);
//                    if(value.equals(""))
//                    {
//                        System.out.println("Value is blank.....");
//                    }
//                    else
//                    {
//                        pw.write(skin + "," + value + "\n");
//                    }
//                    
//                    
//                    
//                }
                
                System.out.println("Done");
                
                getSteamItems();
                
                
                //System.out.println(csgoTMItems);
                
            }
           
            
        }catch(Exception e)
        {
            System.out.println("Exception: "+e.getMessage());
        }

    }
    
    
    
    
    public static void getSteamItems() {
        try {

            String marketHashName = null;
            Double buyOrderPrice = null;

            URL urlS = new URL("https://pricempire.com/api/v1/getAllItems?token=" + apiKey);
            System.out.println("API URL: " + urlS);
            HttpURLConnection connS = (HttpURLConnection) urlS.openConnection();
            connS.setRequestMethod("GET");
            connS.setRequestProperty("Content-Type", "application/json");
            connS.setRequestProperty("Accept-Charset", "UTF-8");
            connS.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36");
            connS.connect();
            int responsecode = connS.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                PrintWriter pwS = null;

                try {
                    pwS = new PrintWriter(new FileWriter("F:\\PricEmpireAPI\\STEAM.csv"), true);
                } catch (FileNotFoundException e) {
                    System.out.println("File not found" + e.getMessage());;
                }

                readerS = new BufferedReader(new InputStreamReader(connS.getInputStream()));
                while ((lineS = readerS.readLine()) != null) {
                    responseContentS.append(lineS);
                }
                readerS.close();

                //System.out.println(responseContent.toString());
                JSONObject jobj = new JSONObject(responseContentS.toString());
                JSONArray jsonarr1 = (JSONArray) jobj.get("items");
                //System.out.println(jsonarr1);

                System.out.println("Size of array: " + jsonarr1.length());

                String columnNamesList = "Steam Name, BuyOrderPrice" + "\n";

                pwS.write(columnNamesList);

                for (int i = 0; i < jsonarr1.length(); i++) {

                    JSONObject items = (JSONObject) jsonarr1.get(i);

                    String skinName = items.getString("name");
                    
                    //System.out.println("Skin Name: "+skinName);

                    JSONObject prices = items.getJSONObject("prices");

                    //System.out.println("Prices are: "+prices);
                    if (prices.toString().equals("{}")) {
                        System.out.println("Prices empty");
                    }

                    if (prices.has("csgoempire") && prices.has("steam_buyorder")) {

                        // It exists, do your stuff
                        //System.out.println(":) exists");
                        JSONObject CSGOEmpire = prices.getJSONObject("csgoempire");

                        JSONObject steam = prices.getJSONObject("steam_buyorder");

                        if (CSGOEmpire.getInt("sourcePrice") > 0 && !skinName.contains("Sticker") && !skinName.contains("Operation")) {

                            steamBuyOrderPrice = steam.getInt("sourcePrice");

                            price = CSGOEmpire.getInt("sourcePrice");

                            Rates rate = new Rates();

                            double steamPrice = steamBuyOrderPrice / 100 * rate.getPaxful60();

                            StringBuilder builder = new StringBuilder();

                            builder.append(skinName + ",");
                            builder.append(steamPrice + ",");
                            builder.append('\n');
                            pwS.write(builder.toString());
                            pwS.flush();

                        } else {
                            //System.out.println("Doesnt exist :(");
                        }

                    }

                }

            }
            
            loadCSGOTMPOJO();

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage() + "-" + e.getStackTrace().toString());
        }

    }
    
    
    private static void loadCSGOTMPOJO() throws IOException
    {
        String aFileToReaddp = "F:\\PricEmpireAPI\\CSGOTM.csv";
        String linerp = null;
        
        try{
        FileReader frrp = new FileReader(aFileToReaddp);
        BufferedReader theBufReaderrp = new BufferedReader(frrp);

        linerp = theBufReaderrp.readLine();
        while ((linerp = theBufReaderrp.readLine()) != null) {

            String[] arr = linerp.split(",");
            csTM = new CSGOTMPOJO();
            csTM.setName(arr[0]);
            csTM.setBuyOrderPrice(arr[1]);
            data.add(csTM);
        }
        }catch(Exception e)
        {
            System.out.println("Exception: "+e.getMessage());
        }
        
        
        System.out.println("Size of CSGOTM aList is: "+data.size());
        
        loadSTEAMPOJO();
        
    }
    
    private static void loadSTEAMPOJO() throws IOException
    {
        String aFileToReaddp = "F:\\PricEmpireAPI\\STEAM.csv";
        String linerp = null;
        
        try{
        FileReader frrp = new FileReader(aFileToReaddp);
        BufferedReader theBufReaderrp = new BufferedReader(frrp);

        linerp = theBufReaderrp.readLine();
        while ((linerp = theBufReaderrp.readLine()) != null) {

            String[] arr = linerp.split(",");
            steam = new STEAMPOJO();
            steam.setName(arr[0]);
            steam.setBuyOrderPrice(arr[1]);
            dataS.add(steam);
        }
        }catch(Exception e)
        {
            System.out.println("Exception: "+e.getMessage());
        }
        
        
        System.out.println("Size of Steam aList is: "+dataS.size());
        
        compareLists();
        
    }
    
    
    private static void compareLists() throws IOException
    {
        
        
        

        try {
            printProfit = new PrintWriter(new FileWriter("F:\\PricEmpireAPI\\csgoTmProfitSheet.csv"), true);
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e.getMessage());;
        }
        
        String columnNamesList = "Name,CSGOTM Buy Order Price $,Steam Buy Order Price $,profit margin,Total sales today" + "\n";
        printProfit.write(columnNamesList);
        
        
        
        
        
        
        
        int counter = 0;
        
        for(int x = 0; x < data.size(); x++)
        {
            for(int i = 0; i < dataS.size(); i++)
            {
                
                if (data.get(x).getName().equals(dataS.get(i).getName())) {
                    counter++;
                    //System.out.println("Skin Name: "+data.get(x).getName() + "csgoTM buyOrderPrice: "+data.get(x).getBuyOrderPrice() + " steamBuyOrderPrice: "+dataS.get(i).getBuyOrderPrice());
                    String skinName = data.get(x).getName();
                    double csgoTMBO = Double.parseDouble(data.get(x).getBuyOrderPrice());
                    double csgoTMAfterTax = csgoTMBO * 0.90;
                    double steamBO = Double.parseDouble(dataS.get(i).getBuyOrderPrice());

                    double profitmargin = (csgoTMAfterTax - steamBO) / steamBO * 100;

                    if (profitmargin > 20 && csgoTMBO > 8) {
                        String volume = "";
                        String dailySales = getDailySales(skinName);
                        System.out.println(dailySales);
                        if (dailySales == null) {
                            volume = "0";
                        } else {
                            volume = dailySales;
                        }
                        
                        int sales = Integer.parseInt(volume);
                        
                        if (sales > 20) {
                            StringBuilder builder = new StringBuilder();

                            builder.append(skinName + ",");
                            builder.append(csgoTMAfterTax + ",");
                            builder.append(steamBO + ",");
                            builder.append(profitmargin + ",");
                            builder.append(volume + ",");
                            builder.append('\n');
                            printProfit.write(builder.toString());
                            printProfit.flush();
                        }
                        else
                        {
                            System.out.println("Sales not high enough");
                        }
                        
                        
                        
                        
                        System.out.println("Skin Name: " + skinName + " --------- Profit Margin = " + profitmargin + "Daily sales: "+volume);
                    }

                }
                
                
                
                
            }
        }
        
        System.out.println(counter+" matches");
        
        
    }

    
    public static void printMap(HashMap mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            pw.write(pair.getKey() + "," + pair.getValue() +"\n");
            pw.flush();
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
    
    
    
    private static String getDailySales(String skinName)
    {

        responseContentV.setLength(0);
        
        String volume = null;
        try
        {
            
        System.out.println("Trying with proxy....");
        
        
        
        URL urlV = new URL("http://api.scraperapi.com/?api_key=67c389629947dded3a2187e18ffdf26c&url=https://steamcommunity.com/market/priceoverview/?appid=730&market_hash_name=" + URLEncoder.encode(skinName, StandardCharsets.UTF_8));
        System.out.println("URL for volume is: "+urlV);
        connectionV = (HttpURLConnection) urlV.openConnection();
        connectionV.setRequestMethod("GET");
        connectionV.setRequestProperty("Content-Type", "application/json");
        connectionV.setRequestProperty("Accept-Charset", "UTF-8");
        connectionV.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36");
        connectionV.connect();
        int responsecode = connectionV.getResponseCode();

        if (responsecode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responsecode);
        } else {

      
            readerV = new BufferedReader(new InputStreamReader(connectionV.getInputStream()));
            while ((lineV = readerV.readLine()) != null) {
                responseContentV.append(lineV);
            }
            readerV.close();

            System.out.println("Success");
            
            JSONObject jobj = new JSONObject(responseContentV.toString());
            System.out.println("ResponseString: "+responseContentV);
            volume = jobj.getString("volume");
            
            System.out.println("Volume of skin: "+skinName+ " is: "+volume);
            
            
            
            
            }
        
        }catch(Exception e)
        {
            System.out.println("Exception: "+e.getMessage());
        }
        
        
        
        return volume;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}



