import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import redis.clients.jedis.Jedis;


import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class redis_homework {
    public static void main(String[] args) throws SQLException, IOException {
        //Łączanie z redisem będącym na localhoście
        Jedis jedis = new Jedis("localhost");
        //Łączanie z bazą danych
        String url = "jdbc:sqlite:D:/chinook.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(url);
        //Utworzenie DAO dla albums
        Dao<albums,Integer> daoAlbums = DaoManager.createDao(connectionSource, albums.class);

        Gson gson = new Gson();
        while(true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("q: wylaczenie programu; *query* from albums *rest of query*: zapytanie do tabeli albums");

            System.out.println("____WPISZ KOMENDE____");
            String query = scanner.nextLine();

            if (query.equals("q")) {
                connectionSource.close();
                break;
            }
            else {
                //Stworzenie redisowego klucza dla query
                String Key = query.toUpperCase().replaceAll(" ","");

                //Sprawdzanko, czy wyniki są w redisie
                if (jedis.exists(Key)){
                    showRedisResults(jedis,gson, Key);
                }

                else {
                    //Pobranie wynikow z bazy danych
                    List<String[]> resultDB = getDBresults(daoAlbums,query);
                    String queryResult = gson.toJson(resultDB);
                    jedis.set(Key, queryResult);
                    jedis.expire(Key, 420); //dane wygasaja po 420 sekundnach
                }
            }
        }
    }
    //klasa pokazujaca wyniki jesli sa one w redisie
    private static void showRedisResults(Jedis jedis, Gson gson, String Key){
        Type theList = new TypeToken<List<String[]>>(){}.getType();
        String fromRedis = jedis.get(Key);
        List<String[]> resultRedis = gson.fromJson(fromRedis, theList);
        resultRedis.forEach(arr -> System.out.println(Arrays.toString(arr)));
        System.out.println("_______"+ "\n" +"Redis");
    }
    //klasa ktora pobiera dane z bazy i je wyswietla
    private static  List<String[]> getDBresults(Dao<albums, Integer> daoAlbums, String query) throws SQLException{
        GenericRawResults<String[]> rawResults = daoAlbums.queryRaw(query);
        List<String[]> resultDB = rawResults.getResults();
        resultDB.forEach(ar -> System.out.println(Arrays.toString(ar)));
        System.out.println("_______" + "\n" + "DB");
        return resultDB;
    }
}