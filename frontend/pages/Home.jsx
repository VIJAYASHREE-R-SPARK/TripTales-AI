function Home() {
  return (
    <div className="home-page">

      <section className="hero-section">
        <div className="hero-content">
          <h1>Capture Your Journey.</h1>

          <h2>Share Your Story.</h2>

          <p>
            Discover beautiful destinations, share your travel memories,
            and connect with travelers around the world.
          </p>

          <div className="hero-buttons">
            <button>Explore Destinations</button>
            <button>Share Your Journey</button>
          </div>
        </div>
      </section>

      <section className="features-section">
        <h2>Why TripTales AI?</h2>

        <div className="features-grid">

          <div className="feature-card">
            <h3>📸 Travel Photos</h3>
            <p>
              Share your travel photos and experiences with the community.
            </p>
          </div>

          <div className="feature-card">
            <h3>🗺️ Travel Memories</h3>
            <p>
              Organize your journeys and build your personal travel memory map.
            </p>
          </div>

          <div className="feature-card">
            <h3>🤖 Smart Recommendations</h3>
            <p>
              Discover destinations based on your travel interests and activity.
            </p>
          </div>

          <div className="feature-card">
            <h3>👥 Travel Community</h3>
            <p>
              Follow travelers, like posts, comment, and discover new experiences.
            </p>
          </div>

        </div>
      </section>

    </div>
  );
}

export default Home;