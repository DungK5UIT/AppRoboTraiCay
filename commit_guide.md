# Hướng dẫn Commit dự án Approbotraicay (Tuần 1)

Chào nhóm 3 người, đây là danh sách file cụ thể mà mỗi người cần chịu trách nhiệm "Commit" để đảm bảo dự án chạy được và không bị lỗi đỏ (conflict).

## 👨‍💻 Dev A (Bạn - Lead/Database)
**Nhiệm vụ:** Commit khung xương dự án và tầng dữ liệu.
*   📁 `app/src/main/java/com/example/approbotraicay/database/`
    *   `DatabaseHelper.java`
    *   `UserDao.java`
*   📁 `app/src/main/java/com/example/approbotraicay/model/`
    *   `TaiKhoan.java`
*   📄 `AndroidManifest.xml` (Khai báo ban đầu)
*   📄 `build.gradle.kts`, `settings.gradle.kts` (Cấu hình project)

## 🎨 Dev B (UI/UX)
**Nhiệm vụ:** Commit giao diện và tài nguyên hình ảnh/màu sắc.
*   📁 `app/src/main/res/layout/`
    *   `reborn_activity_login.xml`
    *   `reborn_activity_register.xml`
    *   `reborn_activity_splash.xml`
*   📁 `app/src/main/res/values/`
    *   `colors_reborn.xml`
    *   `themes.xml`
    *   `strings.xml`

## ⚙️ Dev C (Logic/Features)
**Nhiệm vụ:** Commit logic xử lý trong Java và các công cụ bổ trợ.
*   📁 `app/src/main/java/com/example/approbotraicay/ui/auth/`
    *   `SplashActivity.java`
    *   `LoginActivity.java`
    *   `RegistrationActivity.java`
*   📁 `app/src/main/java/com/example/approbotraicay/utils/`
    *   `SessionManager.java`

---

## 💡 Quy trình Git chuẩn:
1.  **Dev A (Lead)**: Commit toàn bộ thư mục `Approbotraicay` lên trước để tạo nhánh `main`.
2.  **Dev B và C**: `git pull` nhánh `main` về.
3.  Khi làm việc, mỗi người tạo 1 nhánh (branch) riêng:
    *   Dev B: `git checkout -b feature-ui`
    *   Dev C: `git checkout -b feature-auth-logic`
4.  Sau khi xong, các bạn thực hiện **Merge** vào nhánh `main`.
