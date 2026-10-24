<div align="center">

<img src="https://img.shields.io/badge/Status-Live-brightgreen?style=for-the-badge" />
<img src="https://img.shields.io/badge/React-18-61DAFB?style=for-the-badge&logo=react" />
<img src="https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?style=for-the-badge&logo=springboot" />
<img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql" />
<img src="https://img.shields.io/badge/Deployed-Render_%7C_Vercel-black?style=for-the-badge" />

# 🏠 Homease — On-Demand Home Services Platform

### *Book. Track. Done.*

**A full-stack marketplace connecting customers with verified home service providers — featuring real-time order tracking, dual OTP verification, and live provider ratings.**

[🌐 Customer App](https://homease-frontend.vercel.app) &nbsp;•&nbsp;
[👷 Provider Portal](https://homease-provider.vercel.app) &nbsp;•&nbsp;
[🛡️ Admin Panel](https://homease-admin.vercel.app) &nbsp;•&nbsp;
[⚙️ Backend API](https://homease-backend.onrender.com)

</div>

---

## 📌 What is Homease?

Homease is a **full-stack on-demand home services platform** — similar to Urban Company — built entirely by a single developer. Customers can browse services like house cleaning, electrical work, plumbing, painting, and carpentry, book them online, pay securely via Razorpay, and track the service in real time through 6 status stages.

The platform has **3 separate portals**:
- 🏠 **Customer Website** — browse, book, pay, track, rate
- 👷 **Provider Portal** — accept jobs, navigate to customer, verify OTPs, complete jobs
- 🛡️ **Admin Panel** — manage services, approve providers, view all orders

---

## 🎯 Problem It Solves

| Problem | Homease Solution |
|---|---|
| Booking home services still done via phone calls in India | Online booking with real-time confirmation |
| No accountability — provider may not show up | Dual OTP system verifies arrival AND completion |
| Customer has no idea where the provider is | Live status tracking across 6 stages |
| No way to verify service quality | Post-service rating system updates provider score |
| Unverified providers enter homes | Admin approval required before providers go live |
| Admin manually assigns every job | Category-based auto-broadcast to matching providers |

---

## ✨ Key Features

### 👤 Customer Features
- ✅ Browse services by category (Cleaning, Electrical, Plumbing, Painting, Carpentry, Shifting)
- ✅ Add multiple services to cart
- ✅ Secure online payment via **Razorpay**
- ✅ Real-time 6-stage order tracking with progress bar
- ✅ **Start OTP** — share with provider on arrival to begin work
- ✅ **End OTP** — share with provider only after work is fully done
- ✅ Rate provider (1–5 stars) after job completion
- ✅ Full order history with service timeline
- ✅ Print receipt for any order

### 👷 Provider Features
- ✅ Register and wait for admin approval
- ✅ Toggle online/offline availability
- ✅ See available jobs filtered by **their specific category only**
- ✅ Accept jobs (first come, first served)
- ✅ Mark "On the Way" to notify customer
- ✅ Verify customer's **Start OTP** to officially begin service
- ✅ Verify customer's **End OTP** to close the job
- ✅ Dashboard with total earnings, completed jobs, average rating
- ✅ Email notification sent to customer when provider is assigned

### 🛡️ Admin Features
- ✅ Secure admin login (separate from user login)
- ✅ Add / remove services with image upload
- ✅ View all registered providers → approve or reject
- ✅ View all orders with real-time status filters
- ✅ See which provider accepted which order
- ✅ Cancel orders that haven't started yet
- ✅ View full service history log per order

---

## 🔄 Complete Service Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                     COMPLETE ORDER JOURNEY                       │
└─────────────────────────────────────────────────────────────────┘

  CUSTOMER                    PROVIDER                    SYSTEM
     │                           │                           │
     │  1. Browse & Add to Cart  │                           │
     │──────────────────────────────────────────────────────▶│
     │                           │                           │
     │  2. Checkout & Pay (Razorpay)                         │
     │──────────────────────────────────────────────────────▶│
     │                           │    Status: service_requested
     │                           │    Start OTP generated ✓  │
     │                           │◀──────────────────────────│
     │                           │                           │
     │  3. Provider Accepts Job  │                           │
     │                           │──────────────────────────▶│
     │◀── Email: Provider assigned + Start OTP ──────────────│
     │                           │    Status: provider_assigned
     │                           │                           │
     │  4. Provider On The Way   │                           │
     │                           │──────────────────────────▶│
     │  [Start OTP visible]      │    Status: out_for_service│
     │                           │                           │
     │  5. Provider Arrives → Customer shares Start OTP      │
     │  ─────────────────────────▶ Provider enters OTP       │
     │                           │──────────────────────────▶│
     │  [End OTP now visible]    │    Status: service_started│
     │                           │    End OTP generated ✓    │
     │                           │                           │
     │  6. Work Done → Customer shares End OTP               │
     │  ─────────────────────────▶ Provider enters OTP       │
     │                           │──────────────────────────▶│
     │◀── Email: Rate your provider ─────────────────────────│
     │                           │    Status: service_completed
     │                           │                           │
     │  7. Customer Rates Provider (1-5 ⭐)                  │
     │──────────────────────────────────────────────────────▶│
     │                           │    Provider avg rating    │
     │                           │    auto-recalculated ✓    │
```

### Status Stages

| # | Status | Who Sets It | Color |
|---|---|---|---|
| 1 | `service_requested` | System (after payment) | 🔵 Blue |
| 2 | `provider_assigned` | Provider (accepts job) | 🟡 Yellow |
| 3 | `out_for_service` | Provider (on the way) | 🟠 Orange |
| 4 | `service_started` | System (after Start OTP verified) | 🩵 Teal |
| 5 | `service_completed` | System (after End OTP verified) | 🟢 Green |
| 6 | `cancelled` | Admin (before service starts) | 🔴 Red |

---

## 🏗️ System Architecture

```
┌──────────────────────────────────────────────────────────┐
│                     HOMEASE ARCHITECTURE                  │
├──────────────┬───────────────┬───────────────────────────┤
│   FRONTEND   │    PROVIDER   │        ADMIN PANEL        │
│  (Customer)  │    PORTAL     │                           │
│  React+Vite  │  React+Vite   │       React+Vite          │
│  Vercel      │  Vercel       │       Vercel              │
├──────────────┴───────────────┴───────────────────────────┤
│                    REST API (15+ Endpoints)               │
│              Java Spring Boot 3.2 — Render               │
├───────────────────────────────────────────────────────────┤
│                    MySQL Database                         │
│                      Railway                             │
└───────────────────────────────────────────────────────────┘
```

---

## 🛠️ Tech Stack

### Frontend (×3 Portals)
| Technology | Purpose |
|---|---|
| **React 18** | Component-based UI |
| **Vite** | Fast build tool |
| **React Router v6** | Client-side routing |
| **Axios** | HTTP requests |
| **Framer Motion** | Animations |
| **React Bootstrap** | UI components |
| **React Icons** | Icon library |

### Backend
| Technology | Purpose |
|---|---|
| **Java 17** | Core language |
| **Spring Boot 3.2** | REST API framework |
| **Spring Data JPA** | Database ORM |
| **Hibernate** | Entity management |
| **Lombok** | Boilerplate reduction |
| **Spring Mail** | Email notifications |
| **BCrypt** | Password hashing |
| **Spring Actuator** | Health check endpoint |

### Database & Infrastructure
| Technology | Purpose |
|---|---|
| **MySQL 8** | Relational database |
| **Railway** | Managed MySQL hosting |
| **Render** | Backend hosting (Docker) |
| **Vercel** | Frontend hosting (×3) |
| **Razorpay** | Payment gateway |

---

## 📁 Project Structure

```
Homease-production/
│
├── frontend/                    # Customer website
│   ├── src/
│   │   ├── Components/
│   │   │   ├── context/
│   │   │   │   └── storeContext.jsx    # Global state, cart, API URL
│   │   │   ├── Navbar/                 # Navigation with role picker
│   │   │   ├── ServiceItem/            # Service card component
│   │   │   └── ExploreMenu/            # Category filter
│   │   └── Pages/
│   │       ├── Home/                   # Landing page
│   │       ├── Cart/                   # Cart summary
│   │       ├── PlaceOrder/             # Checkout + Razorpay
│   │       ├── MyOrders/               # Order tracking + OTPs + rating
│   │       └── Auth/                   # Login + Signup
│   └── .env.production
│
├── provider/                    # Provider portal
│   ├── src/
│   │   ├── context/
│   │   │   └── ProviderContext.jsx     # Provider auth state
│   │   └── pages/
│   │       ├── Dashboard.jsx           # Earnings, rating, stats
│   │       ├── ActiveJob.jsx           # OTP verification, job control
│   │       └── Login.jsx
│   └── .env.production
│
├── admin/                       # Admin panel
│   ├── src/
│   │   └── Pages/
│   │       ├── Add/                    # Add new services
│   │       ├── Orders/                 # View all orders
│   │       └── Providers/              # Approve/reject providers
│   └── .env.production
│
└── backend/                     # Spring Boot API
    ├── src/main/java/com/homease/backend/
    │   ├── controller/
    │   │   ├── OrderController.java    # Order endpoints
    │   │   ├── ProviderController.java # Provider endpoints
    │   │   ├── AdminProviderController.java
    │   │   ├── AuthController.java     # User auth
    │   │   └── ServiceController.java
    │   ├── service/
    │   │   ├── OrderService.java       # Core business logic
    │   │   ├── ProviderService.java    # Provider logic + emails
    │   │   └── AuthService.java
    │   └── entity/
    │       ├── Order.java              # Dual OTP + tracking history
    │       ├── Provider.java
    │       ├── Service.java
    │       └── User.java
    ├── Dockerfile
    └── src/main/resources/
        └── application.properties
```

---

## 🔌 API Endpoints

### Orders
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/orders/create-order` | Create Razorpay order intent |
| `POST` | `/api/orders/verify-payment` | Save order after payment |
| `GET` | `/api/orders/user/{email}` | Get customer's orders |
| `GET` | `/api/orders` | Get all orders (admin) |
| `POST` | `/api/orders/{id}/rate` | Submit provider rating |
| `PUT` | `/api/orders/{id}/cancel` | Cancel order (admin) |

### Provider
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/provider/register` | Provider registration |
| `POST` | `/api/provider/login` | Provider login |
| `GET` | `/api/provider/available-jobs` | Jobs matching provider category |
| `POST` | `/api/provider/accept-order/{id}` | Accept a job |
| `GET` | `/api/provider/my-jobs` | Provider's accepted jobs |
| `POST` | `/api/provider/out-for-service/{id}` | Mark on the way |
| `POST` | `/api/provider/verify-start-otp` | Start OTP verification |
| `POST` | `/api/provider/verify-end-otp` | End OTP verification |
| `POST` | `/api/provider/toggle-online` | Online/offline toggle |
| `GET` | `/api/provider/dashboard` | Earnings + rating stats |

### Auth & Admin
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/register` | User registration |
| `POST` | `/api/auth/login` | User login |
| `POST` | `/api/admin/login` | Admin login |
| `GET` | `/api/admin/providers` | All providers list |
| `POST` | `/api/admin/provider/approve` | Approve/revoke provider |
| `GET` | `/api/service/list` | All services |
| `POST` | `/api/service/add` | Add new service |

---

## 🔐 Security Features

- **BCrypt** password hashing for all users and providers
- **Dual OTP verification** — Start OTP (begin service) + End OTP (confirm completion)
- **Provider ownership check** — providers can only update orders assigned to them
- **Admin-only routes** — separate admin token, checked on every panel load
- **Category-based job filtering** — providers only see jobs matching their profession
- **Input validation** on all API endpoints

---

## 🚀 Local Development Setup

### Prerequisites
- Java 17+
- Maven
- MySQL 8
- Node.js 18+
- npm

### 1. Clone the repository
```bash
git clone https://github.com/Jignesh-Kanade/Homease-production.git
cd Homease-production
```

### 2. Set up MySQL database
```sql
CREATE DATABASE homease;
```

### 3. Configure backend
```bash
cd backend/src/main/resources
```
Update `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/homease
spring.datasource.username=root
spring.datasource.password=your_password
```

### 4. Run backend
```bash
cd backend
mvn spring-boot:run
```
Backend runs on `http://localhost:8080`

### 5. Run frontend (Customer)
```bash
cd frontend
npm install
npm run dev
```
Runs on `http://localhost:5172`

### 6. Run provider portal
```bash
cd provider
npm install
npm run dev
```
Runs on `http://localhost:5174`

### 7. Run admin panel
```bash
cd admin
npm install
npm run dev
```
Runs on `http://localhost:5173`

---

## 🌐 Deployment

| Service | Platform | Type |
|---|---|---|
| Customer Frontend | Vercel | Static (Vite) |
| Provider Portal | Vercel | Static (Vite) |
| Admin Panel | Vercel | Static (Vite) |
| Backend API | Render | Docker container |
| Database | Railway | Managed MySQL |

All environment variables are injected at build/runtime — no secrets in source code.

---

## 🗄️ Database Schema (Key Tables)

```
orders
├── id, orderId, userId, email
├── firstName, lastName, phone
├── street, city, state, pincode
├── amount, paymentId, paymentStatus
├── serviceCategory              ← used for provider job matching
├── trackingStatus               ← 6-stage status
├── trackingHistory (TEXT/JSON)  ← full audit log
├── otpServiceStart              ← shown to customer, provider enters
├── otpServiceEnd                ← shown after start, confirms completion
├── providerRating, ratingSubmitted
└── provider_id (FK → providers)

providers
├── id, name, email, password (BCrypt)
├── phone, category
├── rating (auto-calculated average)
├── isApproved (admin sets this)
└── isOnline, token

services
├── id, name, image
├── price, description
└── category

users
├── id, email, password (BCrypt)
└── token
```

---

## 💡 Design Decisions & Tradeoffs

| Decision | Reason |
|---|---|
| **Tracking history as JSON string** in TEXT column | Never need to query individual entries — only read full history per order. Avoids extra JOIN |
| **Category saved on order** at booking time | Provider filtering works even if service category changes later |
| **UUID tokens** instead of JWT | Simpler for this scale. JWT would be the production upgrade |
| **First-accept model** for providers | Scalable — no admin bottleneck for job assignment |
| **Dual OTP** instead of single OTP | Verifies both arrival (start) and completion (end) separately |
| **3 separate React apps** | Clean separation of concerns, independent deployments |

---

## 📊 Project Stats

| Metric | Count |
|---|---|
| Portals Built | 3 |
| REST API Endpoints | 15+ |
| Spring Boot Controllers | 5 |
| Service Status Stages | 6 |
| OTPs Per Order | 2 |
| Database Tables | 5 |
| Built By | 1 developer |

---

## 🛣️ Future Roadmap

- [ ] **Real-time notifications** via WebSockets — instant status push to customer
- [ ] **Geolocation tracking** — show provider distance on map (Google Maps API)
- [ ] **Provider payouts** — automatic payment transfer via Razorpay Route API
- [ ] **React Native mobile app** — provider portal for field use
- [ ] **Push notifications** via Firebase Cloud Messaging
- [ ] **Background verification** — Aadhaar upload flow for providers
- [ ] **JWT authentication** — replace UUID tokens with proper JWT + expiry
- [ ] **Pagination** on all list endpoints

---

## 👨‍💻 Developer

**Jignesh Kanade**
Full-Stack Developer | CDAC Student

Built this project end-to-end — database design, REST APIs, 3 React frontends, payment integration, email notifications, Docker deployment.

[![GitHub](https://img.shields.io/badge/GitHub-Jignesh--Kanade-black?style=flat&logo=github)](https://github.com/Jignesh-Kanade)

---

<div align="center">

**⭐ If you found this project useful or interesting, please star the repository!**

*Built with ❤️ by Jignesh Kanade*

</div>