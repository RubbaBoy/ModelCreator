package com.uddernetworks.modelcreator.main;

public enum Texture {
    WHITE("white"),
    ORANGE("orange"),
    MAGENTA("magenta"),
    LIGHT_BLUE("light_blue"),
    YELLOW("yellow"),
    LIME("lime"),
    PINK("pink"),
    GRAY("gray"),
    LIGHT_GRAY("light_gray"),
    CYAN("cyan"),
    PURPLE("purple"),
    BLUE("blue"),
    BROWN("brown"),
    GREEN("green"),
    RED("red"),
    BLACK("black"),
    TRANSPARENT("transparent");

    private String name;

    Texture(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
