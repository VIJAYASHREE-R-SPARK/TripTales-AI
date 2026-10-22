import { useEffect, useState } from "react";
import "../src/index.css";
import API_BASE_URL from "../services/api";

const API_URL = API_BASE_URL;

const defaultPrivacy = {
  profileVisibility: "PUBLIC",
  locationVisibility: "DESTINATION",
  showTravelMap: true,
  allowFollowRequests: true,
};

const defaultNotifications = {
  likeNotifications: true,
  commentNotifications: true,
  followNotifications: true,
  recommendationNotifications: true,
};

function Settings() {
  const storedUser = JSON.parse(
    localStorage.getItem("triptalesUser") || "null"
  );

  const userId = Number(storedUser?.userId);

  const [privacy, setPrivacy] = useState(defaultPrivacy);
  const [notifications, setNotifications] = useState(
    defaultNotifications
  );

  const [privacyId, setPrivacyId] = useState(null);
  const [notificationSettingId, setNotificationSettingId] =
    useState(null);

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState("");

  useEffect(() => {
    if (!userId) {
      setLoading(false);
      setMessage("Please login to access Settings.");
      return;
    }

    loadSettings();
  }, [userId]);

  async function loadOrCreateSettings(endpoint, defaults, setData, setId) {
    const response = await fetch(`${API_URL}/${endpoint}/user/${userId}`);

 
    if (response.ok) {
        const data = await response.json();

        setData({ ...defaults, ...data });

        const recordId =
        endpoint === "privacy-settings"
        ? data.privacyId
        : data.notificationSettingId;

        console.log(`${endpoint} response:`, data);
        console.log(`${endpoint} record ID:`, recordId);

        setId(recordId ?? null);

        return;
    }



    if (response.status !== 404) {
      throw new Error(`Unable to load ${endpoint}.`);
    }

    // This user doesn't have a settings record yet.
    const createResponse = await fetch(`${API_URL}/${endpoint}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        userId,
        ...defaults,
      }),
    });

    if (!createResponse.ok) {
      throw new Error(`Unable to create ${endpoint}.`);
    }

    const created = await createResponse.json();
    setData({ ...defaults, ...created });
    setId(
      created[endpoint === "privacy-settings"
        ? "privacyId"
        : "notificationSettingId"]
    );
  }

  async function loadSettings() {
    setLoading(true);
    setMessage("");

    try {
      await Promise.all([
        loadOrCreateSettings(
          "privacy-settings",
          defaultPrivacy,
          setPrivacy,
          setPrivacyId
        ),
        loadOrCreateSettings(
          "notification-settings",
          defaultNotifications,
          setNotifications,
          setNotificationSettingId
        ),
      ]);
    } catch (error) {
      setMessage(error.message);
    } finally {
      setLoading(false);
    }
  }

  function updatePrivacy(field, value) {
    setPrivacy((previous) => ({
      ...previous,
      [field]: value,
    }));
  }

  function updateNotification(field, value) {
    setNotifications((previous) => ({
      ...previous,
      [field]: value,
    }));
  }

  async function saveSettings(event) {
    event.preventDefault();

    if (!userId) {
      setMessage("Please login first.");
      return;
    }

    setSaving(true);
    setMessage("");

    try {
      const requests = [
        {
          endpoint: "privacy-settings",
          id: privacyId,
          data: { ...privacy, userId },
        },
        {
          endpoint: "notification-settings",
          id: notificationSettingId,
          data: { ...notifications, userId },
        },
      ];

      for (const request of requests) {
        if (!request.id) {
          throw new Error(
            "Settings record ID is missing. Please reload the page."
          );
        }

        const response = await fetch(
          `${API_URL}/${request.endpoint}/${request.id}`,
          {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(request.data),
          }
        );

        if (!response.ok) {
          throw new Error(`Unable to save ${request.endpoint}.`);
        }
      }

      setMessage("Settings saved successfully!");
    } catch (error) {
      setMessage(error.message);
    } finally {
      setSaving(false);
    }
  }

  if (loading) {
    return <div className="settings-page">Loading settings...</div>;
  }

  return (
    <main className="settings-page">
      <header className="settings-header">
        <span className="settings-label">YOUR ACCOUNT</span>
        <h1>Settings ⚙️</h1>
        <p>Manage your privacy and notification preferences.</p>
      </header>

      {!userId ? (
        <section className="settings-card">
          <p>{message}</p>
          <a href="/login">Go to Login</a>
        </section>
      ) : (
        <form onSubmit={saveSettings}>
          <section className="settings-card">
            <h2>🔒 Privacy Settings</h2>
            <p className="settings-description">
              Choose how your travel profile and location are shared.
            </p>

            <div className="settings-row">
              <div>
                <strong>Profile Visibility</strong>
                <p>Control who can view your profile.</p>
              </div>

              <select
                value={privacy.profileVisibility}
                onChange={(event) =>
                  updatePrivacy("profileVisibility", event.target.value)
                }
              >
                <option value="PUBLIC">Public</option>
                <option value="PRIVATE">Private</option>
              </select>
            </div>

            <div className="settings-row">
              <div>
                <strong>Location Visibility</strong>
                <p>Control how much location information is shared.</p>
              </div>

              <select
                value={privacy.locationVisibility}
                onChange={(event) =>
                  updatePrivacy("locationVisibility", event.target.value)
                }
              >
                <option value="PUBLIC">Public</option>
                <option value="DESTINATION">Destinations Only</option>
                <option value="PRIVATE">Private</option>
              </select>
            </div>

            <div className="settings-row">
              <div>
                <strong>Show Travel Map</strong>
                <p>Allow your travel map to be displayed.</p>
              </div>

              <input
                type="checkbox"
                checked={Boolean(privacy.showTravelMap)}
                onChange={(event) =>
                  updatePrivacy("showTravelMap", event.target.checked)
                }
              />
            </div>

            <div className="settings-row">
              <div>
                <strong>Allow Follow Requests</strong>
                <p>Allow other users to send follow requests.</p>
              </div>

              <input
                type="checkbox"
                checked={Boolean(privacy.allowFollowRequests)}
                onChange={(event) =>
                  updatePrivacy(
                    "allowFollowRequests",
                    event.target.checked
                  )
                }
              />
            </div>
          </section>

          <section className="settings-card">
            <h2>🔔 Notification Settings</h2>
            <p className="settings-description">
              Choose which activities you want to receive notifications for.
            </p>

            {[
              ["likeNotifications", "Likes", "When someone likes your post."],
              [
                "commentNotifications",
                "Comments",
                "When someone comments on your post.",
              ],
              [
                "followNotifications",
                "Follows",
                "When someone follows you.",
              ],
              [
                "recommendationNotifications",
                "Recommendations",
                "When travel recommendations are available.",
              ],
            ].map(([field, title, description]) => (
              <div className="settings-row" key={field}>
                <div>
                  <strong>{title}</strong>
                  <p>{description}</p>
                </div>

                <input
                  type="checkbox"
                  checked={Boolean(notifications[field])}
                  onChange={(event) =>
                    updateNotification(field, event.target.checked)
                  }
                />
              </div>
            ))}
          </section>

          <div className="settings-actions">
            <button
              type="submit"
              className="settings-save-button"
              disabled={saving}
            >
              {saving ? "Saving..." : "Save Changes"}
            </button>
          </div>

          {message && (
            <p
              className={
                message.toLowerCase().includes("success")
                  ? "settings-message success"
                  : "settings-message error"
              }
              role="status"
            >
              {message}
            </p>
          )}
        </form>
      )}
    </main>
  );
}

export default Settings;