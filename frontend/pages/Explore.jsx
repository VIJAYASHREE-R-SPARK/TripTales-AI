import { useEffect, useState } from "react";
import axios from "axios";

function Explore() {
  const CURRENT_USER_ID = 7;

  const [posts, setPosts] = useState([]);
  const [postPhotos, setPostPhotos] = useState({});
  const [postLikes, setPostLikes] = useState({});
  const [postSaves, setPostSaves] = useState({});
  const [postFollows, setPostFollows] = useState({});
  const [destinations, setDestinations] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [likingPostId, setLikingPostId] = useState(null);
  const [savingPostId, setSavingPostId] = useState(null);
  const [followingUserId, setFollowingUserId] = useState(null);

  const [postComments, setPostComments] = useState({});
  const [commentText, setCommentText] = useState({});
  const [commentingPostId, setCommentingPostId] = useState(null);

  useEffect(() => {
    const fetchExploreData = async () => {
      try {
        setLoading(true);
        setError("");

        const [postsResponse, destinationsResponse] =
          await Promise.all([
            axios.get("http://localhost:8080/api/posts"),
            axios.get("http://localhost:8080/api/destinations"),
          ]);

        const postsData = postsResponse.data;
        const destinationsData = destinationsResponse.data;

        setPosts(postsData);
        setDestinations(destinationsData);

        // ============================
        // LOAD PHOTOS
        // ============================

        const photoResults = await Promise.all(
          postsData.map(async (post) => {
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
                `Unable to load photos for post ${post.postId}`,
                photoError
              );

              return {
                postId: post.postId,
                photos: [],
              };
            }
          })
        );

        const photoMap = {};

        photoResults.forEach((item) => {
          photoMap[item.postId] = item.photos;
        });

        setPostPhotos(photoMap);

        // ============================
        // LOAD LIKES
        // ============================

        const likeResults = await Promise.all(
          postsData.map(async (post) => {
            try {
              const response = await axios.get(
                `http://localhost:8080/api/likes/post/${post.postId}`
              );

              const likes = response.data || [];

              return {
                postId: post.postId,
                likes: likes,
              };
            } catch (likeError) {
              console.error(
                `Unable to load likes for post ${post.postId}`,
                likeError
              );

              return {
                postId: post.postId,
                likes: [],
              };
            }
          })
        );

        const likesMap = {};

        likeResults.forEach((item) => {
          const likes = item.likes || [];

          likesMap[item.postId] = {
            count: likes.length,
            likedByCurrentUser: likes.some(
              (like) => Number(like.userId) === CURRENT_USER_ID
            ),
          };
        });

        setPostLikes(likesMap);

        // ============================
        // LOAD SAVES
        // ============================

        const saveResults = await Promise.all(
          postsData.map(async (post) => {
            try {
              const response = await axios.get(
                `http://localhost:8080/api/saves/post/${post.postId}`
              );

              const saves = response.data || [];

              return {
                postId: post.postId,
                saves: saves,
              };
            } catch (saveError) {
              console.error(
                `Unable to load saves for post ${post.postId}`,
                saveError
              );

              return {
                postId: post.postId,
                saves: [],
              };
            }
          })
        );

        const savesMap = {};

        saveResults.forEach((item) => {
          const saves = item.saves || [];

          savesMap[item.postId] = {
            count: saves.length,
            savedByCurrentUser: saves.some(
              (save) => Number(save.userId) === CURRENT_USER_ID
            ),
          };
        });

        setPostSaves(savesMap);

        // ============================
        // LOAD COMMENTS
        // ============================

        const commentResults = await Promise.all(
          postsData.map(async (post) => {
            try {
              const response = await axios.get(
                `http://localhost:8080/api/comments/post/${post.postId}`
              );

              return {
                postId: post.postId,
                comments: response.data || [],
              };
            } catch (commentError) {
              console.error(
                `Unable to load comments for post ${post.postId}`,
                commentError
              );

              return {
                postId: post.postId,
                comments: [],
              };
            }
          })
        );

        const commentsMap = {};

        commentResults.forEach((item) => {
          commentsMap[item.postId] = item.comments || [];
        });

        setPostComments(commentsMap);

        // ============================
        // LOAD FOLLOW STATUS
        // ============================

        const uniqueUserIds = [
          ...new Set(
            postsData
              .map((post) => Number(post.userId))
              .filter(
                (userId) =>
                  Number.isInteger(userId) &&
                  userId > 0 &&
                  userId !== CURRENT_USER_ID
              )
          ),
        ];

        const followResults = await Promise.all(
          uniqueUserIds.map(async (userId) => {
            try {
              const [checkResponse, followersResponse] =
                await Promise.all([
                  axios.get(
                    `http://localhost:8080/api/follows/check?followerId=${CURRENT_USER_ID}&followingId=${userId}`
                  ),
                  axios.get(
                    `http://localhost:8080/api/follows/user/${userId}`
                  ),
                ]);

              return {
                userId: userId,
                following: Boolean(checkResponse.data),
                followerCount: (followersResponse.data || []).length,
              };
            } catch (followError) {
              console.error(
                `Unable to load follow data for user ${userId}`,
                followError
              );

              return {
                userId: userId,
                following: false,
                followerCount: 0,
              };
            }
          })
        );

        const followsMap = {};

        followResults.forEach((item) => {
          followsMap[item.userId] = {
            following: item.following,
            followerCount: item.followerCount,
          };
        });

        setPostFollows(followsMap);

        console.log("Posts:", postsData);
        console.log("Destinations:", destinationsData);
        console.log("Photos:", photoMap);
        console.log("Likes:", likesMap);
        console.log("Saves:", savesMap);
        console.log("Comments:", commentsMap);
        console.log("Follows:", followsMap);
      } catch (error) {
        console.error("Explore loading error:", error);

        setError(
          "Unable to load explore data. Please make sure the backend is running."
        );
      } finally {
        setLoading(false);
      }
    };

    fetchExploreData();
  }, []);

  // ============================
  // LIKE / UNLIKE
  // ============================

  const handleLikeToggle = async (postId) => {
    if (likingPostId === postId) {
      return;
    }

    const currentLikeData = postLikes[postId] || {
      count: 0,
      likedByCurrentUser: false,
    };

    try {
      setLikingPostId(postId);

      if (currentLikeData.likedByCurrentUser) {
        // UNLIKE
        await axios.delete(
          `http://localhost:8080/api/likes/remove?userId=${CURRENT_USER_ID}&postId=${postId}`
        );

        setPostLikes((previousLikes) => ({
          ...previousLikes,
          [postId]: {
            count: Math.max(0, currentLikeData.count - 1),
            likedByCurrentUser: false,
          },
        }));
      } else {
        // LIKE
        await axios.post("http://localhost:8080/api/likes", {
          postId: postId,
          userId: CURRENT_USER_ID,
        });

        setPostLikes((previousLikes) => ({
          ...previousLikes,
          [postId]: {
            count: currentLikeData.count + 1,
            likedByCurrentUser: true,
          },
        }));
      }
    } catch (error) {
      console.error("Like toggle error:", error);

      alert("Unable to update like. Please try again.");
    } finally {
      setLikingPostId(null);
    }
  };

  // ============================
  // SAVE / UNSAVE
  // ============================

  const handleSaveToggle = async (postId) => {
    if (savingPostId === postId) {
      return;
    }

    const currentSaveData = postSaves[postId] || {
      count: 0,
      savedByCurrentUser: false,
    };

    try {
      setSavingPostId(postId);

      if (currentSaveData.savedByCurrentUser) {
        // UNSAVE
        await axios.delete(
          `http://localhost:8080/api/saves/remove?userId=${CURRENT_USER_ID}&postId=${postId}`
        );

        setPostSaves((previousSaves) => ({
          ...previousSaves,
          [postId]: {
            count: Math.max(0, currentSaveData.count - 1),
            savedByCurrentUser: false,
          },
        }));
      } else {
        // SAVE
        await axios.post("http://localhost:8080/api/saves", {
          postId: postId,
          userId: CURRENT_USER_ID,
        });

        setPostSaves((previousSaves) => ({
          ...previousSaves,
          [postId]: {
            count: currentSaveData.count + 1,
            savedByCurrentUser: true,
          },
        }));
      }
    } catch (error) {
      console.error("Save toggle error:", error);

      alert("Unable to update save. Please try again.");
    } finally {
      setSavingPostId(null);
    }
  };

  // ============================
  // FOLLOW / UNFOLLOW
  // ============================

  const handleFollowToggle = async (userId) => {
    const numericUserId = Number(userId);

    if (
      !numericUserId ||
      numericUserId === CURRENT_USER_ID ||
      followingUserId === numericUserId
    ) {
      return;
    }

    const currentFollowData = postFollows[numericUserId] || {
      following: false,
      followerCount: 0,
    };

    try {
      setFollowingUserId(numericUserId);

      if (currentFollowData.following) {
        // UNFOLLOW

        const followResponse = await axios.get(
          `http://localhost:8080/api/follows/check?followerId=${CURRENT_USER_ID}&followingId=${numericUserId}`
        );

        if (followResponse.data) {
          const followsResponse = await axios.get(
            `http://localhost:8080/api/follows/user/${numericUserId}`
          );

          const currentFollow = (followsResponse.data || []).find(
            (follow) =>
              Number(follow.followerId) === CURRENT_USER_ID &&
              Number(follow.followingId) === numericUserId
          );

          if (currentFollow?.followId) {
            await axios.delete(
              `http://localhost:8080/api/follows/${currentFollow.followId}`
            );
          }
        }

        setPostFollows((previousFollows) => ({
          ...previousFollows,
          [numericUserId]: {
            following: false,
            followerCount: Math.max(
              0,
              currentFollowData.followerCount - 1
            ),
          },
        }));
      } else {
        // FOLLOW

        await axios.post("http://localhost:8080/api/follows", {
          followerId: CURRENT_USER_ID,
          followingId: numericUserId,
        });

        setPostFollows((previousFollows) => ({
          ...previousFollows,
          [numericUserId]: {
            following: true,
            followerCount:
              currentFollowData.followerCount + 1,
          },
        }));
      }
    } catch (error) {
      console.error("Follow toggle error:", error);

      alert("Unable to update follow. Please try again.");
    } finally {
      setFollowingUserId(null);
    }
  };

  // ============================
  // ADD COMMENT
  // ============================

  const handleAddComment = async (postId) => {
    const text = (commentText[postId] || "").trim();

    if (!text) {
      return;
    }

    if (commentingPostId === postId) {
      return;
    }

    try {
      setCommentingPostId(postId);

      const response = await axios.post(
        "http://localhost:8080/api/comments",
        {
          content: text,
          postId: postId,
          userId: CURRENT_USER_ID,
        }
      );

      setPostComments((previousComments) => ({
        ...previousComments,
        [postId]: [
          ...(previousComments[postId] || []),
          response.data,
        ],
      }));

      setCommentText((previousText) => ({
        ...previousText,
        [postId]: "",
      }));
    } catch (error) {
      console.error("Comment add error:", error);

      alert("Unable to add comment. Please try again.");
    } finally {
      setCommentingPostId(null);
    }
  };

  const filteredDestinations = destinations.filter((destination) => {
    const search = searchTerm.toLowerCase();

    return (
      destination.name?.toLowerCase().includes(search) ||
      destination.city?.toLowerCase().includes(search) ||
      destination.state?.toLowerCase().includes(search) ||
      destination.country?.toLowerCase().includes(search)
    );
  });

  const filteredPosts = posts.filter((post) => {
    const search = searchTerm.toLowerCase();

    return (
      post.title?.toLowerCase().includes(search) ||
      post.description?.toLowerCase().includes(search)
    );
  });

  // ============================
  // LOADING
  // ============================

  if (loading) {
    return (
      <div
        style={{
          minHeight: "80vh",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          background: "#f8fafc",
        }}
      >
        <div style={{ textAlign: "center" }}>
          <div style={{ fontSize: "55px", marginBottom: "15px" }}>
            🌍
          </div>

          <h2>Loading Explore...</h2>

          <p style={{ color: "#64748b" }}>
            Discovering amazing travel experiences for you.
          </p>
        </div>
      </div>
    );
  }

  // ============================
  // ERROR
  // ============================

  if (error) {
    return (
      <div
        style={{
          minHeight: "80vh",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          background: "#f8fafc",
        }}
      >
        <div
          style={{
            textAlign: "center",
            background: "white",
            padding: "40px",
            borderRadius: "20px",
            boxShadow: "0 10px 30px rgba(0,0,0,0.08)",
          }}
        >
          <div style={{ fontSize: "50px" }}>⚠️</div>

          <h2>Something went wrong</h2>

          <p style={{ color: "#64748b" }}>{error}</p>

          <button
            onClick={() => window.location.reload()}
            style={{
              marginTop: "15px",
              padding: "12px 24px",
              border: "none",
              borderRadius: "10px",
              background: "#4f46e5",
              color: "white",
              fontWeight: "600",
              cursor: "pointer",
            }}
          >
            Try Again
          </button>
        </div>
      </div>
    );
  }

  return (
    <div
      style={{
        background: "#f8fafc",
        minHeight: "100vh",
      }}
    >
      {/* ================= HERO ================= */}

      <section
        style={{
          background:
            "linear-gradient(135deg, #eef2ff 0%, #f5f3ff 50%, #fff7ed 100%)",
          padding: "75px 30px",
          borderRadius: "0 0 35px 35px",
        }}
      >
        <div
          style={{
            maxWidth: "1200px",
            margin: "0 auto",
            display: "grid",
            gridTemplateColumns: "1fr 1fr",
            alignItems: "center",
            gap: "50px",
          }}
        >
          <div>
            <p
              style={{
                color: "#4f46e5",
                fontWeight: "800",
                letterSpacing: "2px",
                fontSize: "16px",
                marginBottom: "15px",
              }}
            >
              DISCOVER THE WORLD
            </p>

            <h1
              style={{
                fontSize: "clamp(42px, 5vw, 68px)",
                lineHeight: "1.05",
                color: "#172554",
                margin: "0",
                fontWeight: "800",
              }}
            >
              Explore Amazing
              <br />
              Journeys 🌍
            </h1>

            <p
              style={{
                color: "#475569",
                fontSize: "18px",
                lineHeight: "1.7",
                marginTop: "25px",
                maxWidth: "600px",
              }}
            >
              Discover beautiful destinations, travel stories,
              and unforgettable memories shared by the TripTales AI
              community.
            </p>
          </div>

          <div
            style={{
              display: "flex",
              justifyContent: "center",
            }}
          >
            <div
              style={{
                width: "100%",
                maxWidth: "500px",
                background: "rgba(255,255,255,0.75)",
                padding: "30px",
                borderRadius: "24px",
                boxShadow:
                  "0 20px 50px rgba(79,70,229,0.12)",
              }}
            >
              <div
                style={{
                  fontSize: "70px",
                  textAlign: "center",
                  marginBottom: "20px",
                }}
              >
                🗺️
              </div>

              <h3
                style={{
                  textAlign: "center",
                  color: "#172554",
                  fontSize: "24px",
                  marginBottom: "20px",
                }}
              >
                Find Your Next Destination
              </h3>

              <input
                type="text"
                value={searchTerm}
                onChange={(event) =>
                  setSearchTerm(event.target.value)
                }
                placeholder="Search destinations or travel stories..."
                style={{
                  width: "100%",
                  boxSizing: "border-box",
                  padding: "16px 18px",
                  borderRadius: "12px",
                  border: "1px solid #cbd5e1",
                  background: "white",
                  fontSize: "16px",
                  outline: "none",
                }}
              />
            </div>
          </div>
        </div>
      </section>

      {/* ================= DESTINATIONS ================= */}

      <section
        style={{
          maxWidth: "1200px",
          margin: "65px auto",
          padding: "0 25px",
        }}
      >
        <p
          style={{
            color: "#6366f1",
            fontWeight: "800",
            letterSpacing: "1.5px",
            marginBottom: "8px",
          }}
        >
          DESTINATIONS
        </p>

        <h2
          style={{
            fontSize: "36px",
            color: "#172033",
            marginBottom: "35px",
          }}
        >
          Places Worth Exploring ✈️
        </h2>

        <div
          style={{
            display: "grid",
            gridTemplateColumns:
              "repeat(auto-fit, minmax(230px, 1fr))",
            gap: "28px",
          }}
        >
          {filteredDestinations.map((destination) => (
            <div
              key={destination.destinationId}
              style={{
                background: "white",
                borderRadius: "20px",
                overflow: "hidden",
                boxShadow:
                  "0 8px 25px rgba(15,23,42,0.08)",
                transition: "transform 0.2s",
              }}
            >
              <div
                style={{
                  height: "180px",
                  background:
                    "linear-gradient(135deg, #6366f1, #8b5cf6)",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  fontSize: "65px",
                }}
              >
                🌄
              </div>

              <div style={{ padding: "22px" }}>
                <p
                  style={{
                    color: "#2563eb",
                    fontWeight: "700",
                    marginBottom: "8px",
                  }}
                >
                  📍 {destination.city || destination.name}
                </p>

                <h3
                  style={{
                    fontSize: "25px",
                    margin: "0 0 10px",
                    color: "#172033",
                  }}
                >
                  {destination.name}
                </h3>

                <p
                  style={{
                    color: "#64748b",
                    lineHeight: "1.6",
                    margin: 0,
                  }}
                >
                  {destination.description ||
                    "Explore this beautiful travel destination."}
                </p>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* ================= COMMUNITY POSTS ================= */}

      <section
        style={{
          maxWidth: "1200px",
          margin: "65px auto",
          padding: "0 25px",
        }}
      >
        <p
          style={{
            color: "#6366f1",
            fontWeight: "800",
            letterSpacing: "1.5px",
            marginBottom: "8px",
          }}
        >
          COMMUNITY
        </p>

        <h2
          style={{
            fontSize: "36px",
            color: "#172033",
            marginBottom: "35px",
          }}
        >
          Latest Travel Stories 📸
        </h2>

        {filteredPosts.length === 0 ? (
          <div
            style={{
              background: "white",
              padding: "50px",
              textAlign: "center",
              borderRadius: "20px",
            }}
          >
            <div style={{ fontSize: "55px" }}>📷</div>

            <h3>No travel stories found</h3>

            <p style={{ color: "#64748b" }}>
              Try another search.
            </p>
          </div>
        ) : (
          <div
            style={{
              display: "grid",
              gridTemplateColumns:
                "repeat(auto-fit, minmax(260px, 1fr))",
              gap: "28px",
            }}
          >
            {filteredPosts.map((post) => {
              const photos =
                postPhotos[post.postId] || [];

              const firstPhoto =
                photos.length > 0
                  ? photos[0]
                  : null;

              const imageUrl =
                firstPhoto?.imageUrl;

              const fullImageUrl =
                imageUrl &&
                imageUrl.startsWith("/")
                  ? `http://localhost:8080${imageUrl}`
                  : imageUrl;

              const likeData =
                postLikes[post.postId] || {
                  count: 0,
                  likedByCurrentUser: false,
                };

              const saveData =
                postSaves[post.postId] || {
                  count: 0,
                  savedByCurrentUser: false,
                };

              const postUserId = Number(post.userId);

              const followData =
                postFollows[postUserId] || {
                  following: false,
                  followerCount: 0,
                };

              const isOwnPost =
                postUserId === CURRENT_USER_ID;

              return (
                <article
                  key={post.postId}
                  style={{
                    background: "white",
                    borderRadius: "20px",
                    overflow: "hidden",
                    boxShadow:
                      "0 8px 25px rgba(15,23,42,0.08)",
                  }}
                >
                  {/* IMAGE */}

                  {fullImageUrl ? (
                    <img
                      src={fullImageUrl}
                      alt={post.title}
                      onError={(event) => {
                        event.currentTarget.style.display =
                          "none";

                        event.currentTarget.nextSibling.style.display =
                          "flex";
                      }}
                      style={{
                        width: "100%",
                        height: "240px",
                        objectFit: "cover",
                        display: "block",
                      }}
                    />
                  ) : null}

                  {/* FALLBACK */}

                  <div
                    style={{
                      height: "240px",
                      background:
                        "linear-gradient(135deg, #6366f1, #8b5cf6)",
                      alignItems: "center",
                      justifyContent: "center",
                      fontSize: "70px",
                      display: fullImageUrl
                        ? "none"
                        : "flex",
                    }}
                  >
                    📸
                  </div>

                  <div style={{ padding: "22px" }}>
                    {/* ============================
                        AUTHOR + FOLLOW
                    ============================ */}

                    <div
                      style={{
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "space-between",
                        gap: "12px",
                        marginBottom: "15px",
                        paddingBottom: "15px",
                        borderBottom:
                          "1px solid #e2e8f0",
                      }}
                    >
                      <div
                        style={{
                          display: "flex",
                          alignItems: "center",
                          gap: "10px",
                        }}
                      >
                        <div
                          style={{
                            width: "42px",
                            height: "42px",
                            borderRadius: "50%",
                            background:
                              "linear-gradient(135deg, #6366f1, #8b5cf6)",
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "center",
                            color: "white",
                            fontSize: "18px",
                            fontWeight: "800",
                          }}
                        >
                          👤
                        </div>

                        <div>
                          <div
                            style={{
                              fontWeight: "800",
                              color: "#172033",
                              fontSize: "14px",
                            }}
                          >
                            User {post.userId}
                          </div>

                          <div
                            style={{
                              color: "#94a3b8",
                              fontSize: "12px",
                              marginTop: "2px",
                            }}
                          >
                            {followData.followerCount} follower
                            {followData.followerCount !== 1
                              ? "s"
                              : ""}
                          </div>
                        </div>
                      </div>

                      {!isOwnPost && postUserId > 0 && (
                        <button
                          onClick={() =>
                            handleFollowToggle(postUserId)
                          }
                          disabled={
                            followingUserId === postUserId
                          }
                          style={{
                            border: "none",
                            borderRadius: "10px",
                            padding: "9px 15px",
                            background:
                              followData.following
                                ? "#ede9fe"
                                : "#4f46e5",
                            color:
                              followData.following
                                ? "#6d28d9"
                                : "white",
                            fontWeight: "800",
                            cursor:
                              followingUserId === postUserId
                                ? "wait"
                                : "pointer",
                            opacity:
                              followingUserId === postUserId
                                ? 0.6
                                : 1,
                          }}
                        >
                          {followingUserId === postUserId
                            ? "..."
                            : followData.following
                            ? "Following"
                            : "Follow"}
                        </button>
                      )}
                    </div>

                    <p
                      style={{
                        color: "#2563eb",
                        fontWeight: "700",
                        marginBottom: "10px",
                      }}
                    >
                      📅 {post.travelDate}
                    </p>

                    <h3
                      style={{
                        fontSize: "24px",
                        lineHeight: "1.2",
                        color: "#172033",
                        margin: "0 0 12px",
                      }}
                    >
                      {post.title}
                    </h3>

                    <p
                      style={{
                        color: "#64748b",
                        lineHeight: "1.6",
                        margin: 0,
                      }}
                    >
                      {post.description}
                    </p>

                    {firstPhoto && (
                      <p
                        style={{
                          color: "#6366f1",
                          fontWeight: "700",
                          marginTop: "18px",
                          marginBottom: 0,
                        }}
                      >
                        📸 Travel photo
                      </p>
                    )}

                    {/* ============================
                        LIKE + SAVE BUTTONS
                    ============================ */}

                    <div
                      style={{
                        display: "flex",
                        alignItems: "center",
                        gap: "10px",
                        marginTop: "20px",
                        paddingTop: "15px",
                        borderTop:
                          "1px solid #e2e8f0",
                      }}
                    >
                      {/* LIKE */}

                      <button
                        onClick={() =>
                          handleLikeToggle(post.postId)
                        }
                        disabled={
                          likingPostId === post.postId
                        }
                        style={{
                          display: "flex",
                          alignItems: "center",
                          gap: "7px",
                          border: "none",
                          background: "transparent",
                          cursor:
                            likingPostId === post.postId
                              ? "wait"
                              : "pointer",
                          padding: "8px 10px",
                          borderRadius: "10px",
                          color:
                            likeData.likedByCurrentUser
                              ? "#e11d48"
                              : "#64748b",
                          fontSize: "16px",
                          fontWeight: "700",
                          opacity:
                            likingPostId === post.postId
                              ? 0.6
                              : 1,
                        }}
                      >
                        <span
                          style={{
                            fontSize: "25px",
                          }}
                        >
                          {likeData.likedByCurrentUser
                            ? "❤️"
                            : "🤍"}
                        </span>

                        <span>
                          {likingPostId === post.postId
                            ? "Updating..."
                            : likeData.likedByCurrentUser
                            ? "Liked"
                            : "Like"}
                        </span>

                        <span
                          style={{
                            color: "#475569",
                            fontWeight: "600",
                          }}
                        >
                          {likeData.count}
                        </span>
                      </button>

                      {/* SAVE */}

                      <button
                        onClick={() =>
                          handleSaveToggle(post.postId)
                        }
                        disabled={
                          savingPostId === post.postId
                        }
                        style={{
                          display: "flex",
                          alignItems: "center",
                          gap: "7px",
                          border: "none",
                          background: "transparent",
                          cursor:
                            savingPostId === post.postId
                              ? "wait"
                              : "pointer",
                          padding: "8px 10px",
                          borderRadius: "10px",
                          color:
                            saveData.savedByCurrentUser
                              ? "#7c3aed"
                              : "#64748b",
                          fontSize: "16px",
                          fontWeight: "700",
                          opacity:
                            savingPostId === post.postId
                              ? 0.6
                              : 1,
                        }}
                      >
                        <span
                          style={{
                            fontSize: "23px",
                          }}
                        >
                          🔖
                        </span>

                        <span>
                          {savingPostId === post.postId
                            ? "Updating..."
                            : saveData.savedByCurrentUser
                            ? "Saved"
                            : "Save"}
                        </span>

                        <span
                          style={{
                            color: "#475569",
                            fontWeight: "600",
                          }}
                        >
                          {saveData.count}
                        </span>
                      </button>
                    </div>

                    {/* ============================
                        COMMENTS
                    ============================ */}

                    <div
                      style={{
                        marginTop: "15px",
                        paddingTop: "15px",
                        borderTop:
                          "1px solid #e2e8f0",
                      }}
                    >
                      <div
                        style={{
                          display: "flex",
                          alignItems: "center",
                          justifyContent: "space-between",
                          marginBottom: "12px",
                        }}
                      >
                        <strong
                          style={{
                            color: "#334155",
                            fontSize: "15px",
                          }}
                        >
                          💬 Comments
                        </strong>

                        <span
                          style={{
                            color: "#64748b",
                            fontWeight: "600",
                            fontSize: "14px",
                          }}
                        >
                          {
                            (postComments[post.postId] || [])
                              .length
                          }
                        </span>
                      </div>

                      {(postComments[post.postId] || [])
                        .length > 0 && (
                        <div
                          style={{
                            maxHeight: "180px",
                            overflowY: "auto",
                            marginBottom: "12px",
                          }}
                        >
                          {(
                            postComments[post.postId] || []
                          ).map((comment) => (
                            <div
                              key={comment.commentId}
                              style={{
                                background: "#f8fafc",
                                borderRadius: "10px",
                                padding: "10px 12px",
                                marginBottom: "8px",
                              }}
                            >
                              <div
                                style={{
                                  color: "#172033",
                                  fontSize: "14px",
                                  lineHeight: "1.5",
                                }}
                              >
                                {comment.content}
                              </div>

                              <div
                                style={{
                                  color: "#94a3b8",
                                  fontSize: "12px",
                                  marginTop: "4px",
                                }}
                              >
                                User {comment.userId}
                              </div>
                            </div>
                          ))}
                        </div>
                      )}

                      <div
                        style={{
                          display: "flex",
                          gap: "8px",
                          alignItems: "center",
                        }}
                      >
                        <input
                          type="text"
                          value={
                            commentText[post.postId] || ""
                          }
                          onChange={(event) =>
                            setCommentText(
                              (previousText) => ({
                                ...previousText,
                                [post.postId]:
                                  event.target.value,
                              })
                            )
                          }
                          onKeyDown={(event) => {
                            if (event.key === "Enter") {
                              handleAddComment(post.postId);
                            }
                          }}
                          placeholder="Write a comment..."
                          disabled={
                            commentingPostId === post.postId
                          }
                          style={{
                            flex: 1,
                            minWidth: 0,
                            padding: "10px 12px",
                            border:
                              "1px solid #cbd5e1",
                            borderRadius: "10px",
                            outline: "none",
                            fontSize: "14px",
                            background: "white",
                          }}
                        />

                        <button
                          onClick={() =>
                            handleAddComment(post.postId)
                          }
                          disabled={
                            commentingPostId ===
                              post.postId ||
                            !(
                              commentText[post.postId] ||
                              ""
                            ).trim()
                          }
                          style={{
                            border: "none",
                            borderRadius: "10px",
                            padding: "10px 14px",
                            background:
                              commentingPostId ===
                                post.postId ||
                              !(
                                commentText[
                                  post.postId
                                ] || ""
                              ).trim()
                                ? "#cbd5e1"
                                : "#4f46e5",
                            color: "white",
                            fontWeight: "700",
                            cursor:
                              commentingPostId ===
                                post.postId ||
                              !(
                                commentText[
                                  post.postId
                                ] || ""
                              ).trim()
                                ? "not-allowed"
                                : "pointer",
                          }}
                        >
                          {commentingPostId === post.postId
                            ? "..."
                            : "Post"}
                        </button>
                      </div>
                    </div>
                  </div>
                </article>
              );
            })}
          </div>
        )}
      </section>

      {/* ================= CTA ================= */}

      <section
        style={{
          maxWidth: "1100px",
          margin: "80px auto",
          padding: "65px 30px",
          textAlign: "center",
          borderRadius: "30px",
          background:
            "linear-gradient(135deg, #4f46e5, #7c3aed)",
          color: "white",
        }}
      >
        <div
          style={{
            fontSize: "55px",
            marginBottom: "15px",
          }}
        >
          ✈️
        </div>

        <h2
          style={{
            fontSize: "38px",
            marginBottom: "15px",
          }}
        >
          Your Next Journey Starts Here
        </h2>

        <p
          style={{
            maxWidth: "650px",
            margin: "0 auto 30px",
            lineHeight: "1.7",
            fontSize: "17px",
          }}
        >
          Share your travel memories, discover new destinations,
          and connect with fellow travelers.
        </p>

        <a
          href="/create-post"
          style={{
            display: "inline-block",
            padding: "15px 30px",
            background: "white",
            color: "#4f46e5",
            borderRadius: "12px",
            textDecoration: "none",
            fontWeight: "800",
          }}
        >
          Share Your Journey 📸
        </a>
      </section>
    </div>
  );
}

export default Explore;