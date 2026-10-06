import { useEffect, useState } from "react";
import axios from "axios";

function Notifications() {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // Temporary logged-in user for testing
  const userId = 4;

  useEffect(() => {
    const fetchNotifications = async () => {
      try {
        setLoading(true);
        setError("");

        const response = await axios.get(
          `http://localhost:8080/api/notifications/user/${userId}`
        );

        console.log("Notifications:", response.data);

        setNotifications(response.data || []);
      } catch (error) {
        console.error("Notification loading error:", error);

        setError(
          "Unable to load notifications. Please make sure the backend is running."
        );
      } finally {
        setLoading(false);
      }
    };

    fetchNotifications();
  }, []);

  const formatDate = (dateValue) => {
    if (!dateValue) {
      return "";
    }

    try {
      return new Date(dateValue).toLocaleString();
    } catch {
      return dateValue;
    }
  };

  if (loading) {
    return (
      <div className="profile-page">
        <div className="empty-profile">
          <div className="empty-icon">⏳</div>

          <h2>Loading notifications...</h2>

          <p>
            Please wait while we check for new notifications.
          </p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="profile-page">
        <div className="empty-profile">
          <div className="empty-icon">⚠️</div>

          <h2>Something went wrong</h2>

          <p>{error}</p>

          <button
            className="create-post-button"
            onClick={() => window.location.reload()}
          >
            Try Again
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="profile-page">
      <section className="profile-content">
        <div className="section-heading">
          <div>
            <span className="section-label">
              NOTIFICATIONS
            </span>

            <h1>Notifications 🔔</h1>

            <p>
              Stay updated with activity from your
              TripTales AI community.
            </p>
          </div>
        </div>

        {notifications.length === 0 ? (
          <div className="empty-profile">
            <div className="empty-icon">🔔</div>

            <h2>No notifications yet</h2>

            <p>
              When someone interacts with your posts
              or follows you, your notifications will
              appear here.
            </p>
          </div>
        ) : (
          <div
            style={{
              display: "flex",
              flexDirection: "column",
              gap: "14px",
              marginTop: "25px",
            }}
          >
            {notifications.map((notification) => (
              <div
                key={
                  notification.notificationId ||
                  notification.id
                }
                style={{
                  display: "flex",
                  alignItems: "center",
                  gap: "16px",
                  padding: "18px",
                  borderRadius: "16px",
                  background: "#ffffff",
                  border: "1px solid #e5e7eb",
                  boxShadow:
                    "0 4px 12px rgba(0,0,0,0.05)",
                }}
              >
                <div
                  style={{
                    width: "48px",
                    height: "48px",
                    minWidth: "48px",
                    borderRadius: "50%",
                    background:
                      "linear-gradient(135deg, #6366f1, #8b5cf6)",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    fontSize: "22px",
                  }}
                >
                  🔔
                </div>

                <div style={{ flex: 1 }}>
                  <h3
                    style={{
                      margin: "0 0 6px",
                    }}
                  >
                    {notification.title ||
                      notification.message ||
                      "New notification"}
                  </h3>

                  {notification.message &&
                    notification.title && (
                      <p
                        style={{
                          margin: 0,
                          color: "#6b7280",
                        }}
                      >
                        {notification.message}
                      </p>
                    )}

                  {notification.createdAt && (
                    <small
                      style={{
                        display: "block",
                        marginTop: "8px",
                        color: "#9ca3af",
                      }}
                    >
                      {formatDate(
                        notification.createdAt
                      )}
                    </small>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </section>
    </div>
  );
}

export default Notifications;