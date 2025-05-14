import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description:
 * Author:dengww
 * Date:
 */
public class Differ {
    public static void main(String[] args) {
        Set<String> esIds = readIdsFromFile("/Users/cds-dn-670/Dev/ideaWorkplace/learn/big-data/spark-3.1.0/src/main/java/es.txt");
        Set<String> sr = readIdsFromFile("/Users/cds-dn-670/Dev/ideaWorkplace/learn/big-data/spark-3.1.0/src/main/java/sr.txt");

        Set<String> esSet = new HashSet<>(esIds);
        Set<String> srSet = new HashSet<>(sr);
        srSet.removeAll(esSet);
//        esSet.removeAll(srSet);
        System.out.println(srSet);
//        String collect = srSet.stream().map(product_id -> "\"" + product_id + "\"").collect(Collectors.joining(","));
//        System.out.println(collect);
        writeIdsToFile(srSet,"/Users/cds-dn-670/Dev/ideaWorkplace/learn/big-data/spark-3.1.0/src/main/java/result.txt");
    }

    private static Set<String> readIdsFromFile(String filename) {
        Set<String> ids = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                ids.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ids;
    }

    private static void writeIdsToFile(Set<String> ids, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String id : ids) {
                writer.write(id);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
