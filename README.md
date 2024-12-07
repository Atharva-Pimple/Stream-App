# Stream-App

Stream App is a video streaming platform built using React (with Vite) for the frontend and Spring Boot for the backend. It allows users to upload, stream, and enjoy videos seamlessly. The application incorporates Tailwind CSS for styling and uses FFmpeg for video processing on the backend.

---

## Technology

- **Frontend**:
  - Developed with React and Vite for fast development and performance.
  - Responsive design using Tailwind CSS.
  - Interactive video streaming interface.

- **Backend**:
  - Built with Spring Boot for robust backend support.
  - Supports video processing using FFmpeg.
  - REST API integration for frontend-backend communication.

---

## Prerequisites

### 1. System Requirements
- **Node.js**: Ensure you have Node.js (v16 or later) installed.
- **Java**: Install Java Development Kit (JDK) 17 or later.
- **Maven**: Ensure Maven is installed for building the backend.
- **FFmpeg**: Required for video processing.

### 2. Install FFmpeg
FFmpeg is an essential tool for handling video processing tasks. Follow the steps below to install and configure FFmpeg:

#### **Windows**
1. Download the FFmpeg build from the [official FFmpeg website](https://ffmpeg.org/download.html).
2. Extract the downloaded archive and copy the folder to a location (e.g., `C:\ffmpeg`).
3. Add FFmpeg to the system PATH:
   - Open "Environment Variables" in system settings.
   - Edit the `Path` variable and add the path to the `bin` folder of your FFmpeg installation (e.g., `C:\ffmpeg\bin`).
4. Verify installation:
   ```bash
   ffmpeg -version
### **Linux**
1. Update the package manager:
   ```bash
   sudo apt update
2. Install ffmpeg
   ```bash
   sudo apt install ffmpeg
3. Verify Installation
   ```bash
   ffmpeg -version

## Project Setup

### Frontend setup (React + Vite)
1. Navigate to the frontend directory:
   ```bash
   cd stream-frontend
2. Install dependencies:
   ```bash
   npm install
3. Start the Development server:
   ```bash
   npm run dev

### Backend setup
1. Navigate Backend directory
   ```bash
   cd spring-stream-backend
2. Build project using maven:
   ```bash
   mvn clean install
3. Run the Backend server
   ```bash
   mvn spring-boot:run

### Running the Application
1. Start the backend server as described above.
2. Start the frontend server.
3. Access the application by visiting:
   - **Frontend**: [http://localhost:5173](http://localhost:5173) (or the port configured in Vite).  
   - **Backend API**: [http://localhost:8080](http://localhost:8080).

## Additional Notes

  - Ensure FFmpeg is installed and configured correctly. The backend requires FFmpeg for video encoding and resolution adaptation.
  - Tailwind CSS setup is pre-configured in the `tailwind.config.js` file in the frontend directory.
  - Verify that all dependencies for both the frontend and backend are installed correctly to avoid runtime issues.
