import "./App.css";
import VideoUpload from "./components/VideoUpload";
import NavBar from "./components/NavBar";
import Videos from "./components/Videos";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import PlayScreen from "./components/PlayScreen";
import About from "./components/About";
import Login from "./components/Login";
import Register from "./components/Register";

function App() {
  return (
    <div>
      <Router>
        <NavBar/>
        <Routes>
          <Route path="/" element={<Videos/>}/>
          <Route path="/videos" element={<Videos/>}/>
          <Route path="/videos/play" element={<PlayScreen/>}/>
          <Route path="/upload" element={<VideoUpload/>}/>
          <Route path="/about" element={<About/>}/>
          <Route path="/login" element={<Login/>}/>
          <Route path="/signup" element={<Register/>}/>
        </Routes>
      </Router>
    </div>
  );
  
  
}

export default App;
