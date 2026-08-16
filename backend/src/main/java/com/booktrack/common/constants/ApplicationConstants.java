package com.booktrack.common.constants;

public final class ApplicationConstants {

    private ApplicationConstants() {
    }

    public static final String API_V1 = "/api/v1";

    public static final String AUTH = API_V1 + "/auth";

    public static final String CATEGORIES = API_V1 + "/categories";

    public static final String AUTHORS = API_V1 + "/authors";

    public static final String PUBLISHERS = API_V1 + "/publishers";

    public static final String BOOK_COPIES = API_V1 + "/book-copies";

    public static final String BORROWINGS = API_V1 + "/borrowings";

    public static final String RESERVATIONS = API_V1 + "/reservations";

    public static final String BOOKS = API_V1 + "/books";

    public static final String INVENTORY = API_V1 + "/inventory";

    public static final String NOTIFICATIONS = API_V1 + "/notifications";

    public static final String USERS = API_V1 + "/users";

    public static final String TOKEN_TYPE = "type";

    public static final String ACCESS_TOKEN = "ACCESS";

    public static final String REFRESH_TOKEN = "REFRESH";

    public static final String ROLES = "roles";

    public static final String USER_ID = "uid";

    public static final String BEARER = "Bearer ";

}