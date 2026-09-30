import { useEffect, useState } from "react";
import axios from "axios";

function Explore() {
  const [posts, setPosts] = useState([]);
  const [postPhotos, setPostPhotos] = useState({});
  const [destinations, setDestinations] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

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

        console.log("Posts:", postsData);
        console.log("Destinations:", destinationsData);
        console.log("Photos:", photoMap);

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
                boxShadow: "0 20px 50px rgba(79,70,229,0.12)",
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

                  {/* Image */}

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

                  {/* Fallback */}

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