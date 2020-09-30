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
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;



/**
 *
 * @author matth
 */
public class PricEmpireAPI {
    
    private static HttpURLConnection connection;
    private static  BufferedReader reader;
    private static String line;
    private static StringBuffer responseContent = new StringBuffer();
    
    private static StringBuffer responseContent1 = new StringBuffer();
    private static StringBuffer responseContent2 = new StringBuffer();
    private static StringBuffer responseContent3 = new StringBuffer();
    
    private static DecimalFormat df2 = new DecimalFormat("0.00");
    public static double d;
    public static String response = null;
    
    private static HttpURLConnection connection1, connection2, connection3;
    private static BufferedReader reader1, reader2, reader3;
    private static String line1, line2, line3;
    
    private static double buyOrderPriceFromSteam, price, fPrice, fSteamPrice, steamBuyOrderPrice;
    
    
    
   


  
    public static void main(String[] args) {
        getItems();
    }

    public static void getItems() {
        try {
            URL url = new URL("https://pricempire.com/api/v1/getAllItems?token=L1YtKhmJyMIpTfdMRIlpZ15j4cMQIqU9ekIzzrIyxWzQhoFcXrKyk6MLjXBu055cgd0RWZYPxbT81z1RLuIIlMbA0QCOq0eUZJrLxC4UnLDDb2VXXL5A9pHoUnr4fKuo");
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

                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(new FileWriter("F:\\PricEmpireAPI\\records_price_empire.csv"), true);
                } catch (FileNotFoundException e) {
                    System.out.println("File not found" + e.getMessage());;
                }

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();

                System.out.println("Success");
                JSONObject jobj = new JSONObject(responseContent.toString());
                JSONArray jsonarr1 = (JSONArray) jobj.get("items");
                //System.out.println(jsonarr1);

                System.out.println("Size of array: " + jsonarr1.length());

                String columnNamesList = "Name,Steam price,profit" + "\n";

                pw.write(columnNamesList);

                for (int i = 0; i < jsonarr1.length(); i++) {

                    JSONObject items = (JSONObject) jsonarr1.get(i);

                    String skinName = items.getString("name");

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

                        

                            if (CSGOEmpire.getInt("sourcePrice") > 0) {

                                steamBuyOrderPrice = steam.getInt("sourcePrice");

                                price = CSGOEmpire.getInt("sourcePrice");

                                

                                fPrice = price * 0.59 / 100;
                                
                                fSteamPrice = steamBuyOrderPrice / 100;
                                
                                
                                
                                double steamPrice = steamBuyOrderPrice / 100 * 0.526;
                                
                                //System.out.println("SkingName: " + skinName + "Price is: " + fPrice + " -----------  Steam prie: " + fSteamPrice + " Discounted: "+steamPrice);
                                
                                
                                
                                double profit = (fPrice - steamPrice) / steamPrice * 100 ;
                                
                                System.out.println("SkinName: "+skinName + "Empire cost = "+fPrice+ " Steam price after discount = "+steamPrice+" --------->"+profit+"%");
                                
                                //System.out.println("Skin Name: "+skinName + " / profit: "+profit+"%");

                                //System.out.println("Skin name: "+skinName + "    price: "+fPrice);
                                if (fPrice > 30 && profit > 50) {
                                    StringBuilder builder = new StringBuilder();

                                    builder.append(skinName + ",");
                                    builder.append(fPrice + ",");
                                    builder.append(profit + ",");
                                    builder.append('\n');
                                    pw.write(builder.toString());
                                    pw.flush();
                                } else {
                                    //System.out.println("Price < 30 coins");
                                }

                            } else {
                                //System.out.println("Doesnt exist :(");
                            }
                        

                    }

                }

