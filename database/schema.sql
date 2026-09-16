CREATE DATABASE IF NOT EXISTS triptales_ai;

USE triptales_ai;

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    bio TEXT,
    profile_image VARCHAR(500),
    account_status VARCHAR(20) DEFAULT 'ACTIVE',
    theme VARCHAR(20) DEFAULT 'LIGHT',
    language VARCHAR(20) DEFAULT 'ENGLISH',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS destinations (
    destination_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),
    latitude DECIMAL(10,7),
    longitude DECIMAL(10,7),
    description TEXT,
    cover_image VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS posts (
    post_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    destination_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    travel_date DATE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_post_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id),

    CONSTRAINT fk_post_destination
        FOREIGN KEY (destination_id)
        REFERENCES destinations(destination_id)
);

CREATE TABLE IF NOT EXISTS photos (
    photo_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    caption VARCHAR(255),
    display_order INT DEFAULT 1,

    CONSTRAINT fk_photo_post
        FOREIGN KEY (post_id)
        REFERENCES posts(post_id)
);

CREATE TABLE IF NOT EXISTS comments (
    comment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_comment_post
        FOREIGN KEY (post_id)
        REFERENCES posts(post_id),

    CONSTRAINT fk_comment_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS likes (
    like_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_like_post
        FOREIGN KEY (post_id)
        REFERENCES posts(post_id),

    CONSTRAINT fk_like_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id),

    CONSTRAINT unique_user_post_like
        UNIQUE (user_id, post_id)
);

CREATE TABLE IF NOT EXISTS saves (
    save_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_save_post
        FOREIGN KEY (post_id)
        REFERENCES posts(post_id),

    CONSTRAINT fk_save_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id),

    CONSTRAINT unique_user_post_save
        UNIQUE (user_id, post_id)
);

CREATE TABLE IF NOT EXISTS follows (
    follow_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    follower_id BIGINT NOT NULL,
    following_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_follow_follower
        FOREIGN KEY (follower_id)
        REFERENCES users(user_id),

    CONSTRAINT fk_follow_following
        FOREIGN KEY (following_id)
        REFERENCES users(user_id),

    CONSTRAINT unique_user_follow
        UNIQUE (follower_id, following_id),

    CONSTRAINT check_no_self_follow
        CHECK (follower_id <> following_id)
);

CREATE TABLE IF NOT EXISTS travel_memories (
    memory_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    destination_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    memory_date DATE,
    cover_image VARCHAR(500),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_memory_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id),

    CONSTRAINT fk_memory_destination
        FOREIGN KEY (destination_id)
        REFERENCES destinations(destination_id)
);

CREATE TABLE IF NOT EXISTS trips (
    trip_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    status VARCHAR(20) DEFAULT 'PLANNED',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_trip_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);


CREATE TABLE IF NOT EXISTS trip_destinations (
    trip_destination_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    trip_id BIGINT NOT NULL,
    destination_id BIGINT NOT NULL,
    visit_order INT NOT NULL,
    notes TEXT,

    CONSTRAINT fk_trip_destination_trip
        FOREIGN KEY (trip_id)
        REFERENCES trips(trip_id),

    CONSTRAINT fk_trip_destination_destination
        FOREIGN KEY (destination_id)
        REFERENCES destinations(destination_id),

    CONSTRAINT unique_trip_destination
        UNIQUE (trip_id, destination_id),

    CONSTRAINT unique_trip_visit_order
        UNIQUE (trip_id, visit_order)
);

CREATE TABLE IF NOT EXISTS recommendations (
    recommendation_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    destination_id BIGINT NOT NULL,
    reason TEXT,
    score DECIMAL(5,2),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_recommendation_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id),

    CONSTRAINT fk_recommendation_destination
        FOREIGN KEY (destination_id)
        REFERENCES destinations(destination_id)
);

CREATE TABLE IF NOT EXISTS badges (
    badge_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    icon VARCHAR(500),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_badges (
    user_badge_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    badge_id BIGINT NOT NULL,
    earned_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_badge_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id),

    CONSTRAINT fk_user_badge_badge
        FOREIGN KEY (badge_id)
        REFERENCES badges(badge_id),

    CONSTRAINT unique_user_badge
        UNIQUE (user_id, badge_id)
);


CREATE TABLE IF NOT EXISTS notifications (
    notification_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    message VARCHAR(500) NOT NULL,
    reference_id BIGINT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_notification_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);


CREATE TABLE IF NOT EXISTS user_privacy_settings (
    privacy_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    profile_visibility VARCHAR(20) DEFAULT 'PUBLIC',
    location_visibility VARCHAR(20) DEFAULT 'DESTINATION',
    show_travel_map BOOLEAN DEFAULT TRUE,
    allow_follow_requests BOOLEAN DEFAULT TRUE,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_privacy_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);



CREATE TABLE IF NOT EXISTS user_notification_settings (
    notification_settings_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    likes_enabled BOOLEAN DEFAULT TRUE,
    comments_enabled BOOLEAN DEFAULT TRUE,
    followers_enabled BOOLEAN DEFAULT TRUE,
    recommendations_enabled BOOLEAN DEFAULT TRUE,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_notification_settings_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);


