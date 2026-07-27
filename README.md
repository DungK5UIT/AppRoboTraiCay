# 🍎 Robot Trái Cây — Android Fruit Store App

Native Android e-commerce app for browsing and ordering fruit, with a full customer flow and a separate admin dashboard. Built in Java with a Retrofit-backed REST API and a local SQLite cache.

## Features

**Customer**
- Splash → Login / Registration with session management
- Browse fruit by category (Nhóm Sản Phẩm) with product detail pages
- Cart (Giỏ Hàng), checkout (Thanh Toán), and order history/detail (Đơn Hàng)
- Favorites (Yêu Thích) and product reviews (Đánh Giá)
- In-app chat/support (ChatBox)
- Profile management

**Admin**
- Dashboard with sales statistics (Thống Kê)
- Product management — add / edit / delete (Quản Lý Sản Phẩm)
- Order management and order detail (Đơn Hàng)

## Tech Stack

- **Language:** Java
- **Networking:** Retrofit2 + Gson (REST API, Firebase Realtime Database–style JSON endpoints)
- **Local storage:** SQLite via custom `DatabaseHelper` + DAO layer (products, orders, users, reviews, favorites)
- **Images:** Glide
- **UI:** Android Views (XML layouts), Material Components, ConstraintLayout

## Architecture

```
ui/            Activities (customer + admin)
adapter/       RecyclerView adapters
model/         Data models (SanPham, DonHang, GioHang, TaiKhoan, ...)
database/      SQLite DAO layer
api/           Retrofit service + client
utils/         Session management & helpers
```

## Getting Started

1. Open the project in Android Studio.
2. Let Gradle sync (`compileSdk 34`, `minSdk 24`).
3. Run on an emulator or device — the app launches at `SplashActivity`.

Built as a 3-person team project (Lead/Database, UI/UX, Logic/Features).
