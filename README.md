# PROJECT-CNPM

Tài liệu Refactor Code - Personal Task Manager

1. Mục tiêu của đoạn code
   Đoạn code có nhiệm vụ thêm một nhiệm vụ mới vào hệ thống quản lý nhiệm vụ cá nhân. Thông tin bao gồm tiêu đề, mô tả, ngày đến hạn, mức độ ưu tiên, trạng thái và thời điểm tạo. Dữ liệu được lưu trữ vào file JSON.
2. Vấn đề của đoạn code ban đầu

- Vi phạm nguyên tắc DRY: Lặp lại nhiều đoạn mã cho đọc/ghi file, kiểm tra dữ liệu.
- Vi phạm nguyên tắc KISS: Hàm add quá dài, xử lý nhiều nhiệm vụ trong một hàm.
- Vi phạm nguyên tắc YAGNI: Thêm các thuộc tính như is_recurring, recurrence_pattern dù chưa dùng.

3. Nguyên tắc đã áp dụng khi refactor

- KISS (Keep It Simple, Stupid): Chia nhỏ hàm, mỗi hàm làm một việc cụ thể.
- DRY (Don't Repeat Yourself): Tái sử dụng các đoạn mã thông qua hàm dùng chung.
- YAGNI (You Ain’t Gonna Need It): Bỏ các thuộc tính và chức năng chưa cần thiết.

4. Các bước refactor cụ thể
   Bước 1: Tách hàm kiểm tra dữ liệu rỗng và định dạng ngày.
   Bước 2: Tách hàm kiểm tra mức độ ưu tiên hợp lệ.
   Bước 3: Tách hàm kiểm tra nhiệm vụ bị trùng lặp.
   Bước 4: Tách chức năng đọc và ghi JSON thành hàm riêng.
   Bước 5: Rút gọn hàm addTask chỉ còn nhiệm vụ chính là xử lý logic thêm nhiệm vụ.
   Bước 6: Loại bỏ các tính năng không cần thiết như nhiệm vụ lặp lại (YAGNI).
5. Giải thích lý do cho từng thay đổi

- Giúp code dễ hiểu hơn (KISS), dễ bảo trì và giảm lỗi logic.
- Tránh lặp mã khi đọc/ghi dữ liệu (DRY).
- Giảm độ phức tạp và tăng hiệu suất phát triển phần mềm (YAGNI).

6. So sánh đoạn code trước và sau refactor

- Trước refactor: Hàm add dài >70 dòng, nhiều nhiệm vụ, khó đọc.
- Sau refactor: Hàm add còn khoảng 30 dòng, rõ ràng, dễ tái sử dụng và kiểm thử.
- Đoạn mã kiểm tra lặp lại và xử lý JSON được dùng chung thay vì viết lại.

7. Các bước thực hiện trên GitHub
1. Tạo branch mới từ nhánh chính:
   git checkout -b feature/refactor-add-task
1. Commit từng thay đổi với nội dung rõ ràng:
   git commit -m "Tách hàm kiểm tra dữ liệu và refactor thêm nhiệm vụ"
1. Đẩy nhánh lên GitHub để tạo pull request:
   git push origin feature/refactor-add-task
1. Thực hiện code review và merge về nhánh chính sau khi kiểm thử.
