// CODE SAU KHI REFACTOR (đã comment rõ từng thay đổi)

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PersonalTaskManagerRefactored {

    private static final String DB_FILE_PATH = "tasks_database.json";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Không thay đổi: load dữ liệu từ DB
    private static JSONArray loadTasksFromDb() {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(DB_FILE_PATH)) {
            Object obj = parser.parse(reader);
            if (obj instanceof JSONArray) {
                return (JSONArray) obj;
            }
        } catch (IOException | ParseException e) {
            System.err.println("Lỗi khi đọc file database: " + e.getMessage());
        }
        return new JSONArray();
    }

    // Không thay đổi: ghi dữ liệu ra DB
    private static void saveTasksToDb(JSONArray tasksData) {
        try (FileWriter file = new FileWriter(DB_FILE_PATH)) {
            file.write(tasksData.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi vào file database: " + e.getMessage());
        }
    }

    // BƯỚC MỚI: kiểm tra dữ liệu đầu vào (refactor từ dòng 50–72)
    private static boolean validateTaskInput(String title, String dueDateStr, String priorityLevel) {
        if (title == null || title.trim().isEmpty()) {
            System.out.println("Lỗi: Tiêu đề không được để trống.");
            return false;
        }
        if (dueDateStr == null || dueDateStr.trim().isEmpty()) {
            System.out.println("Lỗi: Ngày đến hạn không được để trống.");
            return false;
        }
        String[] validPriorities = {"Thấp", "Trung bình", "Cao"};
        for (String valid : validPriorities) {
            if (valid.equals(priorityLevel)) {
                return true;
            }
        }
        System.out.println("Lỗi: Mức độ ưu tiên không hợp lệ. Vui lòng chọn từ: Thấp, Trung bình, Cao.");
        return false;
    }

    // BƯỚC MỚI: kiểm tra trùng lặp (refactor dòng 83–90)
    private static boolean isDuplicateTask(JSONArray tasks, String title, LocalDate dueDate) {
        for (Object obj : tasks) {
            JSONObject task = (JSONObject) obj;
            if (task.get("title").toString().equalsIgnoreCase(title)
                    && task.get("due_date").toString().equals(dueDate.format(DATE_FORMATTER))) {
                return true;
            }
        }
        return false;
    }

    // BƯỚC MỚI: tạo task ID đơn giản hơn (loại bỏ UUID – áp dụng YAGNI)
    private static String generateTaskId() {
        return String.valueOf(System.currentTimeMillis()); // Đơn giản hơn UUID
    }

    // BƯỚC MỚI: tạo đối tượng JSONObject từ input (refactor dòng 92–103)
    private static JSONObject createTaskJson(String taskId, String title, String description, LocalDate dueDate, String priority) {
        JSONObject task = new JSONObject();
        task.put("id", taskId);
        task.put("title", title);
        task.put("description", description);
        task.put("due_date", dueDate.format(DATE_FORMATTER));
        task.put("priority", priority);
        task.put("status", "Chưa hoàn thành");
        task.put("created_at", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        task.put("last_updated_at", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        return task;
    }

    // HÀM CHÍNH SAU KHI REFACTOR (rút gọn còn ~10 dòng logic)
    public JSONObject addNewTask(String title, String description,
            String dueDateStr, String priorityLevel) {
        if (!validateTaskInput(title, dueDateStr, priorityLevel)) {
            return null; // Refactor dòng 50–72
        }
        LocalDate dueDate;
        try {
            dueDate = LocalDate.parse(dueDateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("Lỗi: Ngày đến hạn không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
            return null;
        }

        JSONArray tasks = loadTasksFromDb();
        if (isDuplicateTask(tasks, title, dueDate)) { // Refactor dòng 83–90
            System.out.println("Lỗi: Nhiệm vụ đã tồn tại với cùng ngày đến hạn.");
            return null;
        }

        String taskId = generateTaskId(); // Thay UUID => áp dụng YAGNI
        JSONObject newTask = createTaskJson(taskId, title, description, dueDate, priorityLevel);
        tasks.add(newTask);
        saveTasksToDb(tasks);

        System.out.println("Đã thêm nhiệm vụ thành công với ID: " + taskId);
        return newTask;
    }

    public static void main(String[] args) {
        PersonalTaskManagerRefactored manager = new PersonalTaskManagerRefactored();
        manager.addNewTask("Mua sách", "Sách Công nghệ phần mềm.", "2025-07-20", "Cao");
    }
}
