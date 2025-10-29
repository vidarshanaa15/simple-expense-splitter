# 💸 Expense Splitter – Group Expense Tracker

Full-stack web app that helps groups manage shared expenses easily. Users can create groups, add expenses, view summaries, and track balances in one simple dashboard.

---

## 🚀 Tech Stack

### **Frontend**
- Vue.js 3 (Composition API + Vue Router)
- Tailwind CSS
- LocalStorage (for theme & user data)

### **Backend**
- Spring Boot (RESTful APIs)
- PostgreSQL

---

## ✨ Features

### 🧑‍🤝‍🧑 Group Management
- Create and view all groups  

### 💰 Expense Tracking
- Add, edit, and delete expenses  
- View expense list for each group  
- Summary page showing balance per member  

### 🌙 UI/UX Extras
- Responsive Tailwind-based design  
- Dark mode toggle (saved in LocalStorage)  
- Login/Register flow with LocalStorage validation  

---

## 🧩 API Routes

| Method | Route | Description |
|--------|--------|-------------|
| `POST` | `/groups` | Create a group |
| `GET` | `/groups` | Fetch all groups |
| `POST` | `/groups/{id}/expenses` | Add an expense to a group |
| `GET` | `/groups/{id}/expenses` | Fetch all expenses for a group |
| `GET` | `/groups/{id}/summary` | Compute balances per member |
| `PUT` | `/expenses/{id}` | Edit expense |
| `DELETE` | `/expenses/{id}` | Delete expense |

---

## ⚙️ Setup Instructions

### **1️⃣ Clone the Repository**
```bash
git clone https://github.com/yourusername/simple-expense-splitter.git
cd simple-expense-splitter
```
### **2️⃣ Backend Setup**
```bash
cd ExpenseSplitter
./mvnw spring-boot:run
```
### **3️⃣ Frontend Setup**
```bash
cd simple-expense-frontend
npm install
npm run dev
```
