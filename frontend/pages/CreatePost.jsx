import { useState } from "react";
import axios from "axios";

function CreatePost() {
  const [formData, setFormData] = useState({
    title: "",
    description: "",
    destinationId: "",
    travelDate: "",
  });

  const [selectedFile, setSelectedFile] = useState(null);

  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  // Temporary logged-in user
  const userId = 7;

  const handleChange = (event) => {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value,
    });
  };

  const handleFileChange = (event) => {
    const file = event.target.files[0];

    if (!file) {
      setSelectedFile(null);
      return;
    }

    // Allow only image files
    if (!file.type.startsWith("image/")) {
      setError("Please select a valid image file.");
      setSelectedFile(null);
      return;
    }

    setError("");
    setSelectedFile(file);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    setMessage("");
    setError("");

    try {
      // --------------------------------
      // STEP 1: CREATE TRAVEL POST
      // --------------------------------

      const postData = {
        userId: userId,
        destinationId: Number(formData.destinationId),
        title: formData.title,
        description: formData.description,
        travelDate: formData.travelDate,
      };

      const postResponse = await axios.post(
        "http://localhost:8080/api/posts",
        postData
      );

      const createdPost = postResponse.data;

      console.log("Post created:", createdPost);

      const createdPostId = createdPost.postId;

      // --------------------------------
      // STEP 2: UPLOAD PHOTO
      // --------------------------------

      if (selectedFile) {
        const photoData = new FormData();

        photoData.append("postId", createdPostId);
        photoData.append("file", selectedFile);
        photoData.append(
          "caption",
          formData.title
        );
        photoData.append(
          "displayOrder",
          "1"
        );

        const photoResponse = await axios.post(
          "http://localhost:8080/api/photos/upload",
          photoData
        );

        console.log(
          "Photo uploaded:",
          photoResponse.data
        );
      }

      // --------------------------------
      // SUCCESS
      // --------------------------------

      setMessage(
        selectedFile
          ? "Your travel post and photo were published successfully! 🎉📸"
          : "Your travel post was created successfully! 🎉"
      );

      setFormData({
        title: "",
        description: "",
        destinationId: "",
        travelDate: "",
      });

      setSelectedFile(null);

      // Reset file input
      event.target.reset();

    } catch (error) {
      console.error(
        "Create post error:",
        error
      );

      if (error.response?.data?.message) {
        setError(
          error.response.data.message
        );
      } else {
        setError(
          "Unable to create travel post. Please try again."
        );
      }
    }
  };

  return (
    <div className="auth-page">

      <div className="auth-card">

        <h1>
          Share Your Journey ✈️
        </h1>

        <p className="auth-subtitle">
          Create a travel post, upload your
          favorite photo, and share your
          experience with the TripTales AI
          community.
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

          {/* TITLE */}

          <div className="form-group">

            <label>
              Travel Title
            </label>

            <input
              type="text"
              name="title"
              value={formData.title}
              onChange={handleChange}
              placeholder="Example: My Amazing Trip to Ooty"
              required
            />

          </div>

          {/* STORY */}

          <div className="form-group">

            <label>
              Travel Story
            </label>

            <textarea
              name="description"
              value={formData.description}
              onChange={handleChange}
              placeholder="Tell us about your travel experience..."
              rows="5"
              required
            />

          </div>

          {/* DESTINATION */}

          <div className="form-group">

            <label>
              Destination
            </label>

            <select
              name="destinationId"
              value={formData.destinationId}
              onChange={handleChange}
              required
            >

              <option value="">
                Select a destination
              </option>

              <option value="1">
                Ooty
              </option>

              <option value="2">
                Goa
              </option>

              <option value="3">
                Munnar
              </option>

              <option value="4">
                Jaipur
              </option>

              <option value="5">
                Manali
              </option>

            </select>

          </div>

          {/* TRAVEL DATE */}

          <div className="form-group">

            <label>
              Travel Date
            </label>

            <input
              type="date"
              name="travelDate"
              value={formData.travelDate}
              onChange={handleChange}
              required
            />

          </div>

          {/* PHOTO UPLOAD */}

          <div className="form-group">

            <label>
              Travel Photo 📸
            </label>

            <input
              type="file"
              accept="image/*"
              onChange={handleFileChange}
            />

            {selectedFile && (
              <p
                style={{
                  marginTop: "10px",
                  color: "#6366f1",
                  fontWeight: "600",
                }}
              >
                Selected: {selectedFile.name}
              </p>
            )}

          </div>

          {/* SUBMIT */}

          <button
            type="submit"
            className="auth-button"
          >
            Publish Travel Post 📸
          </button>

        </form>

      </div>

    </div>
  );
}

export default CreatePost;