                //loadPriceEmpirePOJO();
            }

        } catch (Exception e) {
            System.out.println("Exception is -------: " + e.getMessage());
        }

    }

    
    public static void loadPriceEmpirePOJO()
        {
            
            skinPOJO sL = null;
            
            
            
            try {
                
                
                //startTime = System.currentTimeMillis();
                String steamCSV = "F:\\PricEmpireAPI\\records_price_empire.csv";

                String line = null;
                FileReader fr = new FileReader(steamCSV);
                BufferedReader theBufReader = new BufferedReader(fr);

                line = theBufReader.readLine();
                ArrayList<skinPOJO> steam = new ArrayList<>();
                while ((line = theBufReader.readLine()) != null) {

                    String[] arr = line.split(",");
                    sL = new skinPOJO();
                    sL.setName(arr[0]);
                    steam.add(sL);
                }

                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(new FileWriter("F:\\PricEmpireAPI\\records_steam_community.csv"), true);
                } catch (FileNotFoundException e) {
                    System.out.println("File not found" + e.getMessage());;
                }

                
                String columnNamesList = "Name,Steam price" + "\n";
                
                pw.write(columnNamesList);
                // No need give the headers Like: id, Name on builder.append
                
                System.out.println("Size of items to check is ----------------------------------> " + steam.size());
                int numberToBeChecked = steam.size();

                
                for (int x = 0; x < steam.size(); x++) {
                
                
                    

                    String name = steam.get(x).getName();
                    

                    startSearch(name);
                    

                    StringBuilder builder = new StringBuilder();
                    
                    builder.append(name + ",");
                    builder.append(d + ",");
                    builder.append('\n');
                    pw.write(builder.toString());
                    pw.flush();
                    
                    
                    d=0;

                
                
                
                //System.out.println(name + "-->"+ id);
                   
                    numberToBeChecked--;
                    System.out.println("Number of items left to search ------------------------------------------------- > "+numberToBeChecked);
            }
                
                
                System.out.println("================ Finished ================");

//                //long elapsedTime = System.currentTimeMillis() - startTime;
//                long elapsedSeconds = elapsedTime / 1000;
//                long secondsDisplay = elapsedSeconds % 60;
//                long elapsedMinutes = elapsedSeconds / 60;
//
//                System.out.println("Finished " + steam.size() + " requests in " + elapsedMinutes);
                
//                long start = System.currentTimeMillis();
//        cc.send();
//        System.out.println("Done in " + (System.currentTimeMillis() - start));
                
                
                
            }
            catch(Exception e)
            {
                System.out.println("Erro: "+e.getMessage());
            }
            

               
            
            
            
            
            
        }
    
    
    
    
    
    
    
    public static void startSearch(String name)
    {
        boolean starFound = false;
        String formattedName;
        String star = null;
        String finalName = null;
        try
        {

//        if(name.substring(0).contains("★"))
//        {
//            starFound = true;
//            star = name.substring(0,1);
//            formattedName = name.substring(1);
//
//
//        }
//        else
//        {
//            formattedName = name;
//        }

            formattedName = name;
            
            //String starbefore = "%3F";
            
            //String starAfter = URLDecoder.decode(starbefore, "UTF-8");
            
            //System.out.println("Star after= "+starAfter);

            String nameToSearch = URLEncoder.encode(formattedName, "UTF-8");
            //System.out.println("Name to search = "+nameToSearch);
            String workingName = nameToSearch.replace("+", "%20").replace("%99", "™").replace("%3F", "★");

            //System.out.println("Working name is: "+workingName);
            

            if(starFound)
            {
                finalName = star + workingName;
            }
            else
            {
                finalName = workingName;
            }


            //System.out.println("Final name is: "+finalName);
        }
        catch(Exception e)
        {
            System.out.println("Exception is: "+e.getMessage());
        }

        retrieveJSON(finalName, name);
        
    }
    
    
    public static void retrieveJSON(String skinName, String name)
    {
        try{
            
            URL url = new URL("http://api.scraperapi.com/?api_key=67c389629947dded3a2187e18ffdf26c&url=https://steamcommunity.com/market/listings/730/"+skinName);
            System.out.println("Request for price is: "+url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);

            int status = connection.getResponseCode();
            System.out.println("Response Code is: "+status);

            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }

            //System.out.println("JSON Array is: " + responseContent.toString());
            response = responseContent.toString();
            //System.out.println("Response is: "+response);

            String itemId = response.substring(response.indexOf("Market_LoadOrderSpread") +23, response.indexOf("// initial load") -3).trim();
            System.out.println("Item Id = "+itemId);
            
            //String topListing = response.substring(response.indexOf("market_listing_price market_listing_price_with_fee"));
            //System.out.println("Top listing is: "+topListing);

            responseContent.delete(0, responseContent.length());


            retrieveBuyOrderPrice(itemId);

        }
        catch(Exception e)
        {
            System.out.println("Exception is new: "+e.getMessage());
        }
        
        //loadSteamVolumeAndMedian(name);
    }
    
    
    
    public static void retrieveBuyOrderPrice(String itemId)
    {
        try
        {
            URL buyOrderPrice = new URL("http://api.scraperapi.com/?api_key=67c389629947dded3a2187e18ffdf26c&url=https://steamcommunity.com/market/itemordershistogram?&language=english&currency=1&item_nameid="+itemId+"&two_factor=0");
            System.out.println("buy order URL: "+buyOrderPrice);
            connection1 = (HttpURLConnection) buyOrderPrice.openConnection();
            connection1.setRequestMethod("GET");
            connection1.setRequestProperty("Content-Type", "application/json");
            connection1.setRequestProperty("Accept-Charset", "UTF-8");
            connection1.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36");
            connection1.setConnectTimeout(15000);
            connection1.setReadTimeout(15000);

            int statusNew = connection1.getResponseCode();
            System.out.println("Response Code is: "+statusNew);

            if (statusNew > 299) {
                reader1 = new BufferedReader(new InputStreamReader(connection1.getErrorStream()));
                while ((line1 = reader1.readLine()) != null) {
                    responseContent1.append(line1);
                }
                reader1.close();
            } else {
                reader1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
                while ((line1 = reader1.readLine()) != null) {
                    responseContent1.append(line1);
                }
                reader1.close();
            }

            //System.out.println("JSON Array is: " + responseContent.toString());
            String response = responseContent1.toString();
            //System.out.println("Response trying to get buy order is: "+response);


            String bop = response.substring(response.indexOf("highest_buy_order")+20, response.indexOf("lowest_sell_order")-3).trim();
            System.out.println("Buy Order Price is = "+bop);
            
            String lso = response.substring(response.indexOf("lowest_sell_order") +20, response.indexOf("buy_order_graph")-3).trim();
            System.out.println("Lowest Sell order price is = "+lso);

            //int integerNumber = parseInt()

            int tempNumber = Integer.parseInt(bop);
            



            buyOrderPriceFromSteam = (double) tempNumber / 100;
            
            
            


            //System.out.println("After cal: "+buyOrderPriceFromSteam);
            
            double afterDiscount = buyOrderPriceFromSteam * 0.526;
            
            //System.out.println("******************************************** Discounted price: "+afterDiscount);
            //System.out.println("After cal: "+buyOrderPriceFromSteam1);

            responseContent1.delete(0, responseContent1.length());

            String tempFormat = df2.format(afterDiscount);
            System.out.println("Formatted String to 2 decimal places: "+tempFormat);

            d =Double.parseDouble(tempFormat);








        }
        catch(Exception e)
        {
            System.out.println("Exception is: "+e.getMessage());
        }


    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
   
    
    
}
