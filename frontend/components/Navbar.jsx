
import { Link, useNavigate } from "react-router-dom";

function Navbar() {
  const navigate = useNavigate();

  const storedUser = JSON.parse(
    localStorage.getItem("triptalesUser") || "null"
  );

  const handleLogout = () => {
    localStorage.removeItem("triptalesUser");
    navigate("/login");
  };

  return (
    <nav className="navbar">
      <Link to="/" className="logo">
        TripTales AI
      </Link>

      <div className="nav-links">
        <Link to="/">Home</Link>
        <Link to="/explore">Explore</Link>
        <Link to="/create-post">Create Post</Link>
        <Link to="/profile">Profile</Link>

        {storedUser ? (
          <button onClick={handleLogout}>
            Logout
          </button>
        ) : (
          <Link to="/login">Login</Link>
        )}
      </div>
    </nav>
  );
}

export default Navbar;