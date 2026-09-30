import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import axios from "axios";

function Profile() {
  const [user, setUser] = useState(null);
  const [posts, setPosts] = useState([]);
  const [follows, setFollows] = useState([]);

  const [postPhotos, setPostPhotos] = useState({});

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // Temporary logged-in user
  const userId = 7;

  useEffect(() => {
    const fetchProfileData = async () => {
      try {
        setLoading(true);
        setError("");

        const [userResponse, postsResponse, followsResponse] =
          await Promise.all([
            axios.get(`http://localhost:8080/api/users/${userId}`),
            axios.get("http://localhost:8080/api/posts"),
            axios.get("http://localhost:8080/api/follows"),
          ]);

        console.log("User:", userResponse.data);
        console.log("Posts:", postsResponse.data);
        console.log("Follows:", followsResponse.data);

        setUser(userResponse.data);
        setPosts(postsResponse.data);
        setFollows(followsResponse.data);

        // Get posts created by this user
        const currentUserPosts = postsResponse.data.filter(
          (post) => Number(post.userId) === userId
        );

        // Fetch photos for each post
        const photoResults = await Promise.all(
          currentUserPosts.map(async (post) => {
            try {
              const response = await axios.get(
                `http://localhost:8080/api/photos/post/${post.postId}`
              );

              return {
                postId: post.postId,
                photos: response.data,
              };
            } catch (photoError) {
              console.error(
                `Unable to load photos for post ${post.postId}:`,
                photoError
              );

              return {
                postId: post.postId,
                photos: [],
              };
            }
          })
        );

        // Convert photo results into object
        const photoMap = {};

        photoResults.forEach((item) => {
          photoMap[item.postId] = item.photos;
        });

        console.log("Post Photos:", photoMap);

        setPostPhotos(photoMap);

      } catch (error) {
        console.error("Profile loading error:", error);

        setError(
          "Unable to load profile. Please make sure the backend is running."
        );
      } finally {
        setLoading(false);
      }
    };

    fetchProfileData();
  }, []);

  // Posts created by this user
  const userPosts = posts.filter(
    (post) => Number(post.userId) === userId
  );

  // People following this user
  const followers = follows.filter(
    (follow) => Number(follow.followingId) === userId
  );

  // People this user follows
  const following = follows.filter(
    (follow) => Number(follow.followerId) === userId
  );

  if (loading) {
    return (
      <div className="profile-page">
        <div className="empty-profile">
          <div className="empty-icon">⏳</div>

          <h2>Loading profile...</h2>

          <p>
            Please wait while we load your travel profile.
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

      {/* Profile Header */}
      <section className="profile-header">

        <div className="profile-avatar">

          {user?.profileImage ? (
            <img
              src={user.profileImage}
              alt={user.username}
              style={{
                width: "100%",
                height: "100%",
                borderRadius: "50%",
                objectFit: "cover",
              }}
            />
          ) : (
            "👤"
          )}

        </div>

        <div className="profile-info">

          <h1>
            {user?.fullName || user?.username}
          </h1>

          <p className="profile-username">
            @{user?.username}
          </p>

          <p className="profile-bio">
            {user?.bio ||
              "No bio added yet. Start sharing your travel journey!"}
          </p>

          {/* Social Statistics */}
          <div className="profile-stats">

            <div>
              <strong>{userPosts.length}</strong>
              <span>Posts</span>
            </div>

            <div>
              <strong>{followers.length}</strong>
              <span>Followers</span>
            </div>

            <div>
              <strong>{following.length}</strong>
              <span>Following</span>
            </div>

          </div>

        </div>

        <Link
          to="/create-post"
          className="profile-edit-button"
        >
          Create Post
        </Link>

      </section>

      {/* Profile Tabs */}
      <section className="profile-content">

        <div className="profile-tabs">

          <button className="active-tab">
            📸 My Posts
          </button>

          <button>
            🗺️ Travel Memories
          </button>

          <button>
            ❤️ Saved
          </button>

        </div>

        {/* Posts */}
        {userPosts.length === 0 ? (

          <div className="empty-profile">

            <div className="empty-icon">
              📷
            </div>

            <h2>No posts yet</h2>

            <p>
              Start sharing your travel experiences
              with the TripTales AI community.
            </p>

            <Link
              to="/create-post"
              className="create-post-button"
            >
              Share Your First Journey
            </Link>

          </div>

        ) : (

          <div className="destination-grid">

            {userPosts.map((post) => {

              const photos = postPhotos[post.postId] || [];

              const firstPhoto = photos.length > 0
                ? photos[0]
                : null;

              return (
                <div
                  className="destination-card"
                  key={post.postId}
                >

                  {/* Travel Photo */}
                  {firstPhoto ? (

                    <img
                      src={`http://localhost:8080${firstPhoto.imageUrl}`}
                      alt={post.title}
                      style={{
                        width: "100%",
                        height: "220px",
                        objectFit: "cover",
                        borderRadius: "16px 16px 0 0",
                        display: "block",
                      }}
                    />

                  ) : (

                    <div
                      style={{
                        width: "100%",
                        height: "220px",
                        background:
                          "linear-gradient(135deg, #6366f1, #8b5cf6)",
                        borderRadius: "16px 16px 0 0",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        fontSize: "60px",
                      }}
                    >
                      📸
                    </div>

                  )}

                  <div className="destination-content">

                    <div className="destination-location">
                      📅 {post.travelDate}
                    </div>

                    <h3>
                      {post.title}
                    </h3>

                    <p>
                      {post.description}
                    </p>

                    {firstPhoto && (
                      <p
                        style={{
                          marginTop: "10px",
                          color: "#6366f1",
                          fontWeight: "600",
                        }}
                      >
                        📸 Photo uploaded
                      </p>
                    )}

                  </div>

                </div>
              );
            })}

          </div>

        )}

      </section>

      {/* Account Information */}
      <section className="profile-content">

        <div className="section-heading">

          <div>

            <span className="section-label">
              ACCOUNT INFORMATION
            </span>

            <h2>Profile Details</h2>

          </div>

        </div>

        <div className="profile-details">

          <div className="profile-detail-item">
            <span>Full Name</span>
            <strong>
              {user?.fullName || "Not provided"}
            </strong>
          </div>

          <div className="profile-detail-item">
            <span>Username</span>
            <strong>
              @{user?.username}
            </strong>
          </div>

          <div className="profile-detail-item">
            <span>Email</span>
            <strong>
              {user?.email}
            </strong>
          </div>

          <div className="profile-detail-item">
            <span>Account Status</span>
            <strong>
              {user?.accountStatus}
            </strong>
          </div>

          <div className="profile-detail-item">
            <span>Language</span>
            <strong>
              {user?.language}
            </strong>
          </div>

          <div className="profile-detail-item">
            <span>Theme</span>
            <strong>
              {user?.theme}
            </strong>
          </div>

        </div>

      </section>

    </div>
  );
}

export default Profile;