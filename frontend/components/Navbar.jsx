import { Link } from "react-router-dom";

function Navbar() {
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
        <Link to="/login">Login</Link>
      </div>

    </nav>
  );
}

export default Navbar;