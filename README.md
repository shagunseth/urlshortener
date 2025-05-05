# 🔗 Scalable URL Shortener

A high-performance, distributed URL shortener system built with **Java Spring Boot**, **MySQL**, **Redis**, and **Docker**. Inspired by systems like Bit.ly, this project showcases modern backend architecture, caching, and scalable design principles.

-----------------------------------------------------------------------------------------------------------------------------

## 🚀 Features

- Generate short links from long URLs
- Redirect from short links to original URLs
- Base62 encoding for clean short codes
- Caching layer using Redis for fast access
- Persistent storage in MySQL
- Custom alias support
- Optional expiry date for links
- Click tracking (basic analytics)
- Containerized with Docker Compose

-------------------------------------------------------------------------------------------------------------------------------

### Components

| Component | Purpose                             |
|----------|--------------------------------------|
| Spring Boot | Core application logic (API + service) |
| MySQL       | Persistent URL and metadata storage |
| Redis       | Fast caching layer for short code lookup |
| Base62      | Converts DB ID to short code          |

---

## ⚙️ Tech Stack

- Java 17, Spring Boot 3.x
- MySQL 8
- Redis
- Docker & Docker Compose
- Maven
- Lombok

---

## 📦 Installation

### 1. Clone the Repo

```bash
git clone https://github.com/your-username/url-shortener.git
cd url-shortener
mvn clean package -DskipTests
docker-compose up --build


App will be available at:
🔗 http://localhost:8080