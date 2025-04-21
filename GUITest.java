import javax.swing.*;
import java.awt.*;

public class GUITest {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Fastest Railway");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // โหลดภาพต้นฉบับ
        ImageIcon originalIcon = new ImageIcon("BTS.jpg");

        // ปรับขนาดภาพให้เหมาะสม (กว้าง 200px สูง 150px)
        Image image = originalIcon.getImage(); // ดึงภาพออกมา
        Image scaledImage = image.getScaledInstance(800, 600, Image.SCALE_SMOOTH); // ปรับขนาดพร้อมความคมชัด
        ImageIcon resizedIcon = new ImageIcon(scaledImage); // แปลงกลับเป็น ImageIcon

        // ใส่ภาพลงใน JLabel
        JLabel label = new JLabel("Hello, World!");
        label.setIcon(resizedIcon);  // เพิ่มภาพที่ปรับขนาดแล้ว

        // สร้างปุ่ม
        JButton button = new JButton("Start");

        // เมื่อกดปุ่ม ให้เปลี่ยนข้อความ
        button.addActionListener(e -> label.setText("Button Clicked!"));

        // จัดวางองค์ประกอบ
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());  // ใช้ BorderLayout เพื่อจัดตำแหน่ง
        panel.add(label, BorderLayout.CENTER);
        panel.add(button, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }
}
