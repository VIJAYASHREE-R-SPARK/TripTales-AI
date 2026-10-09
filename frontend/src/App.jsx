import { BrowserRouter, Routes, Route } from "react-router-dom";

import Navbar from "../components/Navbar.jsx";

import Home from "../pages/Home.jsx";
import Explore from "../pages/Explore.jsx";
import Login from "../pages/Login.jsx";
import Register from "../pages/Register.jsx";
import Profile from "../pages/Profile.jsx";
import CreatePost from "../pages/CreatePost.jsx";
import Notifications from "../pages/Notifications.jsx";

function App() {
  return (
    <BrowserRouter>
      <Navbar />

      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/explore" element={<Explore />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/create-post" element={<CreatePost />} />
        <Route path="/notifications" element={<Notifications />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;