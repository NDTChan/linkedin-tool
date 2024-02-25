package com.hanstack.linkedintool.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ToolbarEnum {
    JOBS("Jobs", 0),
    POSTS("Posts", 1),
    PEOPLE("People", 2),
    GROUPS("Groups", 3),
    EVENTS("Events", 4),
    SCHOOLS("Schools", 5),
    COURSES("Courses", 6),
    PRODUCTS("Products", 7),
    COMPANIES("Companies", 8),
    SERVICES("Services", 9);

    private final String name;
    private final int value;
}
