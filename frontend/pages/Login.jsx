import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginUser } from "../services/authService";

function Login() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    usernameOrEmail: "",
    password: "",
  });

  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const handleChange = (event) => {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value,
    });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    setMessage("");
    setError("");

    try {
      const response = await loginUser(formData);
      // Store logged-in user details
      localStorage.setItem(
        "triptalesUser",
        JSON.stringify(response)
      );

      setMessage("Login successful! Welcome to TripTales AI.");

      setTimeout(() => {
        navigate("/");
      }, 1000);

    } catch (error) {
      console.error("Login error:", error);

      if (error.response?.data?.message) {
        setError(error.response.data.message);
      } else {
        setError(
          "Login failed. Please check your username/email and password."
        );
      }
    }
  };

  return (
    <div className="auth-page">

      <div className="auth-card">

        <h1>Welcome Back</h1>

        <p className="auth-subtitle">
          Login to continue your travel journey.
        </p>

        {message && (
          <p
            style={{
              color: "#16a34a",
              textAlign: "center",
              marginBottom: "20px",
              fontWeight: "600",
            }}
          >
            {message}
          </p>
        )}

        {error && (
          <p
            style={{
              color: "#dc2626",
              textAlign: "center",
              marginBottom: "20px",
              fontWeight: "600",
            }}
          >
            {error}
          </p>
        )}

        <form onSubmit={handleSubmit}>

          <div className="form-group">
            <label>Username or Email</label>

            <input
              type="text"
              name="usernameOrEmail"
              value={formData.usernameOrEmail}
              onChange={handleChange}
              placeholder="Enter username or email"
              required
            />
          </div>

          <div className="form-group">
            <label>Password</label>

            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              placeholder="Enter your password"
              required
            />
          </div>

          <button
            type="submit"
            className="auth-button"
          >
            Login
          </button>

        </form>

        <p className="auth-footer">
          Don't have an account?{" "}

          <a href="/register">
            Create an account
          </a>
        </p>

      </div>

    </div>
  );
}

export default Login;