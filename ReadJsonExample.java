import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;

public class ReadJsonExample {
    public static void main(String[] args) {
        try {
            // อ่านไฟล์ JSON
            FileReader reader = new FileReader("data.json");

            // สร้าง Gson และแปลงเป็นอ็อบเจกต์
            Gson gson = new Gson();
            Person person = gson.fromJson(reader, Person.class);

            // แสดงข้อมูล
            System.out.println("Name: " + person.name);
            System.out.println("Age: " + person.age);

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
