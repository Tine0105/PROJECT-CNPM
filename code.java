
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PersonalTaskManager {

    private static final String DB_FILE_PATH = "tasks_database.json";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private JSONArray loadTasks() {
        try (FileReader reader = new FileReader(DB_FILE_PATH)) {
            Object obj = new JSONParser().parse(reader);
            return (JSONArray) obj;
        } catch (IOException | ParseException e) {
            return new JSONArray();
        }
    }

    private void saveTasks(JSONArray tasks) {
        try (FileWriter writer = new FileWriter(DB_FILE_PATH)) {
            writer.write(tasks.toJSONString());
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi file: " + e.getMessage());
        }
    }

    private boolean isValidPriority(String p) {
        return List.of("Thấp", "Trung bình", "Cao").contains(p);
    }

    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private boolean isDuplicate(JSONArray tasks, String title, String dateStr) {
        for (Object obj : tasks) {
            JSONObject task = (JSONObject) obj;
            if (task.get("title").toString().equalsIgnoreCase(title)
                    && task.get("due_date").toString().equals(dateStr)) {
                return true;
            }
        }
        return false;
    }

    public JSONObject addTask(String title, String desc, String dueDateStr, String priority) {
        if (title == null || title.isBlank() || dueDateStr == null || dueDateStr.isBlank()) {
            System.out.println("Lỗi: Thiếu tiêu đề hoặc ngày đến hạn.");
            return null;
        }

        LocalDate dueDate = parseDate(dueDateStr);
        if (dueDate == null) {
            System.out.println("Lỗi: Ngày đến hạn sai định dạng.");
            return null;
        }

        if (!isValidPriority(priority)) {
            System.out.println("Lỗi: Ưu tiên phải là Thấp, Trung bình, hoặc Cao.");
            return null;
        }

        JSONArray tasks = loadTasks();
        if (isDuplicate(tasks, title, dueDateStr)) {
            System.out.println("Lỗi: Nhiệm vụ trùng lặp.");
            return null;
        }

        JSONObject task = new JSONObject();
        task.put("id", UUID.randomUUID().toString());
        task.put("title", title);
        task.put("description", desc);
        task.put("due_date", dueDateStr);
        task.put("priority", priority);
        task.put("status", "Chưa hoàn thành");
        String now = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        task.put("created_at", now);
        task.put("last_updated_at", now);

        tasks.add(task);
        saveTasks(tasks);
        System.out.println("✅ Đã thêm nhiệm vụ thành công.");
        return task;
    }

    public static void main(String[] args) {
        PersonalTaskManager manager = new PersonalTaskManager();
        manager.addTask("Mua sách", "Sách CNTT", "2025-07-20", "Cao");
        manager.addTask("Tập gym", "1h mỗi sáng", "2025-07-21", "Trung bình");
        manager.addTask("", "Không tiêu đề", "2025-07-22", "Thấp");
    }
}
